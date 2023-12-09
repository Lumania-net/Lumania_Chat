package net.lumania.chat.listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import net.lumania.chat.utils.ConfigHolder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerChatListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public PlayerChatListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    @EventHandler
    public void playerChatListener(AsyncChatEvent event) {
        Player player = event.getPlayer();

        boolean allFeatures = player.hasPermission(ConfigHolder.ALL_FEATURES);
        boolean allBypasses = player.hasPermission(ConfigHolder.ALL_BYPASSES);

        TextComponent textComponent = (TextComponent) event.message();
        String message = textComponent.content();

        if(!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_SPAM_BYPASS)) {
            long currentTimeMillis = System.currentTimeMillis();

            if(LumaniaChatPlugin.SPAM_CACHE.containsKey(player.getUniqueId())) {
                long lastMessage = LumaniaChatPlugin.SPAM_CACHE.get(player.getUniqueId());

                if(lastMessage + (ConfigHolder.ANTI_SPAM_COUNTDOWN * 1000) > currentTimeMillis) {
                    player.sendMessage(ConfigHolder.SPAM_WARNING_MESSAGE);
                    event.setCancelled(true);

                    this.chatPlugin.getLoggingService().addLog(LoggingType.WARNING, player.getName() + " spam warning: " + message, player.getUniqueId());

                    return;
                }

                LumaniaChatPlugin.SPAM_CACHE.put(player.getUniqueId(), currentTimeMillis);
            }
        }

        if(!LumaniaChatPlugin.SPAM_CACHE.containsKey(player.getUniqueId()))
            LumaniaChatPlugin.SPAM_CACHE.put(player.getUniqueId(), System.currentTimeMillis());

        /***** UNICODE *****/

        if((!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_UNICODE_BYPASS)) && this.isUnicode(message)) {
            event.setCancelled(true);
            this.chatPlugin.getLoggingService().addLog(LoggingType.WARNING, player.getName() + " sent a unicode",
                    player.getUniqueId());

            player.sendMessage(ConfigHolder.UNICODE_WARNING_MESSAGE);

            return;
        }

        /***** MUTED *****/

        if((!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.MUTE_CHAT_BYPASS)) && LumaniaChatPlugin.MUTED) {
            player.sendMessage(ConfigHolder.MUTE_CHAT_MUTED_MESSAGE);
            event.setCancelled(true);

            return;
        }

        // ADS -> SWEAR -> CAPS -> MENTION

        /***** FORMAT COLORS *****/

        message = this.formatColors(message);

        /***** ADS *****/

        if((!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_ADS_BYPASS)) && this.chatPlugin.getAdvertisementService().containsAdvertisement(message)) {
            player.sendMessage(ConfigHolder.ADS_WARNING_MESSAGE);
            event.setCancelled(true);

            this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " sent an " +
                    "advertisement, original message: " + message, player.getUniqueId());

            return;
        }

        /***** SWEAR *****/

        if(!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_SWEAR_BYPASS)) {
            if (this.swearFilter(message)) {
                event.setCancelled(true);
                player.sendMessage(ConfigHolder.SWEAR_WARNING_MESSAGE);

                this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " said a swear " +
                        "word: " + message, player.getUniqueId());

                return;
            }

        }

        /***** CAPS *****/

        if(!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_CAPS_BYPASS)) {
            int textLength = this.getTextLength(message);

            if (textLength >= ConfigHolder.ANTI_CAPS_MIN_LENGTH && getCapsPercentage(message) > ConfigHolder.ANTI_CAPS_PERCENTAGE && (!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_CAPS_BYPASS))) {
                player.sendMessage(ConfigHolder.CAPS_WARNING_MESSAGE);
                event.setCancelled(true);

                this.chatPlugin.getLoggingService().addLog(LoggingType.WARNING, player.getName() + " sent too many caps letters: " + message, player.getUniqueId());

                return;
            }
        }

        CachedMetaData cachedMetaData = this.chatPlugin.getLuckPermsApi().getPlayerAdapter(Player.class).getMetaData(player);
        String group = cachedMetaData.getPrimaryGroup();

        final String format = this.formatColors((this.chatPlugin.getConfig().getString("permission-groups." + group) != null ? this.chatPlugin.getConfig().getString("permission-groups." + group) : ""));

        ChatRenderer newRenderer = new ChatRenderer() {
            @Override
            public @NotNull Component render(@NotNull Player player, @NotNull Component component, @NotNull Component component1, @NotNull Audience audience) {
                return Component.text(format + "§8• §7" + player.getName() + "§8: §f" + formatColors(((TextComponent) component1).content()));
            }
        };

        event.renderer(newRenderer);

        /***** MENTIONS *****/

        String[] words = message.split(" ");

        List<String> mentionedPlayers = new ArrayList<>();

        for(String word : words) {
            if(word.startsWith("@")) {
                String playerName = word.substring(1);

                Player mentionedPlayer = Bukkit.getPlayer(playerName);

                if(mentionedPlayer == null || !mentionedPlayer.isOnline() || !mentionedPlayer.getName().equalsIgnoreCase(playerName))
                    continue;

                if(!mentionedPlayers.contains(mentionedPlayer.getName()))
                    mentionedPlayers.add(mentionedPlayer.getName());
            }
        }

        if(!mentionedPlayers.isEmpty()) {
            this.mentionPlayer(message, mentionedPlayers, format + "§8• §7" + player.getName() + "§8: §f");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerCommandListener(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if(!event.getMessage().startsWith("/msg ") || !event.getMessage().startsWith("/r "))
            return;

        boolean allFeatures = player.hasPermission(ConfigHolder.ALL_FEATURES);
        boolean allBypasses = player.hasPermission(ConfigHolder.ALL_BYPASSES);

        String[] contents = event.getMessage().split(" ");
        String[] args = new String[contents.length - 1];

        System.arraycopy(contents, 1, args, 0, contents.length - 1);

        String message = String.join(" ", args);

        if(!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_SPAM_BYPASS)) {
            long currentTimeMillis = System.currentTimeMillis();

            if(LumaniaChatPlugin.SPAM_CACHE.containsKey(player.getUniqueId())) {
                long lastMessage = LumaniaChatPlugin.SPAM_CACHE.get(player.getUniqueId());

                LumaniaChatPlugin.SPAM_CACHE.put(player.getUniqueId(), currentTimeMillis);

                if(lastMessage + (ConfigHolder.ANTI_SPAM_COUNTDOWN * 1000) > currentTimeMillis) {
                    player.sendMessage(ConfigHolder.SPAM_WARNING_MESSAGE);
                    event.setCancelled(true);

                    this.chatPlugin.getLoggingService().addLog(LoggingType.WARNING, player.getName() + " spam warning: " + message, player.getUniqueId());

                    return;
                }
            }
        }

        // ADS -> SWEAR -> CAPS

        /***** ADS *****/

        if((!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_ADS_BYPASS)) && this.chatPlugin.getAdvertisementService().containsAdvertisement(message)) {
            player.sendMessage(ConfigHolder.ADS_WARNING_MESSAGE);
            event.setCancelled(true);

            this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " sent an " +
                    "advertisement, original message: " + message, player.getUniqueId());

            return;
        }

        /***** SWEAR *****/

        if(!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_SWEAR_BYPASS)) {
            if (this.swearFilter(message)) {
                event.setCancelled(true);
                player.sendMessage(ConfigHolder.SWEAR_WARNING_MESSAGE);

                this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " said a swear " +
                        "word: " + message, player.getUniqueId());

                return;
            }

        }

        /***** CAPS *****/

        if(!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_CAPS_BYPASS)) {
            int textLength = this.getTextLength(message);

            if (textLength >= ConfigHolder.ANTI_CAPS_MIN_LENGTH && getCapsPercentage(message) > ConfigHolder.ANTI_CAPS_PERCENTAGE && (!allFeatures || !allBypasses || !player.hasPermission(ConfigHolder.ANTI_CAPS_BYPASS))) {
                player.sendMessage(ConfigHolder.CAPS_WARNING_MESSAGE);
                event.setCancelled(true);

                this.chatPlugin.getLoggingService().addLog(LoggingType.WARNING, player.getName() + " sent too many caps letters: " + message, player.getUniqueId());
            }
        }
    }

    private int getTextLength(String message) {
        int result = 0;

        for(int i = 0; i < message.length(); i++) {
            char temp = message.charAt(i);

            if ((temp >= 'a' && temp <= 'z') || (temp >= 'A' && temp <= 'Z'))
                result++;
        }

        return result;
    }

    /**
     * Mentions players that are added in the message.
     *
     * @param message message to send
     * @param mentionedPlayers all players mentioned in message
     */

    private void mentionPlayer(String message, List<String> mentionedPlayers, String chat) {
        List<UUID> alreadyMentioned = new ArrayList<>();

        for(String mentionedPlayerName : mentionedPlayers) {
            Player mentionedPlayer = Bukkit.getPlayer(mentionedPlayerName);

            if(mentionedPlayer == null && !mentionedPlayer.isOnline())
                continue;

            String[] words = message.split(" ");

            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                if(word.startsWith("@")) {
                    if(mentionedPlayers.stream().anyMatch(word.substring(1)::equalsIgnoreCase))
                        word = word.replace(word, "§b" + word + "§7");
                }

                words[i] = word;
            }

            mentionedPlayer.sendMessage(" ");
            mentionedPlayer.sendMessage(chat + String.join(" ", words));
            mentionedPlayer.sendMessage(" ");

            mentionedPlayer.playSound(mentionedPlayer, Sound.valueOf(ConfigHolder.MENTIONS_SOUND_NAME), 1, 1);

            alreadyMentioned.add(mentionedPlayer.getUniqueId());
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!alreadyMentioned.contains(onlinePlayer.getUniqueId()))
                onlinePlayer.sendMessage(chat + message.replace(" ", ""));
        }
    }

    /**
     * Format color codes '&' and '0x' compatible
     *
     * @param message message to format
     * @return new message with correct colors
     */

    private String formatColors(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");

            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Checks if string contains unicode
     *
     * @param message string to check
     * @return boolean whether it is a unicode or not
     */

    private boolean isUnicode(String message) {
        for(int i = 0; i < message.length(); i++) {
            if(Character.UnicodeBlock.of(message.charAt(i)) != Character.UnicodeBlock.BASIC_LATIN)
                return true;
        }

        return false;
    }

    /**
     * Get percentages of how many letters are upper case
     *
     * @param string message to check
     * @return percentage as double
     */

    private double getCapsPercentage(String string) {
        int uppers = 0;

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (Character.isUpperCase(c))
                uppers++;
        }

        return (uppers * 100.0) / string.length();
    }

    private boolean swearFilter(String message) {
        String filteredMessage = message.replaceAll("[^a-zA-Z0-9]", "").replaceAll("1", "i").replaceAll("2", "z").replaceAll("3", "e").replaceAll("4", "h").replaceAll("5", "s").replaceAll("6", "g").replaceAll("7", "t").replaceAll("8", "p").replaceAll("9", "j").replaceAll("0", "o").toLowerCase();

        for (String swearWords : LumaniaChatPlugin.SWEAR_WORDS) {
            if (filteredMessage.contains(swearWords))
                return true;
        }

        return false;
    }
}

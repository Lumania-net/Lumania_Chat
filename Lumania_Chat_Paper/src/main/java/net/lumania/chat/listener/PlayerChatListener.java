package net.lumania.chat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import net.lumania.chat.utils.PermissionHolder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
        long currentTimeMillis = System.currentTimeMillis();
        boolean unicodeMessage = false;

        Player player = event.getPlayer();

        boolean allFeatures = player.hasPermission(PermissionHolder.ALL_FEATURES);
        boolean allBypasses = player.hasPermission(PermissionHolder.ALL_BYPASSES);

        TextComponent textComponent = (TextComponent) event.message();
        String message = textComponent.content();

        /***** UNICODE *****/

        if(this.isUnicode(message) && (!allFeatures || !allBypasses || !player.hasPermission(PermissionHolder.ANTI_UNICODE_BYPASS))) {
            event.setCancelled(true);
            this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " sent a unicode");

            unicodeMessage = true;
        }

        /***** SPY *****/

        if(LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId())) {
            for(UUID spies : LumaniaChatPlugin.SPY_CACHE.keySet()) {
                if(LumaniaChatPlugin.SPY_CACHE.get(spies).equals(player.getUniqueId())) {
                    Player spyPlayer = Bukkit.getPlayer(spies);

                    if(spyPlayer != null && spyPlayer.isOnline()) {
                        message = this.formatColors(message);

                        if(unicodeMessage) {
                            spyPlayer.sendMessage(LumaniaChatPlugin.PREFIX + "§e§l" + player.getName() + "§7 hat eine Unicode Nachricht gesendet§8.");

                            return;
                        }
                        else
                            spyPlayer.sendMessage(LumaniaChatPlugin.PREFIX + "§e§l" + player.getName() + "§8 -> §7" + message);
                    }
                }
            }
        }

        if(unicodeMessage)
            return;

        /***** MUTED *****/

        if(LumaniaChatPlugin.MUTED && (!allFeatures || !allBypasses || !player.hasPermission(PermissionHolder.MUTE_CHAT_BYPASS))) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Chat ist derzeit gemuted§8!");
            event.setCancelled(true);
        }

        // ADS -> SWEAR -> CAPS -> SPAM -> MENTION

        /***** FORMAT COLORS *****/

        message = this.formatColors(message);

        /***** CAPS *****/

        if(getCapsPercentage(message) > 50 && (!allFeatures || !allBypasses || !player.hasPermission(PermissionHolder.ANTI_CAPS_BYPASS))) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Nicht so viele Großbuchstaben§8!");
            event.setCancelled(true);

            return;
        }

        /***** SPAM *****/

        if(!allFeatures || !allBypasses || !player.hasPermission(PermissionHolder.ANTI_SPAM_BYPASS)) {
            if (LumaniaChatPlugin.SPAM_CACHE.containsKey(player.getUniqueId())) {
                long lastMessage = LumaniaChatPlugin.SPAM_CACHE.get(player.getUniqueId());

                if (lastMessage + 1000L > currentTimeMillis) {
                    event.setCancelled(true);
                    player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Warte vor deiner nächsten Nachricht§8.");

                    return;
                }
            }

            LumaniaChatPlugin.SPAM_CACHE.put(player.getUniqueId(), currentTimeMillis);
        }

        event.message(Component.text(message));

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
            this.mentionPlayer(message, mentionedPlayers);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerCommandListener(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();

        if(!LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId()))
            return;

        for(UUID spy : LumaniaChatPlugin.SPY_CACHE.keySet()) {
            if(LumaniaChatPlugin.SPY_CACHE.get(spy).equals(player.getUniqueId())) {
                Player spyPlayer = Bukkit.getPlayer(LumaniaChatPlugin.SPY_CACHE.get(spy));

                if(spyPlayer == null || !spyPlayer.isOnline()) {
                    LumaniaChatPlugin.SPY_CACHE.remove(spy);
                    return;
                }

                spyPlayer.sendMessage(LumaniaChatPlugin.PREFIX + "§e§l" + player.getName() + "§8 -> §7" + message);
            }
        }
    }

    /**
     * Mentions players that are added in the message.
     *
     * @param message message to send
     * @param mentionedPlayers all players mentioned in message
     */

    private void mentionPlayer(String message, List<String> mentionedPlayers) {
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
                        word = word.replace(word, "§b§l" + word + "§f");
                }

                words[i] = word;
            }

            mentionedPlayer.sendMessage(" ");
            mentionedPlayer.sendMessage(String.join(" ", words));
            mentionedPlayer.sendMessage(" ");

            mentionedPlayer.playSound(mentionedPlayer, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            alreadyMentioned.add(mentionedPlayer.getUniqueId());
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!alreadyMentioned.contains(onlinePlayer.getUniqueId()))
                onlinePlayer.sendMessage(message);
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
     * Checks if string contains unicode (char > 128)
     *
     * @param message string to check
     * @return boolean whether it is a unicode or not
     */

    private boolean isUnicode(String message) {
        for(int i = 0; i < message.length(); i++) {
            int c = message.charAt(i);

            if(c > 128)
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
}

package net.lumania.chat.listeners;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.ConfigHolder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
    public void playerChatListener(ChatEvent event) {
        if(!(event.getSender() instanceof ProxiedPlayer player))
            return;

        if(!LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId()))
            return;

        for(UUID spies : LumaniaChatPlugin.SPY_CACHE.keySet()) {
            if(!LumaniaChatPlugin.SPY_CACHE.get(spies).equals(player.getUniqueId()))
                return;

            ProxiedPlayer spyPlayer = ProxyServer.getInstance().getPlayer(spies);

            if(spyPlayer == null || !spyPlayer.isConnected()) {
                LumaniaChatPlugin.SPY_CACHE.remove(spies);
                return;
            }

            String message = this.formatColors(event.getMessage());

            if(this.isUnicode(message) && (!player.hasPermission(ConfigHolder.ALL_BYPASSES_PERMISSION) || !player.hasPermission(ConfigHolder.ALL_FEATURES_PERMISSION) || !player.hasPermission(ConfigHolder.ANTI_UNICODE_BYPASS))) {
                spyPlayer.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§b" + player.getServer().getInfo().getName() + "§8: §f" + ConfigHolder.UNICODE_WARNING_MESSAGE.replaceAll("%target%", player.getName())));
                return;
            }

            spyPlayer.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§b" + player.getServer().getInfo().getName() + " §8| §e§l" + player.getName() + "§8 -> §f" + message));
        }
    }

    private String formatColors(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");

            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private boolean isUnicode(String message) {
        for(int i = 0; i < message.length(); i++) {
            if(Character.UnicodeBlock.of(message.charAt(i)) != Character.UnicodeBlock.BASIC_LATIN)
                return true;
        }

        return false;
    }
}

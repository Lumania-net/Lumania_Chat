package net.lumania.chat.listeners;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void playerQuitListener(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        LumaniaChatPlugin.SPY_CACHE.remove(player.getUniqueId());

        if(LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId())) {
            for(UUID spies : LumaniaChatPlugin.SPY_CACHE.keySet()) {
                if(LumaniaChatPlugin.SPY_CACHE.get(spies).equals(player.getUniqueId())) {
                    LumaniaChatPlugin.SPY_CACHE.remove(spies);

                    ProxiedPlayer spyPlayer = ProxyServer.getInstance().getPlayer(spies);

                    if(spyPlayer == null || !spyPlayer.isConnected())
                        continue;

                    spyPlayer.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Der Spieler den du ausspioniert hast§8, §7ist Offline gegangen§8."));
                }
            }
        }

        LumaniaChatPlugin.MESSAGE_CACHE.remove(player.getUniqueId());

        if(LumaniaChatPlugin.MESSAGE_CACHE.containsValue(player.getUniqueId())) {
            for(UUID message : LumaniaChatPlugin.MESSAGE_CACHE.keySet()) {
                if(LumaniaChatPlugin.MESSAGE_CACHE.get(message).equals(player.getUniqueId()))
                    LumaniaChatPlugin.MESSAGE_CACHE.remove(message);
            }
        }
    }
}

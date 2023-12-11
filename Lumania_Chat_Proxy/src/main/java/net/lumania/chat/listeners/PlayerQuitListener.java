package net.lumania.chat.listeners;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.ConfigHolder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerQuitListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public PlayerQuitListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

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

                    spyPlayer.sendMessage(new TextComponent(ConfigHolder.SPY_SPYING_PLAYER_WENT_OFFLINE_MESSAGE.replaceAll("%target%", player.getName())));
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

        boolean messageToggle = LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.get(player.getUniqueId());

        LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.remove(player.getUniqueId());

        CompletableFuture.runAsync(() -> {
            if(this.chatPlugin.getDatabaseService().playerInDatabase(player.getUniqueId()))
                this.chatPlugin.getDatabaseService().updatePlayer(player.getUniqueId(), messageToggle);
            else
                this.chatPlugin.getDatabaseService().setupPlayer(player.getUniqueId(), messageToggle);
        });
    }
}

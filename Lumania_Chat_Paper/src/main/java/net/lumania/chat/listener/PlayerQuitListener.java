package net.lumania.chat.listener;

import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public PlayerQuitListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler
    public void playerQuitListener(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        LumaniaChatPlugin.SPY_CACHE.remove(player.getUniqueId());

        if(LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId())) {
            for(UUID spies : LumaniaChatPlugin.SPY_CACHE.keySet()) {
                if(LumaniaChatPlugin.SPY_CACHE.get(spies).equals(player.getUniqueId()))
                    LumaniaChatPlugin.SPY_CACHE.remove(spies);
            }
        }

        LumaniaChatPlugin.MESSAGE_CACHE.remove(player.getUniqueId());

        if(LumaniaChatPlugin.MESSAGE_CACHE.containsValue(player.getUniqueId())) {
            for(UUID message : LumaniaChatPlugin.MESSAGE_CACHE.keySet()) {
                if(LumaniaChatPlugin.MESSAGE_CACHE.get(message).equals(player.getUniqueId()))
                    LumaniaChatPlugin.MESSAGE_CACHE.remove(message);
            }
        }

        LumaniaChatPlugin.SPAM_CACHE.remove(player.getUniqueId());
    }
}

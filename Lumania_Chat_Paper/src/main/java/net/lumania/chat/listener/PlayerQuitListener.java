package net.lumania.chat.listener;

import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void playerQuitListener(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        LumaniaChatPlugin.SPAM_CACHE.remove(player.getUniqueId());
    }
}

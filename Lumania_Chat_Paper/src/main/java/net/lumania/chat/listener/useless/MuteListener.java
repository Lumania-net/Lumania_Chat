package net.lumania.chat.listener.useless;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MuteListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public MuteListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler
    public void playerChatListener(AsyncChatEvent event) {
        if(LumaniaChatPlugin.MUTED)
            return;

        Player player = event.getPlayer();

        if(player.hasPermission("lumania.chat.mute"))
            return;

        event.setCancelled(true);
    }
}

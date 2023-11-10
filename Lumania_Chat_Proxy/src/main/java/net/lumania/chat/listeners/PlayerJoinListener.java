package net.lumania.chat.listeners;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public PlayerJoinListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler
    public void playerJoinListener(LoginEvent event) {
        if(!(event.getConnection() instanceof ProxiedPlayer player))
            return;

        boolean messageToggle = true;

        if(this.chatPlugin.getDatabaseService().playerInDatabase(player.getUniqueId()))
            messageToggle = this.chatPlugin.getDatabaseService().getPlayerToggled(player.getUniqueId());

        LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.put(player.getUniqueId(), messageToggle);
    }
}

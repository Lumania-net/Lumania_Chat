package net.lumania.chat.listeners;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
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
        boolean messageToggle = true;

        if(this.chatPlugin.getDatabaseService().playerInDatabase(event.getConnection().getUniqueId()))
            messageToggle = this.chatPlugin.getDatabaseService().getPlayerToggled(event.getConnection().getUniqueId());

        LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.put(event.getConnection().getUniqueId(), messageToggle);
    }
}

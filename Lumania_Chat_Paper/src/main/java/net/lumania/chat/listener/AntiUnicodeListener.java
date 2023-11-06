package net.lumania.chat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AntiUnicodeListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public AntiUnicodeListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerChatListener(AsyncChatEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(player.hasPermission("lumania.chat.unicode"))
            return;

        TextComponent textComponent = (TextComponent) event.message();

        String message = textComponent.content();

        if(isUnicode(message)) {
            event.setCancelled(true);

            this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " sent a unicode message");
        }
    }

    private boolean isUnicode(String message) {
        for(int i = 0; i < message.length(); i++) {
            int c = message.charAt(i);

            if(c > 15000)
                return true;
        }

        return false;
    }
}

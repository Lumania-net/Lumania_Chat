package net.lumania.chat.listener.useless;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AntiSpamListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public AntiSpamListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler
    public void playerChatListener(AsyncChatEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(player.hasPermission("lumania.chat.spam"))
            return;

        if(LumaniaChatPlugin.SPAM_CACHE.containsKey(player.getUniqueId())) {
            long currentTime = System.currentTimeMillis();
            long lastMessage = LumaniaChatPlugin.SPAM_CACHE.get(player.getUniqueId());

            if(lastMessage + 1000 < currentTime) {
                LumaniaChatPlugin.SPAM_CACHE.put(player.getUniqueId(), currentTime);
                return;
            }

            event.setCancelled(true);
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Warte noch ein bisschen auf die nächste Nachricht§8.");

            this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " tried to spam");

            return;
        }

        LumaniaChatPlugin.SPAM_CACHE.put(player.getUniqueId(), System.currentTimeMillis());
    }
}

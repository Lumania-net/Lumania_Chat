package net.lumania.chat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AntiAdvertisementListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public AntiAdvertisementListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerChatListener(AsyncChatEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(player.hasPermission("lumania.chat.ads"))
            return;

        TextComponent textComponent = (TextComponent) event.message();

        String message = textComponent.content();

        if(this.chatPlugin.getAdvertisementService().advertising(player.getName(), message)) {
            event.setCancelled(true);
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du darfst keine Werbung senden§8!");
        }
    }
}

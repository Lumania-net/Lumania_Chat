package net.lumania.chat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AntiCapsListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public AntiCapsListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerChatListener(AsyncChatEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(player.hasPermission("lumania.chat.caps"))
            return;

        TextComponent textComponent = (TextComponent) event.message();

        String message = textComponent.content();

        if(getCapsPercentage(message) > 50) { // TODO: Check for length | Check for percentage
            event.setCancelled(true);
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Nicht so viele Großbuchstaben§8!");
        }

    }

    private double getCapsPercentage(String string) {
        int uppers = 0;

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (Character.isUpperCase(c))
                uppers++;
        }

        return (uppers * 100.0) / string.length();
    }
}

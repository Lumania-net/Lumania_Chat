package net.lumania.chat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColoredChatListener implements Listener {

    private LumaniaChatPlugin chatPlugin;

    public ColoredChatListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChatListener(AsyncChatEvent event) {
        if(event.isCancelled())
            return;

        TextComponent textComponent = (TextComponent) event.message();

        String message = this.formatColors(textComponent.content());

        event.message(Component.text(message));
    }

    private String formatColors(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");

            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

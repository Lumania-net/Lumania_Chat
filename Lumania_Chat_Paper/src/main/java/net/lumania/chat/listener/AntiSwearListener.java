package net.lumania.chat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class AntiSwearListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;
    private final List<String> swearWords;

    public AntiSwearListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
        this.swearWords = new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerChatListener(AsyncChatEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(player.hasPermission("lumania.chat.swear"))
            return;

        TextComponent textComponent = (TextComponent) event.message();

        String message = textComponent.content();
        String[] array = message.split(" ");

        for (String word : this.swearWords) {
            for (String cha : array) {
                StringBuilder stringBuilder = new StringBuilder();
                char[] charArray = cha.toCharArray();

                for (char c : charArray) {
                    if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
                        stringBuilder.append(c);
                }

                String filtered = stringBuilder.toString();

                if (filtered.equalsIgnoreCase(word))
                    event.setCancelled(true);
            }
        }

        if(event.isCancelled()) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Dieses Wort ist verboten§8.");
            this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, player.getName() + " sent a swear word: " + message);
        }
    }
}

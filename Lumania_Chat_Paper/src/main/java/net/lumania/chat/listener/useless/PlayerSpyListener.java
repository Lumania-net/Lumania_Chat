package net.lumania.chat.listener.useless;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class PlayerSpyListener implements Listener {

    private final LumaniaChatPlugin chatPlugin;

    public PlayerSpyListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler
    public void playerChatListener(AsyncChatEvent event) {
        Player player = event.getPlayer();

        TextComponent textComponent = (TextComponent) event.message();

        String message = textComponent.content();

        if(!LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId()))
            return;

        for(UUID spy : LumaniaChatPlugin.SPY_CACHE.keySet()) {
            if(LumaniaChatPlugin.SPY_CACHE.get(spy).equals(player.getUniqueId())) {
                Player spyPlayer = Bukkit.getPlayer(LumaniaChatPlugin.SPY_CACHE.get(spy));

                if(spyPlayer == null || !spyPlayer.isOnline()) {
                    LumaniaChatPlugin.SPY_CACHE.remove(spy);
                    return;
                }

                spyPlayer.sendMessage(LumaniaChatPlugin.PREFIX + "§e§l" + player.getName() + "§8 -> §7" + message);
            }
        }
    }

    @EventHandler
    public void playerCommandListener(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();

        if(!LumaniaChatPlugin.SPY_CACHE.containsValue(player.getUniqueId()))
            return;

        for(UUID spy : LumaniaChatPlugin.SPY_CACHE.keySet()) {
            if(LumaniaChatPlugin.SPY_CACHE.get(spy).equals(player.getUniqueId())) {
                Player spyPlayer = Bukkit.getPlayer(LumaniaChatPlugin.SPY_CACHE.get(spy));

                if(spyPlayer == null || !spyPlayer.isOnline()) {
                    LumaniaChatPlugin.SPY_CACHE.remove(spy);
                    return;
                }

                spyPlayer.sendMessage(LumaniaChatPlugin.PREFIX + "§e§l" + player.getName() + "§8 -> §7" + message);
            }
        }
    }
}

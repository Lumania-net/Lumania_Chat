package net.lumania.chat.listener.useless;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PlayerMentionListener implements Listener {

    private LumaniaChatPlugin chatPlugin;

    public PlayerMentionListener(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @EventHandler
    public void playerChatListener(AsyncChatEvent event) {
        Player player = event.getPlayer();

        TextComponent textComponent = (TextComponent) event.message();

        String message = textComponent.content();
        String[] words = message.split(" ");

        List<Player> mentionedPlayers = new ArrayList<>();

        for(String word : words) {
            if(word.startsWith("@")) {
                String playerName = word.substring(1);

                Player mentionedPlayer = Bukkit.getPlayer(playerName);

                if(mentionedPlayer == null || !mentionedPlayer.isOnline())
                    continue;

                mentionedPlayers.add(mentionedPlayer);
            }
        }

        if(!mentionedPlayers.isEmpty()) {
            this.mentionPlayer(message, mentionedPlayers);
            event.setCancelled(true);
        }
    }

    private void mentionPlayer(String message, List<Player> mentionedPlayers) {
        String[] words = message.split(" ");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if(word.startsWith("@"))
                word = word.replace(word, "§e§l" + word + "§f");

            words[i] = word;
        }

        for(Player mentionedPlayer : mentionedPlayers) {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getUniqueId().equals(mentionedPlayer.getUniqueId()))
                    continue;

                onlinePlayer.sendMessage(message);
            }

            mentionedPlayer.sendMessage(String.join(" ", words));
            mentionedPlayer.playSound(mentionedPlayer, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
    }
}

package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;
    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public MessageCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player))
            return false;

        if(command.getName().equalsIgnoreCase("r")) {
            if(strings.length == 0) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lr §8<§e§lmsg§8> - §7Antwort an den letzten Spieler§8, §7der dir eine Nachricht geschickt hat§8.");
                return false;
            }

            if(!LumaniaChatPlugin.MESSAGE_CACHE.containsKey(player.getUniqueId())) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du hast noch keine Nachricht erhalten§8.");
                return false;
            }

            Player sendPlayer = Bukkit.getPlayer(LumaniaChatPlugin.MESSAGE_CACHE.get(player.getUniqueId()));

            if(sendPlayer == null || !sendPlayer.isOnline()) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Spieler ist nicht mehr Online§8.");
                return false;
            }

            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < strings.length; i++)
                stringBuilder.append(strings[0]).append(" ");

            String message = this.formatColors(stringBuilder.toString());

            player.sendMessage("§e§lDu§8: " + message);
            sendPlayer.sendMessage("§e§l" + player.getName() + "§8: " + message);

            LumaniaChatPlugin.MESSAGE_CACHE.put(sendPlayer.getUniqueId(), player.getUniqueId());
        } else if(command.getName().equalsIgnoreCase("msg")) {
            if(strings.length < 1) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lmsg §8<§e§lplayer§8 | §e§ltoggle§8> <§e§lmsg§8> - §7Entweder Messages deaktivieren oder eine Nachricht an einen Spieler schicken");
                return false;
            }

            if(strings.length == 1) {
                if(!strings[0].equalsIgnoreCase("toggle")) {
                    player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lmsg §8<§e§lplayer§8 | §e§ltoggle§8> <§e§lmsg§8> - §7Entweder Nachrichten deaktivieren oder eine Nachricht an einen Spieler schicken");
                    return false;
                }

                // TODO: Toggle in database

                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du hast die Nachrichten erfolgreich deaktiviert/aktiviert§8.");
                return false;
            }

            String name = strings[0];

            Player sendPlayer = Bukkit.getPlayer(name);

            if(sendPlayer == null || !sendPlayer.isOnline()) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der genannte Spieler ist nicht Online§8.");
                return false;
            }

            if(player.getUniqueId().equals(sendPlayer.getUniqueId())) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du kannst dir nicht selber eine Nachricht senden§8.");
                return false;
            }

            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 1; i < strings.length; i++)
                stringBuilder.append(strings[0]).append(" ");

            String message = this.formatColors(stringBuilder.toString());

            player.sendMessage("§e§lDu§8: " + message);
            sendPlayer.sendMessage("§e§l" + player.getName() + "§8: " + message);

            LumaniaChatPlugin.MESSAGE_CACHE.put(sendPlayer.getUniqueId(), player.getUniqueId());
        }

        return false;
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

    private boolean isUnicode(String message) {
        for(int i = 0; i < message.length(); i++) {
            int c = message.charAt(i);

            if(c > 128)
                return true;
        }

        return false;
    }
}

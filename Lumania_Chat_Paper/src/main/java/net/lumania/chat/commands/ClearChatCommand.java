package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import net.lumania.chat.utils.PermissionHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClearChatCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;

    public ClearChatCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        if(!(sender instanceof Player player))
            return false;

        if(!player.hasPermission(PermissionHolder.ALL_FEATURES) || !player.hasPermission(PermissionHolder.ALL_COMMANDS) || !player.hasPermission(PermissionHolder.CLEAR_CHAT)) {
            player.sendMessage(LumaniaChatPlugin.NO_PERMISSIONS);
            return false;
        }

        if(strings.length == 0) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                if(!onlinePlayer.hasPermission(PermissionHolder.ALL_FEATURES) || !onlinePlayer.hasPermission(PermissionHolder.ALL_BYPASSES) || !onlinePlayer.hasPermission(PermissionHolder.CLEAR_CHAT_BYPASS))
                    onlinePlayer.sendMessage("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            });

            this.chatPlugin.getLoggingService().addLog(LoggingType.INFO, player.getName() + " cleared chat for all");
        } else if(strings.length == 1) {
            String name = strings[0];
            Player clearPlayer = Bukkit.getPlayer(name);

            if(clearPlayer == null || !clearPlayer.isOnline()) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der genannte Spieler ist nicht Online§8.");
                return false;
            }

            clearPlayer.sendMessage("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

            this.chatPlugin.getLoggingService().addLog(LoggingType.INFO, player.getName() + " cleared chat for player: " + name);
        } else {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lclearchat §8- §7Chat Clear für alle Spieler");
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lclearchat §8<§e§lname§8> - §7Chat Clear für bestimmten Spieler");
        }

        return false;
    }
}

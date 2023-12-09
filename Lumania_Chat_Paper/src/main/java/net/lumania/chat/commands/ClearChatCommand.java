package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import net.lumania.chat.utils.ConfigHolder;
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

        if(!player.hasPermission(ConfigHolder.ALL_FEATURES) || !player.hasPermission(ConfigHolder.ALL_COMMANDS) || !player.hasPermission(ConfigHolder.CLEAR_CHAT)) {
            player.sendMessage(LumaniaChatPlugin.NO_PERMISSIONS);
            return false;
        }

        if(strings.length == 0) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                if(!onlinePlayer.hasPermission(ConfigHolder.ALL_FEATURES) || !onlinePlayer.hasPermission(ConfigHolder.ALL_BYPASSES) || !onlinePlayer.hasPermission(ConfigHolder.CLEAR_CHAT_BYPASS))
                    onlinePlayer.sendMessage("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ConfigHolder.CLEAR_CHAT_CLEARED_MESSAGE.replaceAll("%player%", player.getName()) ,"");
            });

            this.chatPlugin.getLoggingService().addLog(LoggingType.INFO, player.getName() + " cleared chat for all",
                    player.getUniqueId());
        } else if(strings.length == 1) {
            String name = strings[0];
            Player clearPlayer = Bukkit.getPlayer(name);

            if(clearPlayer == null || !clearPlayer.isOnline()) {
                player.sendMessage(ConfigHolder.PLAYER_NOT_ONLINE_MESSAGE.replaceAll("%target%", clearPlayer.getName()));
                return false;
            }

            clearPlayer.sendMessage("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ConfigHolder.CLEAR_CHAT_CLEARED_MESSAGE.replaceAll("%player%", player.getName()) ,"");

            this.chatPlugin.getLoggingService().addLog(LoggingType.INFO, player.getName() + " cleared chat for " +
                    "player: " + name, player.getUniqueId());
        } else {
            if(ConfigHolder.CLEAR_CHAT_HELP_MESSAGE != null) {
                player.sendMessage(ConfigHolder.CLEAR_CHAT_HELP_MESSAGE);
            }
        }

        return false;
    }
}

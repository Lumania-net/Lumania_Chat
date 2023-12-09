package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.ConfigHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SaveLogCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;

    public SaveLogCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player) && sender instanceof ConsoleCommandSender) {
            try {
                this.chatPlugin.getLoggingService().saveLog();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission(ConfigHolder.ALL_FEATURES) || !player.hasPermission(ConfigHolder.ALL_COMMANDS) || !player.hasPermission(ConfigHolder.SAVE_LOGS)) {
            player.sendMessage(LumaniaChatPlugin.NO_PERMISSIONS);
            return false;
        }

        try {
            String fileName = this.chatPlugin.getLoggingService().saveLog();
            player.sendMessage(ConfigHolder.SAVE_LOG_SUCCESSFULLY_MESSAGE.replaceAll("%file%", fileName));
        } catch (IOException e) {
            this.chatPlugin.getLogger().severe("Error while trying to save logs to file");

            player.sendMessage(ConfigHolder.SAVE_LOG_FAILURE_MESSAGE);
        }

        return false;
    }
}

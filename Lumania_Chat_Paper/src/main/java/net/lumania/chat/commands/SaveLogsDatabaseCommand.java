package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.ConfigHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaveLogsDatabaseCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;

    public SaveLogsDatabaseCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof ConsoleCommandSender)) {
            if(sender instanceof Player && sender.hasPermission(ConfigHolder.ALL_FEATURES))
                sender.sendMessage(LumaniaChatPlugin.PREFIX + "§7Nur über Konsole möglich§8!");

            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("savelogsdatabase <filename> <filename> <filename> ......");
            return false;
        }

        this.chatPlugin.getLoggingService().addLogToDatabase(strings);

        sender.sendMessage("Currently uploading logs to database...");

        return false;
    }
}

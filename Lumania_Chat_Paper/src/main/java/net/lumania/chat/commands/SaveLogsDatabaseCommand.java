package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class SaveLogsDatabaseCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;

    public SaveLogsDatabaseCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof ConsoleCommandSender))
            return false;

        if(strings.length == 0) {
            sender.sendMessage("savelogsdatabase <filename> <filename> <filename> ......");
            return false;
        }

        this.chatPlugin.getLoggingService().addLogToDatabase(strings);

        sender.sendMessage("Currently uploading logs to database...");

        return false;
    }
}

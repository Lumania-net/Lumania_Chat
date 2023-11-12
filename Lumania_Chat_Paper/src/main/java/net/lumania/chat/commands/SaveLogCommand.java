package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.PermissionHolder;
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

        if(!player.hasPermission(PermissionHolder.ALL_FEATURES) || !player.hasPermission(PermissionHolder.ALL_COMMANDS) || !player.hasPermission(PermissionHolder.SAVE_LOGS)) {
            player.sendMessage(LumaniaChatPlugin.NO_PERMISSIONS);
            return false;
        }

        try {
            this.chatPlugin.getLoggingService().saveLog();
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Log wurde erfolgreich gespeichert§8.");
        } catch (IOException e) {
            this.chatPlugin.getLogger().severe("Error while trying to save logs to file");

            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Log konnte nicht gespeichert werden§8! §7Überprüfe die Konsole§8.");
        }

        return false;
    }
}

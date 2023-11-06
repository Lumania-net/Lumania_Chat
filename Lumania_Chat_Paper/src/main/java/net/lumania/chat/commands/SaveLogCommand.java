package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        if(!(sender instanceof Player player)) {
            try {
                this.chatPlugin.getLoggingService().saveLog();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        if(player.hasPermission("lumania.chat.logs")) {
            try {
                this.chatPlugin.getLoggingService().saveLog();
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Log wurde erfolgreich gespeichert§8.");
            } catch (IOException e) {
                e.printStackTrace();

                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Log konnte nicht gespeichert werden§8! §7Überprüfe den Log§8.");
            }

            return false;
        }

        player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du hast nicht genügend Rechte§8.");

        return false;
    }
}

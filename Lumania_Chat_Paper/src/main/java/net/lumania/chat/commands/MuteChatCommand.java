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

public class MuteChatCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;

    public MuteChatCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        if(!(sender instanceof Player player))
            return false;

        if(!player.hasPermission(PermissionHolder.ALL_FEATURES) || !player.hasPermission(PermissionHolder.ALL_COMMANDS) || !player.hasPermission(PermissionHolder.MUTE_CHAT)) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du hast nicht genügend Rechte§8.");
            return false;
        }

        if(strings.length == 0) {
            LumaniaChatPlugin.MUTED = !LumaniaChatPlugin.MUTED;
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der Chat ist nun für alle Spieler " + (LumaniaChatPlugin.MUTED ? "gemuted" : "freigegeben") +"§8.");

            this.chatPlugin.getLoggingService().addLog(LoggingType.INFO, player.getName() + (LumaniaChatPlugin.MUTED ? "muted" : "unmuted") + " the chat");
        } else {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lmutechat §8- §7Mutet den Chat für alle Spieler ohne genügend Rechte§8.");
        }

        return false;
    }
}

package net.lumania.chat.commands;

import net.kyori.adventure.text.Component;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import net.lumania.chat.utils.ConfigHolder;
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

        if(!player.hasPermission(ConfigHolder.ALL_FEATURES) || !player.hasPermission(ConfigHolder.ALL_COMMANDS) || !player.hasPermission(ConfigHolder.MUTE_CHAT)) {
            player.sendMessage(LumaniaChatPlugin.NO_PERMISSIONS);
            return false;
        }

        if(strings.length == 0) {
            LumaniaChatPlugin.MUTED = !LumaniaChatPlugin.MUTED;

            this.chatPlugin.getLoggingService().addLog(LoggingType.INFO, player.getName() + (LumaniaChatPlugin.MUTED
                    ? " muted" : " unmuted") + " the chat", player.getUniqueId());

            Bukkit.broadcast(Component.text(" "));

            if(LumaniaChatPlugin.MUTED)
                Bukkit.broadcast(Component.text(ConfigHolder.MUTE_CHAT_BROADCAST_MESSAGE.replaceAll("%player%", player.getName())));
            else
                Bukkit.broadcast(Component.text(ConfigHolder.UNMUTE_CHAT_BROADCAST_MESSAGE.replaceAll("%player%", player.getName())));

            Bukkit.broadcast(Component.text("   "));
        } else {
            if(ConfigHolder.MUTE_CHAT_HELP_MESSAGE != null)
                player.sendMessage(ConfigHolder.MUTE_CHAT_HELP_MESSAGE);
        }

        return false;
    }
}

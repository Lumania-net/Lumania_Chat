package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.PermissionHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpyCommand implements CommandExecutor {

    private final LumaniaChatPlugin chatPlugin;

    public SpyCommand(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player))
            return false;

        if(!player.hasPermission(PermissionHolder.ALL_FEATURES) || !player.hasPermission(PermissionHolder.ALL_COMMANDS) || !player.hasPermission(PermissionHolder.SPY_PLAYER)) {
            player.sendMessage(LumaniaChatPlugin.NO_PERMISSIONS);
            return false;
        }

        if(strings.length != 1) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§8/§e§lspy §8<§e§lstop§8 | §e§lname§8> - §7Erhalte alle Chat-Nachrichten des Spielers");
            return false;
        }

        if(strings[0].equalsIgnoreCase("stop")) {
            if(!LumaniaChatPlugin.SPY_CACHE.containsKey(player.getUniqueId())) {
                player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du bist aktuell niemanden am spionieren§8.");
                return false;
            }

            LumaniaChatPlugin.SPY_CACHE.remove(player.getUniqueId());
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du hast erfolgreich die Spionage beendet§8.");

            return false;
        }

        if(LumaniaChatPlugin.SPY_CACHE.containsKey(player.getUniqueId())) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du bist bereits am spionieren§8.");
            return false;
        }

        String playerName = strings[0];

        Player spyPlayer = Bukkit.getPlayer(playerName);

        if(spyPlayer == null || !spyPlayer.isOnline()) {
            player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Der genannte Spieler ist nicht Online§8.");
            return false;
        }

        LumaniaChatPlugin.SPY_CACHE.put(player.getUniqueId(), spyPlayer.getUniqueId());
        player.sendMessage(LumaniaChatPlugin.PREFIX + "§7Du spionierst nun §e§l" + spyPlayer.getName() + "§8.");

        return false;
    }
}

package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SpyCommand extends Command {

    public SpyCommand() {
        super("spy");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if(!(sender instanceof ProxiedPlayer player))
            return;

        if(!player.hasPermission("")) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.NO_PERMISSIONS));
            return;
        }

        if(strings.length != 1) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§8/§e§lspy §8<§e§lstop§8 | §e§lname§8> - §7Erhalte alle Chat-Nachrichten des Spielers"));
            return;
        }

        if(strings[0].equalsIgnoreCase("stop")) {
            if(!LumaniaChatPlugin.SPY_CACHE.containsKey(player.getUniqueId())) {
                player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Du bist aktuell niemanden am spionieren§8."));
                return;
            }

            LumaniaChatPlugin.SPY_CACHE.remove(player.getUniqueId());
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Du hast erfolgreich die Spionage beendet§8."));

            return;
        }

        if(LumaniaChatPlugin.SPY_CACHE.containsKey(player.getUniqueId())) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Du bist bereits am spionieren§8."));
            return;
        }

        String playerName = strings[0];

        ProxiedPlayer spyPlayer = ProxyServer.getInstance().getPlayer(playerName);

        if(spyPlayer == null || !spyPlayer.isConnected()) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Der genannte Spieler ist nicht Online§8."));
            return;
        }

        LumaniaChatPlugin.SPY_CACHE.put(player.getUniqueId(), spyPlayer.getUniqueId());
        player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Du spionierst nun §e§l" + spyPlayer.getName() + "§8."));
    }
}

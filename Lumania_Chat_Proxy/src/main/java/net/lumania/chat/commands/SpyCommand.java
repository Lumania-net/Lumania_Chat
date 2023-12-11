package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.ConfigHolder;
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

        if(!player.hasPermission(ConfigHolder.ALL_FEATURES_PERMISSION) || !player.hasPermission(ConfigHolder.ALL_COMMANDS_PERMISSION) || !player.hasPermission(ConfigHolder.SPY_PERMISSION)) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.NO_PERMISSIONS));
            return;
        }

        if(strings.length != 1) {
            if(ConfigHolder.SPY_HELP_MESSAGE != null)
                player.sendMessage(new TextComponent(ConfigHolder.SPY_HELP_MESSAGE));

            return;
        }

        if(strings[0].equalsIgnoreCase("stop")) {
            if(!LumaniaChatPlugin.SPY_CACHE.containsKey(player.getUniqueId())) {
                player.sendMessage(new TextComponent(ConfigHolder.SPY_NO_SPYING_RIGHT_NOW_MESSAGE));
                return;
            }

            LumaniaChatPlugin.SPY_CACHE.remove(player.getUniqueId());
            player.sendMessage(new TextComponent(ConfigHolder.SPY_ENDED_SPYING_MESSAGE));

            return;
        }

        if(LumaniaChatPlugin.SPY_CACHE.containsKey(player.getUniqueId())) {
            player.sendMessage(new TextComponent(ConfigHolder.SPY_ALREADY_SPYING_MESSAGE));
            return;
        }

        String playerName = strings[0];

        ProxiedPlayer spyPlayer = ProxyServer.getInstance().getPlayer(playerName);

        if(spyPlayer == null || !spyPlayer.isConnected()) {
            player.sendMessage(new TextComponent(ConfigHolder.SPY_PLAYER_NOT_ONLINE_MESSAGE.replaceAll("%target%", playerName)));
            return;
        }

        LumaniaChatPlugin.SPY_CACHE.put(player.getUniqueId(), spyPlayer.getUniqueId());
        player.sendMessage(new TextComponent(ConfigHolder.SPY_START_SPYING_MESSAGE.replaceAll("%target%", playerName)));
    }
}

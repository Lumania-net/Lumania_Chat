package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.ConfigHolder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplyCommand extends Command {

    public ReplyCommand() {
        super("reply", "", "r");
    }

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if(!(sender instanceof ProxiedPlayer player))
            return;

        if(strings.length == 0) {
            if(ConfigHolder.REPLY_HELP_MESSAGE != null)
                player.sendMessage(new TextComponent(ConfigHolder.REPLY_HELP_MESSAGE));

            return;
        }

        if(!LumaniaChatPlugin.MESSAGE_CACHE.containsKey(player.getUniqueId())) {
            player.sendMessage(new TextComponent(ConfigHolder.REPLY_DONT_HAVE_OPEN_CONVERSATION_MESSAGE));
            return;
        }

        ProxiedPlayer sendPlayer = ProxyServer.getInstance().getPlayer(LumaniaChatPlugin.MESSAGE_CACHE.get(player.getUniqueId()));

        if(sendPlayer == null || !sendPlayer.isConnected()) {
            player.sendMessage(new TextComponent(ConfigHolder.REPLY_PLAYER_NOT_ONLINE_MESSAGE));
            LumaniaChatPlugin.MESSAGE_CACHE.remove(player.getUniqueId());

            return;
        }

        if(!LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.get(sendPlayer.getUniqueId())) {
            player.sendMessage(new TextComponent(ConfigHolder.MESSAGE_PLAYER_DOES_NOT_WANT_MESSAGES_MESSAGE.replaceAll("%target%", sendPlayer.getName())));
            return;
        }

        String message = this.formatColors(String.join(" ", strings));

        player.sendMessage(new TextComponent("§e§lDu§8: §f" + message));
        sendPlayer.sendMessage(new TextComponent("§e§l" + player.getName() + "§8: §f"  + message));

        LumaniaChatPlugin.MESSAGE_CACHE.put(sendPlayer.getUniqueId(), player.getUniqueId());
    }

    private String formatColors(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");

            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

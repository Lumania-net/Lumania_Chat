package net.lumania.chat.commands;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.PermissionHolder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageCommand extends Command {

    public MessageCommand() {
        super("msg");
    }

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if(!(sender instanceof ProxiedPlayer player))
            return;

        if(strings.length < 1) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§8/§e§lmsg §8<§e§lplayer§8 | §e§ltoggle§8> <§e§lmsg§8> - §7Entweder Messages deaktivieren oder eine Nachricht an einen anderen Spieler schicken"));
            return;
        }

        if(strings.length == 1) {
            if(strings[0].equalsIgnoreCase("toggle")) {
                boolean activated = LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.get(player.getUniqueId());
                LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.put(player.getUniqueId(), !activated);

                player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Du hast die Nachrichten erfolgreich " + (!activated ? "aktiviert" : "deaktviert") + "§8."));
            } else
                player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§8/§e§lmsg §8<§e§lplayer§8 | §e§ltoggle§8> <§e§lmsg§8> - §7Entweder Messages deaktivieren oder eine Nachricht an einen anderen Spieler schicken"));

            return;
        }

        String name = strings[0];

        if(name.equalsIgnoreCase(player.getName())) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Du kannst dir nicht selber eine Nachricht senden§8."));
            return;
        }

        ProxiedPlayer sendPlayer = ProxyServer.getInstance().getPlayer(name);

        if(sendPlayer == null || !sendPlayer.isConnected()) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Der gennante Spieler ist nicht Online§8."));
            return;
        }

        if(!LumaniaChatPlugin.MESSAGE_TOGGLE_CACHE.get(sendPlayer.getUniqueId())) {
            player.sendMessage(new TextComponent(LumaniaChatPlugin.PREFIX + "§7Dieser Spieler möchte keine Nachrichten erhalten§8."));
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

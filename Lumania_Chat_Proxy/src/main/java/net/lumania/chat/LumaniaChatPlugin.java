package net.lumania.chat;

import net.lumania.chat.commands.MessageCommand;
import net.lumania.chat.commands.ReplyCommand;
import net.lumania.chat.commands.SpyCommand;
import net.lumania.chat.listeners.PlayerChatListener;
import net.lumania.chat.listeners.PlayerQuitListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LumaniaChatPlugin extends Plugin {

    public static final String PREFIX = "";

    public static final Map<UUID, UUID> MESSAGE_CACHE = new HashMap<>();
    public static final Map<UUID, UUID> SPY_CACHE = new HashMap<>();

    private LumaniaChatPlugin instance;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerChatListener(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerQuitListener());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SpyCommand());
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDisable() {
        SPY_CACHE.clear();
        MESSAGE_CACHE.clear();
    }

    public LumaniaChatPlugin getInstance() {
        return instance;
    }
}

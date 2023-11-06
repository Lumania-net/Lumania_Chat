package net.lumania.chat;

import net.lumania.chat.commands.*;
import net.lumania.chat.listener.*;
import net.lumania.chat.logger.LoggingService;
import net.lumania.chat.utils.AdvertisementService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class LumaniaChatPlugin extends JavaPlugin {

    public static final String PREFIX = "§b§lLUMANIA §8•§7 ";

    public static final Map<UUID, Long> SPAM_CACHE = new HashMap<>();
    public static final Map<UUID, UUID> MESSAGE_CACHE = new HashMap<>();
    public static final Map<UUID, UUID> SPY_CACHE = new HashMap<>();

    public static boolean MUTED = false;

    private LumaniaChatPlugin instance;

    private LoggingService loggingService;
    private AdvertisementService advertisementService;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new AntiAdvertisementListener(this), this);
        pluginManager.registerEvents(new AntiCapsListener(this), this);
        pluginManager.registerEvents(new AntiSpamListener(this), this);
        pluginManager.registerEvents(new AntiSwearListener(this), this);
        pluginManager.registerEvents(new AntiUnicodeListener(this), this);
        pluginManager.registerEvents(new ColoredChatListener(this), this);
        pluginManager.registerEvents(new MuteListener(this), this);
        pluginManager.registerEvents(new PlayerMentionListener(this), this);
        pluginManager.registerEvents(new PlayerSpyListener(this), this);

        this.getCommand("clearchat").setExecutor(new ClearChatCommand(this));
        this.getCommand("msg").setExecutor(new MessageCommand(this));
        this.getCommand("r").setExecutor(new MessageCommand(this));
        this.getCommand("mutechat").setExecutor(new MuteChatCommand(this));
        this.getCommand("savelog").setExecutor(new SaveLogCommand(this));
        this.getCommand("spy").setExecutor(new SpyCommand(this));

        loggingService = new LoggingService(this);
        advertisementService = new AdvertisementService(this);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public LumaniaChatPlugin getInstance() {
        return instance;
    }

    public LoggingService getLoggingService() {
        return loggingService;
    }

    public AdvertisementService getAdvertisementService() {
        return advertisementService;
    }
}

package net.lumania.chat;

import net.luckperms.api.LuckPerms;
import net.lumania.chat.commands.*;
import net.lumania.chat.listener.PlayerChatListener;
import net.lumania.chat.listener.PlayerQuitListener;
import net.lumania.chat.logger.LoggingService;
import net.lumania.chat.utils.AdvertisementService;
import net.lumania.chat.utils.PermissionHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;

public class LumaniaChatPlugin extends JavaPlugin {

    public static String PREFIX = "§b§lLUMANIA §8•§7 ";
    public static String NO_PERMISSIONS;

    public static final Map<UUID, Long> SPAM_CACHE = new HashMap<>();
    public static final Map<UUID, UUID> MESSAGE_CACHE = new HashMap<>();
    public static final Map<UUID, UUID> SPY_CACHE = new HashMap<>();

    public static boolean MUTED;

    private LumaniaChatPlugin instance;

    private LoggingService loggingService;
    private AdvertisementService advertisementService;

    private LuckPerms luckPermsApi;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if(!this.getDataFolder().exists())
            this.getDataFolder().mkdirs();

        this.saveDefaultConfig();
        this.loadConfigValues();

        pluginManager.registerEvents(new PlayerChatListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);

        this.getCommand("clearchat").setExecutor(new ClearChatCommand(this));
        this.getCommand("mutechat").setExecutor(new MuteChatCommand(this));
        this.getCommand("savelog").setExecutor(new SaveLogCommand(this));

        loggingService = new LoggingService(this);
        advertisementService = new AdvertisementService(this);

        RegisteredServiceProvider<LuckPerms> registeredServiceProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        if(registeredServiceProvider != null)
            this.luckPermsApi = registeredServiceProvider.getProvider();

        this.getLogger().info("LumaniaChat loaded successfully");
    }

    @Override
    public void onLoad() {
        instance = this;
        MUTED = false;
    }

    @Override
    public void onDisable() {
        SPY_CACHE.clear();
        SPAM_CACHE.clear();
        MESSAGE_CACHE.clear();

        try {
            this.loggingService.saveLog();
        } catch (IOException e) {
            this.getLogger().severe("Failed while saving log file");
        }
    }

    private void loadConfigValues() {
        String configPrefix = this.getConfig().getString("prefix").replaceAll("&", "§");
        PREFIX = configPrefix.endsWith(" ") ? configPrefix : configPrefix + " ";

        String noPermissionsMessage = this.getConfig().getString("permissions.noPermissionsMessage").replaceAll("&", "§");
        NO_PERMISSIONS = PREFIX + noPermissionsMessage;

        PermissionHolder.load(this.getConfig());
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

    public LuckPerms getLuckPermsApi() {
        return luckPermsApi;
    }
}

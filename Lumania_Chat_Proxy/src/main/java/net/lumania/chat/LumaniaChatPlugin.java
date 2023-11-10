package net.lumania.chat;

import net.lumania.chat.commands.MessageCommand;
import net.lumania.chat.commands.ReplyCommand;
import net.lumania.chat.commands.SpyCommand;
import net.lumania.chat.database.DatabaseService;
import net.lumania.chat.listeners.PlayerChatListener;
import net.lumania.chat.listeners.PlayerJoinListener;
import net.lumania.chat.listeners.PlayerQuitListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LumaniaChatPlugin extends Plugin {

    public static String PREFIX = "§b§lLUMANIA §8•§7 ";
    public static String NO_PERMISSIONS = "";

    public static final Map<UUID, UUID> MESSAGE_CACHE = new HashMap<>();
    public static final Map<UUID, UUID> SPY_CACHE = new HashMap<>();

    public static final Map<UUID, Boolean> MESSAGE_TOGGLE_CACHE = new HashMap<>();

    private Configuration chatConfiguration;

    private LumaniaChatPlugin instance;
    private DatabaseService databaseService;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerChatListener(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerQuitListener(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerJoinListener(this));

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SpyCommand());

        try {
            this.chatConfiguration = this.defaultConfig();
        } catch (Exception e) {
            this.getLogger().severe("Error while trying to add config file");
        }

        String configPrefix = this.chatConfiguration.getString("prefix").replaceAll("&", "§");
        PREFIX = configPrefix.endsWith(" ") ? configPrefix : configPrefix + " ";

        String noPermissionsMessage = this.chatConfiguration.getString("permissions.noPermissionsMessage").replaceAll("&", "§");
        NO_PERMISSIONS = PREFIX + noPermissionsMessage;

        this.databaseService = new DatabaseService(this, this.chatConfiguration.getString("database.host"), this.chatConfiguration.getString("database.username"), this.chatConfiguration.getString("database.password"), this.chatConfiguration.getString("database.database"), this.chatConfiguration.getInt("database.port"));
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDisable() {
        SPY_CACHE.clear();
        MESSAGE_CACHE.clear();
        MESSAGE_TOGGLE_CACHE.clear();

        try {
            this.getDatabaseService().getDataSource().getConnection().close();
        } catch (SQLException e) {
            this.getLogger().severe("Error while trying to close database connection");
        }
    }

    private Configuration defaultConfig() throws Exception {
        if(!this.getDataFolder().exists())
            this.getDataFolder().mkdirs();

        File configFile = new File(this.getDataFolder(), "config.yml");

        if(!configFile.exists()) {
            InputStream inputStream = this.getResourceAsStream("config.yml");
            Files.copy(inputStream, configFile.toPath());
        }

        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public LumaniaChatPlugin getInstance() {
        return instance;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }
}

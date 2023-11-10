package net.lumania.chat.utils;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.config.Configuration;

public class PermissionHolder {

    private final LumaniaChatPlugin chatPlugin;

    public PermissionHolder(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    public static String DATABASE_HOST;
    public static String DATABASE_DATABASE;
    public static String DATABASE_USERNAME;
    public static String DATABASE_PASSWORD;
    public static int DATABASE_PORT;

    public static String ALL_FEATURES_PERMISSION;
    public static String ALL_COMMANDS_PERMISSION;
    public static String ALL_BYPASSES_PERMISSION;

    public static String SPY_PERMISSION;

    public static String ANTI_CAPS_BYPASS;
    public static String ANTI_SPAM_BYPASS;

    public static double ANTI_SPAM_COUNTDOWN;

    public static double ANTI_CAPS_PERCENTAGE;
    public static int ANTI_CAPS_MIN_LENGTH;

    public void loadConfig(Configuration configuration) {
        DATABASE_HOST = configuration.getString("database.host");
        DATABASE_DATABASE = configuration.getString("database.database");
        DATABASE_USERNAME = configuration.getString("database.username");
        DATABASE_PASSWORD = configuration.getString("database.password");
        DATABASE_PORT = configuration.getInt("database.port");

        ALL_FEATURES_PERMISSION = configuration.getString("permissions.allFeatures");
        ALL_COMMANDS_PERMISSION = configuration.getString("permissions.allCommands");
        ALL_BYPASSES_PERMISSION = configuration.getString("permissions.allBypasses");

        SPY_PERMISSION = configuration.getString("permissions.spyPlayer");

        ANTI_CAPS_BYPASS = configuration.getString("permissions.bypasses.antiCapsBypass");
        ANTI_SPAM_BYPASS = configuration.getString("permissions.bypasses.antiSpamBypass");

        ANTI_SPAM_COUNTDOWN = configuration.getDouble("antiSpam.countdown");

        ANTI_CAPS_PERCENTAGE = configuration.getDouble("antiCaps.capsPercentage");
        ANTI_CAPS_MIN_LENGTH = configuration.getInt("antiCaps.minTextLength");
    }
}

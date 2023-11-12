package net.lumania.chat.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class PermissionHolder {

    public static String DATABASE_HOST;
    public static String DATABASE_DATABASE;
    public static String DATABASE_USERNAME;
    public static String DATABASE_PASSWORD;
    public static int DATABASE_PORT;

    public static String ALL_BYPASSES;
    public static String ALL_COMMANDS;
    public static String ALL_FEATURES;

    public static String CLEAR_CHAT;
    public static String MUTE_CHAT;
    public static String SPY_PLAYER;
    public static String SAVE_LOGS;

    public static String ANTI_ADS_BYPASS;
    public static String ANTI_SWEAR_BYPASS;
    public static String ANTI_SPAM_BYPASS;
    public static String ANTI_CAPS_BYPASS;
    public static String ANTI_UNICODE_BYPASS;
    public static String MUTE_CHAT_BYPASS;
    public static String CLEAR_CHAT_BYPASS;

    public static double ANTI_CAPS_PERCENTAGE;
    public static int ANTI_CAPS_MIN_LENGTH;

    public static double ANTI_SPAM_COUNTDOWN;

    public static void load(FileConfiguration fileConfiguration) {
        DATABASE_HOST = fileConfiguration.getString("database.host");
        DATABASE_DATABASE = fileConfiguration.getString("database.database");
        DATABASE_USERNAME = fileConfiguration.getString("database.username");
        DATABASE_PASSWORD = fileConfiguration.getString("database.password");
        DATABASE_PORT = fileConfiguration.getInt("database.port");

        ALL_BYPASSES = fileConfiguration.getString("permissions.allBypasses");
        ALL_COMMANDS = fileConfiguration.getString("permissions.allCommands");
        ALL_FEATURES = fileConfiguration.getString("permissions.allFeatures");

        CLEAR_CHAT = fileConfiguration.getString("permissions.clearChat");
        MUTE_CHAT = fileConfiguration.getString("permissions.muteChat");
        SPY_PLAYER = fileConfiguration.getString("permissions.spyPlayer");
        SAVE_LOGS = fileConfiguration.getString("permissions.saveLogs");

        ANTI_ADS_BYPASS = fileConfiguration.getString("permissions.bypasses.antiAdsBypass");
        ANTI_SWEAR_BYPASS = fileConfiguration.getString("permissions.bypasses.antiSwearBypass");
        ANTI_SPAM_BYPASS = fileConfiguration.getString("permissions.bypasses.antiSpamBypass");
        ANTI_CAPS_BYPASS = fileConfiguration.getString("permissions.bypasses.antiCapsBypass");
        ANTI_UNICODE_BYPASS = fileConfiguration.getString("permissions.bypasses.antiUnicodeBypass");
        MUTE_CHAT_BYPASS = fileConfiguration.getString("permissions.bypasses.muteChatBypass");
        CLEAR_CHAT_BYPASS = fileConfiguration.getString("permissions.bypasses.clearChatBypass");

        ANTI_CAPS_PERCENTAGE = fileConfiguration.getDouble("antiCaps.capsPercentageLimit");
        ANTI_CAPS_MIN_LENGTH = fileConfiguration.getInt("antiCaps.capsMinTextLength");

        ANTI_SPAM_COUNTDOWN = fileConfiguration.getDouble("antiSpam.countdown");
    }
}

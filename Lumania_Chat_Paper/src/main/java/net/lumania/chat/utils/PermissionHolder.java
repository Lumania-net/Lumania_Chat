package net.lumania.chat.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class PermissionHolder {

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

    public static void load(FileConfiguration fileConfiguration) {
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
    }
}

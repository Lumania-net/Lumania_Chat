package net.lumania.chat.utils;

import net.lumania.chat.LumaniaChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigHolder {

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


    public static String MENTIONS_SOUND_NAME;

    public static String SPAM_WARNING_MESSAGE;
    public static String UNICODE_WARNING_MESSAGE;
    public static String ADS_WARNING_MESSAGE;
    public static String SWEAR_WARNING_MESSAGE;
    public static String CAPS_WARNING_MESSAGE;

    public static String PLAYER_NOT_ONLINE_MESSAGE;

    public static String CLEAR_CHAT_CLEARED_MESSAGE;
    public static String CLEAR_CHAT_HELP_MESSAGE;

    public static String MUTE_CHAT_BROADCAST_MESSAGE;
    public static String UNMUTE_CHAT_BROADCAST_MESSAGE;
    public static String MUTE_CHAT_HELP_MESSAGE;
    public static String MUTE_CHAT_MUTED_MESSAGE;

    public static String SAVE_LOG_SUCCESSFULLY_MESSAGE;
    public static String SAVE_LOG_FAILURE_MESSAGE;

    public static String[] DOMAIN_ARRAY;

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


        MENTIONS_SOUND_NAME = fileConfiguration.getString("mentions.soundName").toUpperCase();

        SPAM_WARNING_MESSAGE = fileConfiguration.getString("messages.spamWarningMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        UNICODE_WARNING_MESSAGE = fileConfiguration.getString("messages.unicodeWarningMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        ADS_WARNING_MESSAGE = fileConfiguration.getString("messages.adsWarningMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SWEAR_WARNING_MESSAGE = fileConfiguration.getString("messages.swearWarningMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        CAPS_WARNING_MESSAGE = fileConfiguration.getString("messages.capsWarningMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        PLAYER_NOT_ONLINE_MESSAGE = fileConfiguration.getString("messages.playerNotOnlineMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        CLEAR_CHAT_CLEARED_MESSAGE = fileConfiguration.getString("messages.clearChatClearedMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        CLEAR_CHAT_HELP_MESSAGE = fileConfiguration.getString("messages.clearChatHelpMessage").equals("") ? null : fileConfiguration.getString("messages.clearChatHelpMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        MUTE_CHAT_BROADCAST_MESSAGE = fileConfiguration.getString("messages.muteChatBroadcastMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        UNMUTE_CHAT_BROADCAST_MESSAGE = fileConfiguration.getString("messages.unmuteChatBroadcastMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MUTE_CHAT_HELP_MESSAGE = fileConfiguration.getString("messages.muteChatHelpMessage").equals("") ? null : fileConfiguration.getString("messages.muteChatHelpMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MUTE_CHAT_MUTED_MESSAGE = fileConfiguration.getString("messages.muteChatMutedMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        SAVE_LOG_SUCCESSFULLY_MESSAGE = fileConfiguration.getString("messages.saveLogSuccessfullyMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SAVE_LOG_FAILURE_MESSAGE = fileConfiguration.getString("messages.saveLogFailureMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        DOMAIN_ARRAY = fileConfiguration.getStringList("advertisement.endings").toArray(new String[] {});
    }
}

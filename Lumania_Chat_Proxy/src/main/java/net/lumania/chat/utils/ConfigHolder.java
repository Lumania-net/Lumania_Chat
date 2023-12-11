package net.lumania.chat.utils;

import net.lumania.chat.LumaniaChatPlugin;
import net.md_5.bungee.config.Configuration;

public class ConfigHolder {

    private final LumaniaChatPlugin chatPlugin;

    public ConfigHolder(LumaniaChatPlugin chatPlugin) {
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

    public static String ANTI_UNICODE_BYPASS;
    public static String UNICODE_WARNING_MESSAGE;


    public static String MESSAGE_TOGGLE_ACTIVATED_MESSAGE;
    public static String MESSAGE_TOGGLE_DEACTIVATED_MESSAGE;
    public static String MESSAGE_CANT_MESSAGE_YOURSELF_MESSAGE;
    public static String MESSAGE_PLAYER_NOT_ONLINE_MESSAGE;
    public static String MESSAGE_PLAYER_DOES_NOT_WANT_MESSAGES_MESSAGE;
    public static String MESSAGE_HELP_MESSAGE;

    public static String REPLY_HELP_MESSAGE;
    public static String REPLY_DONT_HAVE_OPEN_CONVERSATION_MESSAGE;
    public static String REPLY_PLAYER_NOT_ONLINE_MESSAGE;

    public static String SPY_NO_SPYING_RIGHT_NOW_MESSAGE;
    public static String SPY_ENDED_SPYING_MESSAGE;
    public static String SPY_ALREADY_SPYING_MESSAGE;
    public static String SPY_PLAYER_NOT_ONLINE_MESSAGE;
    public static String SPY_START_SPYING_MESSAGE;
    public static String SPY_SPYING_PLAYER_WENT_OFFLINE_MESSAGE;
    public static String SPY_HELP_MESSAGE;

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

        ANTI_UNICODE_BYPASS = configuration.getString("permissions.bypasses.antiUnicodeBypass");
        UNICODE_WARNING_MESSAGE = configuration.getString("messages.unicodeWarningMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        MESSAGE_TOGGLE_ACTIVATED_MESSAGE = configuration.getString("messages.messageToggleActivatedMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MESSAGE_TOGGLE_DEACTIVATED_MESSAGE = configuration.getString("messages.messageToggleDeactivatedMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MESSAGE_CANT_MESSAGE_YOURSELF_MESSAGE = configuration.getString("messages.messageCantMessageYourselfMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MESSAGE_PLAYER_NOT_ONLINE_MESSAGE = configuration.getString("messages.messagePlayerNotOnlineMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MESSAGE_PLAYER_DOES_NOT_WANT_MESSAGES_MESSAGE = configuration.getString("messages.messagePlayerDoesNotWantMessagesMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        MESSAGE_HELP_MESSAGE = configuration.getString("messages.messageHelpMessage").equals("") ? null : configuration.getString("messages.messageHelpMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        REPLY_DONT_HAVE_OPEN_CONVERSATION_MESSAGE = configuration.getString("messages.replyNoRunningConversationMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        REPLY_PLAYER_NOT_ONLINE_MESSAGE = configuration.getString("messages.replyPlayerNotOnlineMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        REPLY_HELP_MESSAGE = configuration.getString("messages.replyHelpMessage").equals("") ? null : configuration.getString("messages.replyHelpMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");

        SPY_NO_SPYING_RIGHT_NOW_MESSAGE = configuration.getString("messages.spyNoSpyingRightNowMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SPY_ENDED_SPYING_MESSAGE = configuration.getString("messages.spyEndedSpyingMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SPY_ALREADY_SPYING_MESSAGE = configuration.getString("messages.spyAlreadySpyingMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SPY_PLAYER_NOT_ONLINE_MESSAGE = configuration.getString("messages.spyPlayerNotOnlineMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SPY_START_SPYING_MESSAGE = configuration.getString("messages.spyStartSpyingMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SPY_SPYING_PLAYER_WENT_OFFLINE_MESSAGE = configuration.getString("messages.spySpyingPlayerWentOfflineMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
        SPY_HELP_MESSAGE = configuration.getString("messages.spyHelpMessage").equals("") ? null : configuration.getString("messages.spyHelpMessage").replaceAll("%prefix%", LumaniaChatPlugin.PREFIX).replaceAll("&", "§");
    }
}

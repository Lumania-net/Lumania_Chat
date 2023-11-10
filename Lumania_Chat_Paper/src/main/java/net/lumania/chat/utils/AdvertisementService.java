package net.lumania.chat.utils;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvertisementService {

    private final LumaniaChatPlugin chatPlugin;

    private final Pattern urlPattern = Pattern.compile("((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,])?)?)");

    public AdvertisementService(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    private boolean validateUrl(String message) {
        //message = message.replaceAll(" ", ".").replaceAll(";", ".").replaceAll(",", ".").replaceAll(":", ".");

        return urlPattern.matcher(message).find();
    }
}

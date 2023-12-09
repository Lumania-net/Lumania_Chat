package net.lumania.chat.utils;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvertisementService {

    private final LumaniaChatPlugin chatPlugin;

    public AdvertisementService(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    public boolean containsAdvertisement(String message) {
        message = message.replaceAll("(dot|DOT|Dot|dOt|doT|DOt|dOT|DoT|d0t|D0T|D0t|d0T|!|,|:|_|-)", ".");
        Pattern validHostname = Pattern.compile("^(?=(?:.*?[\\.\\,]){1})(?:[a-z][a-z0-9-]*[a-z0-9](?=[\\.,][a-z]|$)[\\.,:;|\\\\]?)+$");

        boolean found = false;

        Matcher matcher = validHostname.matcher(message);

        while(matcher.find()) {
            return true;
        }

        message = message.replaceAll(" ", "");
        String[] messageArr = message.split("\\.");

        for(int i = 0; i < messageArr.length; i++) {
            for(int j = 0; j < ConfigHolder.DOMAIN_ARRAY.length; j++) {
                if(messageArr[i].toLowerCase().startsWith(ConfigHolder.DOMAIN_ARRAY[j]))
                    return true;
            }
        }

        return found;
    }
}

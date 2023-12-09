package net.lumania.chat.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdTester {

    public static final String[] ARRAY = new String[] {"de", "com", "net", "io", "org"};

    private static boolean containsAdvertisement(String message) {
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
            for(int j = 0; j < ARRAY.length; j++) {
                if(messageArr[i].toLowerCase().startsWith(ARRAY[j]))
                    return true;
            }
        }

        return found;
    }

    public static void main(String[] args) {
        System.out.println(containsAdvertisement("Hallo wie gehts euch. test"));
    }
}

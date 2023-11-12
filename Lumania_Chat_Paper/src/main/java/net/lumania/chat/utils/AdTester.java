package net.lumania.chat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdTester {

    private static boolean containsAdvertisement(String message) {
        message = message.replaceAll("(;|=|:|_|-|,|!|/|\\\\)", ".");

        String[] contents = message.replaceAll("(dot|DOT|Dot|dOt|doT|DOt|dOT|DoT|d0t|D0T|D0t|d0t|d0T|D0t|d0T|D0T)", ".").trim().split(" ");

        Pattern validHostName = Pattern.compile("^(?=(?:.*?[\\\\.\\\\,]){1})(?:[a-z][a-z0-9-]*[a-z0-9](?=[\\\\.,][a-z]|$)[\\\\.,:;|\\\\\\\\]?)+$");
        Pattern validIpName = Pattern.compile("^(?:(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(?::\\d*)?$", 2);

        for (String content : contents) {
            String tempId = content.trim().toLowerCase().replaceAll("[\\(\\)!@#\\$%\\^\\s\\&\\*;\"'\\?><~`,\\\\a-zA-Z]", "");
            String tempHost = content.trim().toLowerCase().replaceAll("[\\d\\s\\(\\)!@#\\$%\\^\\s\\&\\*:;\"'\\?><~`,\\\\]", "");

            if (validIpName.matcher(tempId).find())
                return true;

            if (validHostName.matcher(tempHost).find())
                return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(containsAdvertisement("Hallo ich habe ein test.de"));
    }
}

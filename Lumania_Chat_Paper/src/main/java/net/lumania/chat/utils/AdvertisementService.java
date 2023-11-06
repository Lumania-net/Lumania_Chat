package net.lumania.chat.utils;

import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvertisementService {

    private final LumaniaChatPlugin chatPlugin;

    public AdvertisementService(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
    }

    private final Pattern ipPattern = Pattern.compile("(?:\\d{1,3}[.,\\-:;\\/()=?}+ ]{1,4}){3}\\d{1,3}");

    private final Pattern webPatternAdv = Pattern.compile("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+~#?&//=]*)?");
    private final Pattern webPatternSimple = Pattern.compile("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.(com|ru|net|org|de|jp|uk|br|pl|in|it|fr|au|info|nl|cn|ir|es|cz|biz|ca|kr|eu|ua|za|co|gr|ro|se|tw|vn|mx|ch|tr|at|be|hu|dk|tv|me|ar|us|no|sk|fi|id|cl|nz|by|pt)\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?");

    public boolean advertising(String playerName, String message) {
        return checkIp(playerName, message) || checkWeb(playerName, message);
    }

    private boolean checkIp(String playerName, String message) {
        boolean advertising = false;

        Matcher matcher = this.ipPattern.matcher(message);

        while (matcher.find()) {
            if(!matcher.group().isEmpty() && this.ipPattern.matcher(message).find()) {
                String advertisement = matcher.group().trim();
                advertising = true;

                this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, playerName + " send Ad: " + advertisement);
            }
        }

        return advertising;
    }

    private boolean checkWeb(String playerName, String message) {
        boolean advertising = false;

        Matcher matcher = this.webPatternAdv.matcher(message);

        while (matcher.find()) {
            if (!matcher.group().isEmpty() && this.webPatternAdv.matcher(message).find()) {
                if(this.webPatternAdv.matcher(message).find()) {
                    String advertisement = matcher.group().trim();
                    advertising = true;

                    this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, playerName + " send Ad: " + advertisement);
                }
            }
        }

        matcher = this.webPatternSimple.matcher(message);

        while (matcher.find()) {
            if (!matcher.group().isEmpty() && this.webPatternSimple.matcher(message).find()) {
                if(this.webPatternSimple.matcher(message).find()) {
                    String advertisement = matcher.group().trim();
                    advertising = true;

                    this.chatPlugin.getLoggingService().addLog(LoggingType.VIOLATION, playerName + " send Ad: " + advertisement);
                }
            }
        }

        return advertising;
    }

}

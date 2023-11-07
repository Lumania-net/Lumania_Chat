package net.lumania.chat.logger;

import net.lumania.chat.LumaniaChatPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoggingService {

    private final LumaniaChatPlugin chatPlugin;
    private final List<String> textLogs;

    public LoggingService(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
        this.textLogs = new ArrayList<>();
    }

    public void addLog(LoggingType loggingType, String message) {
        this.textLogs.add(this.getCurrentDate() + " " + loggingType.name() + " " + message);
    }

    public void saveLog() throws IOException {
        File file = new File(this.chatPlugin.getDataFolder() + "/logs/", this.getCurrentDate() + "-logs.txt");

        if(!file.exists())
            file.createNewFile();

        FileWriter fileWriter = new FileWriter(file);

        for(String string : this.textLogs) {
            fileWriter.write(string + System.lineSeparator());
        }

        fileWriter.close();

        this.textLogs.clear();
    }

    private String getCurrentDate() {
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm");
        Date date = new Date(currentTime);

        return simpleDateFormat.format(date);
    }
}

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
        this.textLogs.add("[" + this.getCurrentLogTime() + "] [" + loggingType.name() + "] " + message);
    }

    public void saveLog() throws IOException {
        this.chatPlugin.getLogger().info(this.textLogs.size() + " size of text logs");

        String datetime = this.getCurrentDate();

        File file = new File(this.chatPlugin.getDataFolder(), datetime + "-log.txt");

        if(!file.exists())
            file.createNewFile();

        FileWriter fileWriter = new FileWriter(file);

        for(String string : this.textLogs) {
            fileWriter.write(string + System.lineSeparator());
        }

        fileWriter.close();

        this.textLogs.clear();

        this.chatPlugin.getLogger().info("Saved log successfully in " + file.getName());
    }

    private String getCurrentDate() {
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");
        Date date = new Date(currentTime);

        return simpleDateFormat.format(date);
    }

    private String getCurrentLogTime() {
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date(currentTime);

        return simpleDateFormat.format(date);
    }

    public List<String> getTextLogs() {
        return textLogs;
    }
}

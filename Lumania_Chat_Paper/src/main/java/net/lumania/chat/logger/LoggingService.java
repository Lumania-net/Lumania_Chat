package net.lumania.chat.logger;

import net.lumania.chat.LumaniaChatPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class LoggingService {

    private final LumaniaChatPlugin chatPlugin;
    private final List<String> textLogs;

    public LoggingService(LumaniaChatPlugin chatPlugin) {
        this.chatPlugin = chatPlugin;
        this.textLogs = new ArrayList<>();
    }

    public void addLog(LoggingType loggingType, String message, UUID playerUniqueId) {
        this.textLogs.add("[" + playerUniqueId.toString() + "] [" + this.getCurrentLogTime() + "] [" + loggingType.name() + "] " + message);
    }

    public String saveLog() throws IOException {
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

        return file.getName();
    }

    private String getCurrentDate() {
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Date date = new Date(currentTime);

        return simpleDateFormat.format(date);
    }

    private String getCurrentLogTime() {
        long currentTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);

        return simpleDateFormat.format(date);
    }

    public List<String> getTextLogs() {
        return textLogs;
    }

    public void addLogToDatabase(String... fileNames) {
        File dataFolder = this.chatPlugin.getDataFolder();

        CompletableFuture.runAsync(() -> {
            for(String fileName : fileNames) {
                List<String> logList = new ArrayList<>();

                File logFile = new File(dataFolder, fileName);

                if(!logFile.exists())
                    continue;

                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile))) {
                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        logList.add(line);
                } catch (IOException e) {
                    chatPlugin.getLogger().severe("Error while trying to read lines of file " + fileName);
                }

                logList.forEach(log -> {
                    String[] contents = log.split(" ");

                    String uuid = contents[0].replace("[", "").replace("]", "");
                    String datetime = (contents[1] + " " + contents[2]).replace("[", "").replace("]", "");
                    String loggingType = contents[3].replace("[", "").replace("]", "");

                    String[] actionArray = new String[contents.length - 4];

                    System.arraycopy(contents, 4, actionArray, 0, contents.length - 4);

                    String action = String.join(" ", actionArray);

                    this.chatPlugin.getDatabaseService().addLog(uuid, loggingType, datetime, action);
                });
            }
        });
    }
}

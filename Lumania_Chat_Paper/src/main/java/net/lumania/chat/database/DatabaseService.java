package net.lumania.chat.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.logger.LoggingType;
import net.lumania.chat.utils.PermissionHolder;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class DatabaseService {

    private final DataSource dataSource;
    private final LumaniaChatPlugin chatPlugin;

    private Connection connection;

    public DatabaseService(LumaniaChatPlugin chatPlugin) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mariadb://" + PermissionHolder.DATABASE_HOST + ":" + PermissionHolder.DATABASE_PORT + "/" + PermissionHolder.DATABASE_DATABASE);
        hikariConfig.setUsername(PermissionHolder.DATABASE_USERNAME);
        hikariConfig.setPassword(PermissionHolder.DATABASE_PASSWORD);

        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(hikariConfig);
        this.chatPlugin = chatPlugin;

        try {
            this.connection = dataSource.getConnection();

            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS chat_logs (uuid VARCHAR(64), logging_type VARCHAR(12), datetime DATE, stamp VARCHAR(16), player_action VARCHAR(256))").executeUpdate();
        } catch (SQLException e) {
            this.chatPlugin.getLogger().severe("Error while trying to connect to database");
            this.chatPlugin.getLogger().severe("Shutting down...");

            Bukkit.getPluginManager().disablePlugin(this.chatPlugin);
        }
    }
    public void addLog(String uuid, String loggingType, String datetime, String action) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat_logs (uuid, logging_type, datetime, stamp, player_action) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, loggingType);
            preparedStatement.setDate(3, Date.valueOf(datetime.split(" ")[0]));
            preparedStatement.setString(4, datetime.split(" ")[1]);
            preparedStatement.setString(5, action);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            chatPlugin.getLogger().severe("Error while inserting player into database");
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}

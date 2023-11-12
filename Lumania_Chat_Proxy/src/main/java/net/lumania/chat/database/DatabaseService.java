package net.lumania.chat.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.lumania.chat.LumaniaChatPlugin;
import net.lumania.chat.utils.PermissionHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS chat (uuid VARCHAR(64), toggled VARCHAR(10))").executeUpdate();
        } catch (SQLException e) {
            this.chatPlugin.getLogger().severe("Error while trying to connect to database");
            this.chatPlugin.getLogger().severe("Shutting down...");

            this.chatPlugin.getProxy().getPluginManager().unregisterListeners(this.chatPlugin);
            this.chatPlugin.getProxy().getPluginManager().unregisterCommands(this.chatPlugin);

            this.chatPlugin.onDisable();
        }
    }

    public boolean playerInDatabase(UUID uuid) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT uuid FROM chat WHERE uuid = ?");
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            chatPlugin.getLogger().severe("Error in database");
        }

        return false;
    }

    public void setupPlayer(UUID uuid, boolean messageToggle) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat (uuid, toggled) VALUES (?, ?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setBoolean(2, messageToggle);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            chatPlugin.getLogger().severe("Error while inserting player into database");
        }
    }

    public void updatePlayer(UUID uuid, boolean toggled) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chat SET toggled = ? WHERE uuid = ?");
            preparedStatement.setBoolean(1, toggled);
            preparedStatement.setString(2, uuid.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            chatPlugin.getLogger().severe("Error while updating player in database");
        }
    }

    public boolean getPlayerToggled(UUID uuid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT toggled FROM chat WHERE uuid = ?");
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
                return resultSet.getBoolean("toggled");
        } catch (SQLException e) {
            chatPlugin.getLogger().severe("Error while trying to get toggled attribute of player in database");
        }

        return true;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}

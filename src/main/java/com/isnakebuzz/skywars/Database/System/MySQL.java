package com.isnakebuzz.skywars.Database.System;

import com.isnakebuzz.skywars.Calls.Callback;
import com.isnakebuzz.skywars.Main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    private Main plugin;
    private HikariDataSource dataSource;

    public MySQL(Main plugin) {
        this.plugin = plugin;
    }

    public void init() {
        ConfigurationSection myqslConfig = plugin.getConfig("Extra/Database").getConfigurationSection("MySQL");

        boolean bool = myqslConfig.getBoolean("enabled");

        String ip = myqslConfig.getString("hostname").split(":")[0];
        String port = myqslConfig.getString("hostname").split(":")[1];
        String database = myqslConfig.getString("database");
        String username = myqslConfig.getString("username");
        String password = myqslConfig.getString("password");
        HikariConfig config = new HikariConfig();
        config.setPoolName("HypeSpectPool");

        if (bool) {
            config.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false");
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        } else {
            checkFile(database);

            config.setDriverClassName("org.sqlite.JDBC");
            config.setJdbcUrl("jdbc:sqlite:" + plugin.getDataFolder().getPath() + "/" + database + ".db");
            config.setConnectionTestQuery("SELECT 1");
            config.setMaxLifetime(60000);
            config.setIdleTimeout(45000);
            config.setMaximumPoolSize(50);
        }

        dataSource = new HikariDataSource(config);
    }

    public void close() {
        this.dataSource.close();
    }

    public void preparedUpdate(String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void preparedQuery(String sql, Callback<ResultSet> callback) {
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            callback.done(resultSet);

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkFile(String dbName) {
        File file = new File(plugin.getDataFolder(), dbName + ".db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

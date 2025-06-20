package com.example.powtorzenie.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static java.sql.Connection connection;


    public static Connection getConnection() {
        return connection;
    }
    public static DatabaseConnection getInstance(String dbUrl) throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection(dbUrl);
            instance.createTable();
        }

        return instance;
    }
    public DatabaseConnection(String dbUrl) {
        String url = "jdbc:sqlite:" + dbUrl;
        try {
            if(connection == null) {
                connection = DriverManager.getConnection(url);
            }
            else {
                System.out.println("Już jesteś połączony!");
            }
        } catch (SQLException e) {
            System.out.println("Nawiązanie połączenia z bazą danych się nie powiodło");
        }
    }
    public void createTable() throws SQLException {
        String create = "CREATE TABLE  IF NOT EXISTS dot(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "x INTEGER NOT NULL," +
                "y INTEGER NOT NULL," +
                "color TEXT NOT NULL," +
                "radius INTEGER NOT NULL" +
        ");";
        PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(create);
        statement.executeUpdate();
    }
    public void disconnect() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Próba rozłączenia z db nie zakończyła się powodzeniem");
            }
        }
    }
}
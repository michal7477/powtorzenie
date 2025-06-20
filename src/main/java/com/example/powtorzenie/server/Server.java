package com.example.powtorzenie.server;

import com.example.powtorzenie.Dot;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientThread> clients;
    private DatabaseConnection connection;
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    public void broadcast(String message) throws SQLException {
        for (ClientThread c : clients){
            c.send(message);
        }
        saveDot(Dot.fromMessage(message));
    }

    public void listen() throws IOException, SQLException {

        connection = DatabaseConnection.getInstance("dot.db");

        while(true) {
            System.out.println("Server oczekuje na połączenie...");
            Socket socket = serverSocket.accept();
            System.out.println("Połączono z klientem");
            ClientThread client = new ClientThread(socket,this);
            clients.add(client);
            Thread thread = new Thread(client);
            thread.start();
            List<Dot> dots = getSavedDots();
            String clientMessage = "";
            System.out.println(dots.toString());
            for (Dot d : dots){
                if (dots.size() > 0) {
                    client.send(d.sendMessage());
                }
            }

        }
    }
    public void saveDot(Dot dot) throws SQLException {
        String insertSQL = "INSERT INTO dot( x, y, color, radius) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(insertSQL);

        statement.setInt(1, dot.x());
        statement.setInt(2, dot.y());
        statement.setString(3, dot.color().toString());
        statement.setInt(4, dot.radius());
        statement.executeUpdate();
    }

    public List<Dot> getSavedDots() throws SQLException {
        String sql = "SELECT x, y, color, radius FROM dot";

        PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);

        if (!statement.execute()) throw new RuntimeException("SELECT failed");
        List<Dot> dots = new ArrayList<>();
        ResultSet result = statement.getResultSet();
        while(result.next()) {
            int x = result.getInt(1);
            int y = result.getInt(2);
            Color color = Color.valueOf(result.getString(3));
            int radius = result.getInt(4);


            dots.add(new Dot(x, y, color, radius));
        }
        return dots;
    }
}

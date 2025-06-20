package com.example.powtorzenie.server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread extends Thread {


    private Socket socket;
    private PrintWriter writer;
    private Server server;



    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }
    public void run(){

        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(message);
                server.broadcast(message);
            }
            System.out.println("closed");
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message){
        writer.println(message);
    }
}


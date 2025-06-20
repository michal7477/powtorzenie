package com.example.powtorzenie.client;

import com.example.powtorzenie.Dot;
import com.example.powtorzenie.HelloController;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerThread extends Thread{
    private BufferedReader reader;
    private PrintWriter writer;

    private HelloController controller;
    private Consumer<Dot> dotConsumer;

    public void setDotConsumer(Consumer<Dot> dotConsumer) {
        this.dotConsumer = dotConsumer;
    }

    public void setController(HelloController controller) {
        this.controller = controller;
    }

    public void connect(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(output),true);

    }


    @Override
    public void run() {
        String message;

        try {
            while((message = reader.readLine()) != null) {
                Dot dot = Dot.fromMessage(message);
                Platform.runLater(() -> dotConsumer.accept(dot));
                //System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(int x, int y, Color color, int radius){
        writer.println(String.format("%d %d %f %f %f %d", x, y, color.getRed(), color.getGreen(), color.getBlue(), radius));
    }

    public void drawDot(){

    }

}

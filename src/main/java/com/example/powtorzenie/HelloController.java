package com.example.powtorzenie;

import com.example.powtorzenie.client.ServerThread;
import com.example.powtorzenie.server.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;


public class HelloController {


    @FXML
    private Canvas canvas;
    @FXML
    private Slider radiusSlider;

    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField portField;
    @FXML
    private TextField addressField;
    private ServerThread client;
    private Consumer<Dot> dotConsumer;

    @FXML
    public void initialize() {
        dotConsumer = dot -> {
            Platform.runLater(() -> {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(dot.color());
                gc.fillOval(dot.x() - dot.radius(), dot.y() - dot.radius(),
                        dot.radius() * 2, dot.radius() * 2);
            });
        };
    }
    @FXML
    private void onMouseClicked(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        double radius = radiusSlider.getValue();
        Color color = colorPicker.getValue();

//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.setFill(color);
//        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        client.send((int)x, (int)y, color, (int)radius);
    }

    @FXML
    public void onStartServerClicked() throws IOException {
        Server server = new Server(Integer.parseInt(portField.getText()));
        Thread serverThread = new Thread(() -> {
            try {
                server.listen();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

        System.out.println("Serwer wystartował w tle.");
    }
    @FXML
    public void onConnectClicked() throws IOException {

        client = new ServerThread();
        client.connect(addressField.getText(), Integer.parseInt(portField.getText()));
        client.setController(this);
        client.setDotConsumer(dot -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(dot.color());
            gc.fillOval(dot.x() - dot.radius(), dot.y() - dot.radius(),
                    dot.radius() * 2, dot.radius() * 2);
        });
        client.start();
    }



}
package com.example.powtorzenie;

import javafx.scene.paint.Color;

public record Dot(int x, int y, Color color, int radius) {
    public static String toMessage(int x, int y, Color color, int radius){
        return String.format("%d %d %f %f %f %d", x, y, color.getRed(), color.getGreen(), color.getBlue(), radius);
    }
    public static Dot fromMessage(String message){
        String[] cols = message.split(" ");

        int x = Integer.parseInt(cols[0]);
        int y = Integer.parseInt(cols[1]);
        double red = Double.parseDouble(cols[2].replace(",", "."));
        double green = Double.parseDouble(cols[3].replace(",", "."));
        double blue = Double.parseDouble(cols[4].replace(",", "."));
        int radius = Integer.parseInt(cols[5]);


        Color color = new Color(red, green, blue, 1.0);
        //Color color = Color.color(red, green, blue);
        return new Dot(x, y, color, radius);

    }
    public String sendMessage(){
        return String.format("%d %d %f %f %f %d", x, y, color.getRed(), color.getGreen(), color.getBlue(), radius);
    }
}

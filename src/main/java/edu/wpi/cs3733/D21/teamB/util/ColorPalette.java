package edu.wpi.cs3733.D21.teamB.util;

import javafx.scene.paint.Color;

public class ColorPalette {

    public static Color getColorOfNodeType(String type){
        Color color = null;
        switch(type){
            case "SERV":
                return Color.web("1B5E20");
            case "REST":
                return Color.web("880E4F");
            case "LABS":
                return Color.web("E91E63");
            case "ELEV":
                return Color.web("4A148C");
            case "DEPT":
                return Color.web("006064");
            case "CONF":
                return Color.web("00897B");
            case "HALL":
                return Color.web("012D5A");
            case "WALK":
                return Color.web("26C6DA");
            case "INFO":
                return Color.web("212121");
            case "RETL":
                return Color.web("FFEB3B");
            case "BATH":
                return Color.web("66BB6A");
            case "EXIT":
                return Color.web("B71C1C");
            case "STAI":
                return Color.web("BF360C");
            case "PARK":
                return Color.web("5D4037");
            default:
                throw new IllegalArgumentException("How did we get here?");
        }
    }
}

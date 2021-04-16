package edu.wpi.teamB.util;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;

@Getter
public class GraphicalEditorData {
    private final double x;
    private final double y;
    private final AnchorPane nodeHolder;

    public GraphicalEditorData(double x, double y, AnchorPane nodeHolder){
        this.x = x;
        this.y = y;
        this.nodeHolder = nodeHolder;
    }
}

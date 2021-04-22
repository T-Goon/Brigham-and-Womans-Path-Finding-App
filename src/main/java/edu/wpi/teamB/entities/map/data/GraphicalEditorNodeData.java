package edu.wpi.teamB.entities.map.data;

import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalEditorNodeData {
    private final String nodeID;
    private final double x;
    private final double y;
    private final String floor;
    private final String building;
    private final String nodeType;
    private final String longName;
    private final String shortName;
    private final Boolean restricted;
    private final StackPane mapStack;
    private final PathfindingMenuController pfmc;
    private final Circle circle;
}

package edu.wpi.teamB.util;

import edu.wpi.teamB.views.menus.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

@Getter
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
    private final AnchorPane nodeHolder;
    private final PathfindingMenuController pfmc;

    public GraphicalEditorNodeData(String nodeID, double x, double y, String floor, String building, String nodeType, String longName, String shortName, Boolean restricted, AnchorPane nodeHolder, PathfindingMenuController pfmc) {
        this.nodeID = nodeID;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.restricted = restricted;
        this.nodeHolder = nodeHolder;
        this.pfmc = pfmc;
    }
}

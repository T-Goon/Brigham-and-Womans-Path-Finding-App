package edu.wpi.teamB.util;

import edu.wpi.teamB.views.mapEditor.NodePopupWindowController;
import edu.wpi.teamB.views.menus.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

@Getter
public class GraphicalNodeDelData extends GraphicalEditorNodeData{
    private final NodePopupWindowController parent;

    public GraphicalNodeDelData(String nodeID, double x, double y, String floor, String building, String nodeType,
                                String longName, String shortName, Boolean restricted, AnchorPane nodeHolder,
                                PathfindingMenuController pfmc, NodePopupWindowController parent) {
        super(nodeID, x, y, floor, building, nodeType, longName, shortName, restricted, nodeHolder, pfmc);

        this.parent = parent;
    }
}

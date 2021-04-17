package edu.wpi.teamB.util;

import edu.wpi.teamB.views.mapEditor.NodePopupWindowController;
import edu.wpi.teamB.views.menus.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalNodeDelData{
    private final GraphicalEditorNodeData data;
    private final NodePopupWindowController parent;

}

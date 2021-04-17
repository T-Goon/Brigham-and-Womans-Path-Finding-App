package edu.wpi.teamB.util;

import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.views.menus.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class GraphicalEditorEdgeData {
    private final Node start;
    private final Node end;
    private final AnchorPane nodeHolder;
    private final PathfindingMenuController pmfc;
}

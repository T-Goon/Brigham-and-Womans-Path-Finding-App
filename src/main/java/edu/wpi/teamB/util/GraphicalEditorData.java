package edu.wpi.teamB.util;

import edu.wpi.teamB.views.menus.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalEditorData {
    private final double x;
    private final double y;
    private final String floor;
    private final AnchorPane nodeHolder;
    private final PathfindingMenuController pfmc;

}

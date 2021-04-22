package edu.wpi.teamB.entities.map.data;

import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.scene.layout.StackPane;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class GraphicalEditorEdgeData {
    private final Node start;
    private final Node end;
    private final StackPane mapStack;
    private final PathfindingMenuController pmfc;
}

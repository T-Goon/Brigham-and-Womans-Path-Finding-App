package edu.wpi.teamB.util;

import edu.wpi.teamB.views.mapEditor.DelEdgePopupController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalEdgePopupData {
    private final GraphicalEditorEdgeData data;
    private final DelEdgePopupController parent;
}

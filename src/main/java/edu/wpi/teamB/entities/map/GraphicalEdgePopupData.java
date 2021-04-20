package edu.wpi.teamB.entities.map;

import edu.wpi.teamB.views.map.edgePopup.DelEdgePopupController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalEdgePopupData {
    private final GraphicalEditorEdgeData data;
    private final DelEdgePopupController parent;
}

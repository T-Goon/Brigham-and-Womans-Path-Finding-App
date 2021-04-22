package edu.wpi.teamB.entities.map.data;

import edu.wpi.teamB.views.map.nodePopup.NodePopupWindowController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalNodePopupData {
    private final GraphicalEditorNodeData data;
    private final NodePopupWindowController parent;

}

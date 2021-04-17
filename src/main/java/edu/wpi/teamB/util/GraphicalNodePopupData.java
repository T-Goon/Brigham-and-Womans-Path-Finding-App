package edu.wpi.teamB.util;

import edu.wpi.teamB.views.mapEditor.graphical.nodePopup.NodePopupWindowController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalNodePopupData {
    private final GraphicalEditorNodeData data;
    private final NodePopupWindowController parent;

}

package edu.wpi.teamB.util;

import edu.wpi.teamB.views.mapEditor.NodePopupWindowController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphicalNodeEditData {
    private final GraphicalEditorNodeData data;
    private final NodePopupWindowController parent;
}

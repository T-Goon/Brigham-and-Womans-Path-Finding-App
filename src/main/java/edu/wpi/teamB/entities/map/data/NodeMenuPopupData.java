package edu.wpi.teamB.entities.map.data;

import edu.wpi.teamB.entities.map.MapDrawer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NodeMenuPopupData {
    private final String nodeID;
    private final double x;
    private final double y;
    private final String floor;
    private final String building;
    private final String nodeType;
    private final String longName;
    private final String shortName;
    private final boolean fromTree;
    private final MapDrawer md;
}

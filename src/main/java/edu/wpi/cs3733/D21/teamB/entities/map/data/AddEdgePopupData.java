package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddEdgePopupData {
    private final MapCache mc;
    private final MapDrawer md;
}

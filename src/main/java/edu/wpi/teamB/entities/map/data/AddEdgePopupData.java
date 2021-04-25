package edu.wpi.teamB.entities.map.data;

import edu.wpi.teamB.entities.map.MapCache;
import edu.wpi.teamB.entities.map.MapDrawer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddEdgePopupData {
    private final MapCache mc;
    private final MapDrawer md;
}

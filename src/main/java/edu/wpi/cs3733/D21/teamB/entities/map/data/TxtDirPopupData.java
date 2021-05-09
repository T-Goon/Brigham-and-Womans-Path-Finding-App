package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.map.FloorSwitcher;
import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import javafx.scene.layout.StackPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.util.List;

@Getter
@AllArgsConstructor
public class TxtDirPopupData {
    private final List<Directions.Direction> instructions;
    private final MapDrawer mapDrawer;
    private final MapCache mapCache;
    private final FloorSwitcher floorSwitcher;
    private final GesturePane gesturePane;
    private final StackPane stackPane;
}

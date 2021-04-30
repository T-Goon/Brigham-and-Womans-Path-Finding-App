package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import javafx.scene.shape.Line;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

@AllArgsConstructor
@Getter
public class DelEdgePopupData {
    private final Node start;
    private final Node end;
    private final GesturePane gesturePane;
    private final MapDrawer md;
    private final MapCache mc;
    private final int X;
    private final int Y;
    private final Line line;
}

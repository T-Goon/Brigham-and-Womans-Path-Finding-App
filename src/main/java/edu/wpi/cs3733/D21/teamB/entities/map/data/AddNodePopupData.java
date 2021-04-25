package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

@Getter
@AllArgsConstructor
public class AddNodePopupData {
    private final double x;
    private final double y;
    private final String floor;
    private final MapDrawer md;
    private final GesturePane gesturePane;
}

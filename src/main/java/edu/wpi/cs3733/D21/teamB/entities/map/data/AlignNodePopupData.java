package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import javafx.scene.shape.Circle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.util.List;

@Getter
@AllArgsConstructor
public class AlignNodePopupData {
    private final GesturePane gesturePane;
    private final MapDrawer mapDrawer;
    private final List<Circle> nodes;
}

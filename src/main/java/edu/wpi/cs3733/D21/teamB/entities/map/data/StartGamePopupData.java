package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

@Getter
@AllArgsConstructor
public class StartGamePopupData {
    private final MapDrawer md;
    private final MapCache mc;
    private final AnchorPane ap;
    private final GesturePane gesturePane;
    private final Label score;
}

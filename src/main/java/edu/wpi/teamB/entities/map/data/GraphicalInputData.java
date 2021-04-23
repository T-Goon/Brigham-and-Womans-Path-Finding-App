package edu.wpi.teamB.entities.map.data;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.entities.map.MapDrawer;
import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GraphicalInputData {
    private final String nodeName;
    private final JFXTextField startTxt;
    private final JFXTextField endTxt;
    private final MapDrawer md;
    private final PathfindingMenuController pfmc;
}

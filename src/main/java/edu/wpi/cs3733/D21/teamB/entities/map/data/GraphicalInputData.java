package edu.wpi.cs3733.D21.teamB.entities.map.data;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GraphicalInputData {
    private final String nodeName;
    private final JFXTextField startTxt;
    private final JFXTextField endTxt;
    private final JFXButton btnRmStop;
    private final MapDrawer md;
    private final MapCache mc;
    private final PathfindingMenuController pfmc;
}

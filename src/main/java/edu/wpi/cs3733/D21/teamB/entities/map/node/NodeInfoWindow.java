package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class NodeInfoWindow extends Window<VBox, GraphicalInputData, VBox> implements Poppable {
    public NodeInfoWindow(Pane parent, GraphicalInputData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Show the window
     */
    @Override
    public void show() {
        VBox nodeInfoWindow = null;

        // Pass to the controller
        App.getPrimaryStage().setUserData(this);

        // Load window
        try {
            nodeInfoWindow = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/nodeInfoWindow.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(nodeInfoWindow);
    }
}

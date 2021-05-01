package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class ChangeParkingSpotPopup extends Window<VBox, GraphicalInputData, VBox> implements Poppable {

    public ChangeParkingSpotPopup(Pane parent, GraphicalInputData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Show this popup on the map
     */
    @Override
    public void show() {
        VBox popup = null;

        try {
            popup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/misc/changeParkingSpot.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(popup);
    }
}

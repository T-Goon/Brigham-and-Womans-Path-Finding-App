package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.entities.map.data.AlignNodePopupData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.ChangeParkingSpotData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class ChangeParkingSpotPopup extends Popup<VBox, ChangeParkingSpotData> implements Poppable {

    public ChangeParkingSpotPopup(Pane parent, ChangeParkingSpotData data) {
        super(parent, data);
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

        data.getGesturePane().setGestureEnabled(false);
        super.show(popup);
    }

    /**
     * Remove the popup from the map.
     */
    @Override
    public void hide() {
        super.hide();
    }
}

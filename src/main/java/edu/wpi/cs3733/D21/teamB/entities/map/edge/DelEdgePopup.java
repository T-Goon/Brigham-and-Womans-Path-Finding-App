package edu.wpi.cs3733.D21.teamB.entities.map.edge;

import edu.wpi.cs3733.D21.teamB.entities.map.data.DelEdgePopupData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class DelEdgePopup extends Popup<VBox, DelEdgePopupData> implements Poppable {

    public DelEdgePopup(Pane parent, DelEdgePopupData data) {
        super(parent, data);
    }

    /**
     * Shows the popup on the map
     */
    public void show() {
        VBox popup = null;

        try {
            popup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/edgePopup/delEdgePopup.fxml")));
        } catch (IOException e) { e.printStackTrace(); }

        data.getGesturePane().setGestureEnabled(false);

        super.show(popup);
    }

    /**
     * Removed the popup from the map
     */
    @Override
    public void hide() {
        super.hide();
    }
}

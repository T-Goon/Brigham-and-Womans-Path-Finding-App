package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.entities.map.data.StartGamePopupData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class StartGamePopup extends Popup<VBox, StartGamePopupData> implements Poppable {

    public StartGamePopup(Pane parent, StartGamePopupData data) {
        super(parent, data);
    }

    /**
     * Show this popup on the map
     */
    public void show() {
        VBox popup = null;

        try {
            popup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/misc/startGamePopup.fxml")));
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

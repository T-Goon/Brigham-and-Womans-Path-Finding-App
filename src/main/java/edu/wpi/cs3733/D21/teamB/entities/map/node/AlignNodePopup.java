package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.entities.map.data.AlignNodePopupData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class AlignNodePopup extends Popup<VBox, AlignNodePopupData> implements Poppable {

    public AlignNodePopup(Pane parent, AlignNodePopupData data) {
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
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/alignNodePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        data.getGesturePane().setGestureEnabled(false);
        super.show(popup);
    }

    /**
     * Aligns the given nodes
     */
    public void alignNodes() {

        // TODO alignment
        System.out.println("Consider yourself...aligned");

        hide();
        data.getMapDrawer().removeAllPopups();
        data.getMapDrawer().refreshEditor();
    }

    /**
     * Hide this popup on the map
     */
    @Override
    public void hide() {
        for (Circle c : data.getNodes()) {
            c.setStroke(Color.BLACK);
        }
        data.getMapDrawer().getAligned().clear();
        data.getGesturePane().setGestureEnabled(true);
        super.hide();
    }
}

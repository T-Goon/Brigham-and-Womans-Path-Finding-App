package edu.wpi.teamB.entities.map.edge;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.AddNodePopupData;
import edu.wpi.teamB.entities.map.data.DelEdgePopupData;
import edu.wpi.teamB.entities.map.data.GraphicalEdgePopupData;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.Objects;

public class DelEdgePopup extends Popup<VBox, DelEdgePopupData> {

    public DelEdgePopup(Pane parent, DelEdgePopupData data) {
        super(parent, data);
    }

    public void show(){
        VBox popup = null;

        try {
            popup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        data.getGesturePane().setGestureEnabled(false);

        super.show(popup);
    }

    @Override
    public void hide() {
        super.hide();
        data.getGesturePane().setGestureEnabled(true);
    }
}

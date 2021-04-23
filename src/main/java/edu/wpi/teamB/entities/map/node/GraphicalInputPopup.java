package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jdk.nashorn.internal.ir.GetSplitState;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.Objects;

public class GraphicalInputPopup extends Popup<VBox, GraphicalInputData> {

    private final GesturePane gpane;

    public GraphicalInputPopup(Pane parent, GraphicalInputData data, GesturePane gpane) {
        super(parent, data);

        this.gpane = gpane;
    }

    public void show(){

        VBox locInput = null;

        App.getPrimaryStage().setUserData(data);

        try{
            // Load fxml
            locInput = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/graphicalInput.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(locInput);
        this.gpane.setGestureEnabled(false);
    }

    @Override
    public void hide() {
        data.getMd().removeAllPopups();
        super.hide();
        this.gpane.setGestureEnabled(true);
    }
}

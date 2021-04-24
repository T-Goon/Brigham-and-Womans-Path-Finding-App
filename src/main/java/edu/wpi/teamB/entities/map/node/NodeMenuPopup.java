package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.Objects;

public class NodeMenuPopup extends Popup<VBox, NodeMenuPopupData> {

    private final GesturePane gpane;

    public NodeMenuPopup(Pane parent, NodeMenuPopupData data, GesturePane gpane) {
        super(parent, data);
        this.gpane = gpane;
    }

    public void show(){

        VBox nmPopup = null;

        try{
            nmPopup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/nodePopupWindow.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(nmPopup);
        gpane.setGestureEnabled(false);
    }

    @Override
    public void hide() {
        super.hide();
        gpane.setGestureEnabled(true);
    }
}

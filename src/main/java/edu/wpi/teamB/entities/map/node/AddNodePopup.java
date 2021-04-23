package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.AddNodePopupData;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class AddNodePopup extends Popup<VBox, AddNodePopupData> {

    public AddNodePopup(Pane parent, AddNodePopupData data) {
        super(parent, data);
    }

    public void show(){
        VBox popup = null;

        try {
            popup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/addNodePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        data.getGesturePane().setGestureEnabled(false);

        super.show(popup);
    }

    public void addNode(String nodeID, int x, int y, String floor, String building, String type, String longName, String shortName){

        Node aNode = new Node(nodeID, x, y, floor, building, type, longName, shortName);
        DatabaseHandler.getDatabaseHandler("main.db").addNode(aNode);

        // Refresh map editor
        data.getMd().refreshEditor();

        // Remove popup from map
        hide();
    }

    @Override
    public void hide() {
        super.hide();
        data.getGesturePane().setGestureEnabled(true);
    }
}

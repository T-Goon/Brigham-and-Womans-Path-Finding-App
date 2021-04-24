package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.AddNodePopupData;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddNodePopup extends Popup<VBox, AddNodePopupData> {

    public AddNodePopup(Pane parent, AddNodePopupData data) {
        super(parent, data);
    }

    /**
     * Show this popup on the map
     */
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

    /**
     * Add a node to the database
     * @param nodeID Node id of the node
     * @param x x coordinate of the node in pixels
     * @param y y coordinate of the node in pixels
     * @param floor Floor of the hospital the node is on
     * @param building Building that the node is in
     * @param type Type of the node
     * @param longName Long name of the node
     * @param shortName short name of the node
     */
    public void addNode(String nodeID, int x, int y, String floor, String building, String type, String longName, String shortName){

        Node aNode = new Node(nodeID, x, y, floor, building, type, longName, shortName);

        try {
            DatabaseHandler.getDatabaseHandler("main.db").addNode(aNode);
        } catch (SQLException e) { e.printStackTrace(); }

        // Refresh map editor
        data.getMd().refreshEditor();
        data.getMd().removeAllPopups();
    }

    /**
     * Remove the popup from the map.
     */
    @Override
    public void hide() {
        super.hide();
    }
}

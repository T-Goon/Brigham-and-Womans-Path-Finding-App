package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class DelNodeAYSWindow extends Window<VBox, NodeMenuPopupData, VBox> {


    public DelNodeAYSWindow(Pane parent, NodeMenuPopupData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Delete a node
     */
    public void delNode(){
        try {
            DatabaseHandler.getDatabaseHandler("main.db").removeNode(data.getNodeID());
        } catch (SQLException e) { e.printStackTrace(); }

        data.getMd().removeAllPopups();
        data.getMd().refreshEditor();
    }

    /**
     * Show popup
     */
    public void show() {

        VBox areYouSureMenu = null;

        // Pass data to window
        App.getPrimaryStage().setUserData(this);

        // Load new window
        try {
            areYouSureMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/delNodeAreYouSure.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(areYouSureMenu);
    }
}

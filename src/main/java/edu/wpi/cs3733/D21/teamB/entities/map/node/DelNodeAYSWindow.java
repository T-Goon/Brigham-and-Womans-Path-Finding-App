package edu.wpi.cs3733.D21.teamB.entities.map.node;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class DelNodeAYSWindow extends Window<VBox, NodeMenuPopupData, VBox> implements Poppable {

    public DelNodeAYSWindow(Pane parent, NodeMenuPopupData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Delete a node
     */
    public void delNode() {
        try {
            DatabaseHandler.getHandler().removeNode(data.getNodeID());
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
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/delNodeAreYouSure.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(areYouSureMenu);
    }
}

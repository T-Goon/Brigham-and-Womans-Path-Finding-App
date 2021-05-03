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

public class DelEdgeBtwnFloorsAYSWindow extends Window<VBox, NodeMenuPopupData, VBox> implements Poppable {

    private final String delID;

    public DelEdgeBtwnFloorsAYSWindow(Pane parent, NodeMenuPopupData data, VBox previous, String id) {
        super(parent, data, previous);
        delID = id;
    }

    /**
     * Show the window
     */
    @Override
    public void show() {
        VBox delEdgeBtwnFloorAYSWindow = null;

        // Pass data to controller
        App.getPrimaryStage().setUserData(this);

        // Load new window
        try {
            delEdgeBtwnFloorAYSWindow = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/delEdgeBtwnFloorsAYSWindow.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(delEdgeBtwnFloorAYSWindow);
    }

    /**
     * Deletes the edge
     */
    public void deleteEdge(){
        try {
            DatabaseHandler.getHandler().removeEdge(delID);
        } catch (SQLException e) { e.printStackTrace(); }

        // Remove popup from map and refresh the nodes
        data.getMd().removeAllPopups();
        data.getMd().refreshEditor();
    }
}

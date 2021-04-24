package edu.wpi.teamB.entities.map.edge;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.MapDrawer;
import edu.wpi.teamB.entities.map.data.DelEdgePopupData;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.Popup.Popup;
import edu.wpi.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class DelEdgeAYSWindow extends Window<VBox, DelEdgePopupData, VBox> {

    public DelEdgeAYSWindow(Pane parent, DelEdgePopupData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Delete an edge and
     */
    public void deleteEdge(){
        try {
            DatabaseHandler.getDatabaseHandler("main.db").removeEdge(
                    data.getStart().getNodeID() + "_" +
                            data.getEnd().getNodeID()
            );
        } catch (SQLException e) { e.printStackTrace(); }

        Graph.getGraph().updateGraph();

        // Remove popup from map and refresh the nodes
        data.getMd().removeAllPopups();
        data.getMd().refreshEditor();
    }

    /**
     * Show this window
     */
    public void show() {

        // Pass data to next window
        App.getPrimaryStage().setUserData(this);

        VBox node = null;
        // Load window
        try{
            node = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgeAreYouSure.fxml")));
        } catch (IOException e){
            e.printStackTrace();
        }

        super.show(node);
    }

    /**
     * Hide this window
     */
    @Override
    public void hide() {
        super.hide();
    }
}

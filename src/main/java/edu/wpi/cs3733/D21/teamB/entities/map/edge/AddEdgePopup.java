package edu.wpi.cs3733.D21.teamB.entities.map.edge;

import edu.wpi.cs3733.D21.teamB.App;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.AddEdgePopupData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;

import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddEdgePopup extends Popup<VBox, AddEdgePopupData> implements Poppable {

    public AddEdgePopup(Pane parent, AddEdgePopupData data) {
        super(parent, data);
    }

    /**
     * Delete an edge and
     */
    public void addEdge() {

        // Do not allow adding an edge that has same start and end node
        if(!data.getMc().getNewEdgeStart().equals(data.getMc().getNewEdgeEnd())) {
            try {

                if (DatabaseHandler.getDatabaseHandler("main.db").getEdges().get(data.getMc().getNewEdgeStart() + "_" +
                        data.getMc().getNewEdgeEnd()) == null)
                    DatabaseHandler.getDatabaseHandler("main.db").addEdge(new Edge(
                            data.getMc().getNewEdgeStart() + "_" +
                                    data.getMc().getNewEdgeEnd(), data.getMc().getNewEdgeStart(), data.getMc().getNewEdgeEnd()
                    ));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Graph.getGraph().updateGraph();
        }

        // Remove popup from map and refresh the nodes
        reset();
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
        try {
            node = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/edgePopup/addEdgeAreYouSure.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(node);
    }

    public void reset() {
        data.getMc().getStartNode().setStroke(Color.BLACK);
        data.getMc().setNewEdgeStart(null);
        data.getMc().setNewEdgeEnd(null);
        data.getMc().setStartNode(null);
    }

    /**
     * Hide this window
     */
    @Override
    public void hide() {
        super.hide();
    }
}

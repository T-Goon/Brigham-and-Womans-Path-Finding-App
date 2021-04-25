package edu.wpi.teamB.entities.map.edge;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.AddEdgePopupData;
import edu.wpi.teamB.entities.map.data.DelEdgePopupData;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.Popup.Poppable;
import edu.wpi.teamB.util.Popup.Popup;
import edu.wpi.teamB.util.Popup.Window;
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
        try {
            System.out.println(data.getMc().getNewEdgeStart() + "_" + data.getMc().getNewEdgeEnd());
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
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/addEdgeAreYouSure.fxml")));
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

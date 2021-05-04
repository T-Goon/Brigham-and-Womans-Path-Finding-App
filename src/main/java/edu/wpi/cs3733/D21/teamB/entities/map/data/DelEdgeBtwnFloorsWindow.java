package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.node.DelEdgeBtwnFloorsAYSWindow;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;
import edu.wpi.cs3733.D21.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class DelEdgeBtwnFloorsWindow extends Window<VBox, NodeMenuPopupData, VBox> implements Poppable {

    private final Map<Integer, String> indexToNodeId = new HashMap<>();

    public DelEdgeBtwnFloorsWindow(Pane parent, NodeMenuPopupData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Shows the window
     */
    @Override
    public void show() {

        VBox delEdgeBtwnFloorWindow = null;

        // Pass data to window
        App.getPrimaryStage().setUserData(this);

        // Load new window
        try {
            delEdgeBtwnFloorWindow = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/delEdgeBtwnFloorsWindow.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(delEdgeBtwnFloorWindow);
    }

    /**
     * Deletes an edge
     */
    public void deleteEdge(int index) {
        parent.getChildren().remove(window);

        String nodeId = indexToNodeId.get(index);
        String edgeID = null;

        // Figure out the correct edge id
        if (DatabaseHandler.getHandler().getEdges().containsKey(data.getNodeID() + "_" + nodeId)) {
            edgeID = data.getNodeID() + "_" + nodeId;
        } else if (DatabaseHandler.getHandler().getEdges().containsKey(nodeId + "_" + data.getNodeID())) {
            edgeID = nodeId + "_" + data.getNodeID();
        }

        // Create and show the are you sure window
        DelEdgeBtwnFloorsAYSWindow delEdgeBtwnFloorsAYSWindow = new DelEdgeBtwnFloorsAYSWindow(
                parent, data, window, edgeID
        );

        delEdgeBtwnFloorsAYSWindow.show();
    }

    /**
     * Gets a list of node names on different floors this node is connected to
     *
     * @return A list of node long names
     */
    public List<String> getEdgesBtwnFloors() {

        DatabaseHandler db = DatabaseHandler.getHandler();

        // Get all edges
        List<Edge> edges = null;
        try {
            edges = db.getAdjacentEdgesOfNode(data.getNodeID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> edgeNames = new ArrayList<>();
        assert edges != null;
        for (Edge e : edges) {
            Node start = db.getNodeById(e.getStartNodeID());
            Node end = db.getNodeById(e.getEndNodeID());

            // Edge goes to a different floor
            if (!start.getFloor().equals(data.getFloor())) {
                edgeNames.add(start.getLongName());
                indexToNodeId.put(edgeNames.size() - 1, start.getNodeID());
            } else if (!end.getFloor().equals(data.getFloor())) {
                edgeNames.add(end.getLongName());
                indexToNodeId.put(edgeNames.size() - 1, end.getNodeID());
            }
        }

        return edgeNames;
    }
}

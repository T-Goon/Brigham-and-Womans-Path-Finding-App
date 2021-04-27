package edu.wpi.cs3733.D21.teamB.entities.map.node;


import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;

import edu.wpi.cs3733.D21.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EditNodeWindow extends Window<VBox, NodeMenuPopupData, VBox> implements Poppable {

    public EditNodeWindow(Pane parent, NodeMenuPopupData data, VBox previous) {
        super(parent, data, previous);
    }

    public void updateNode(int x, int y, String floor, String building, String type, String longName, String shortName, Color color) {

        // if the node types are different, delete and remake so the nodeID is up to date
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
        if (!data.getNodeType().equals(type)) {

            List<String> savedOtherEdgeIDs = new ArrayList<>();
            try {
                List<Edge> edges = db.getAdjacentEdgesOfNode(data.getNodeID());
                for (Edge e : edges) {
                    if (e.getStartNodeID().equals(data.getNodeID()))
                        savedOtherEdgeIDs.add(e.getEndNodeID());
                    else savedOtherEdgeIDs.add(e.getStartNodeID());
                }
                db.removeNode(data.getNodeID());
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            // Figure out what the index should be
            List<Node> nodes;
            // Get all nodes of the same category
            try {
                nodes = db.getNodesByCategory(NodeType.valueOf(type));
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            List<Integer> indexes = new ArrayList<>();
            assert nodes != null;

            // Find the number to append to the end of the id
            nodes.forEach(node -> {
                if (node.getNodeID().startsWith("b"))
                    indexes.add(Integer.parseInt(node.getNodeID().substring(5, 8)));
            });

            Collections.sort(indexes);
            int index = 1;

            for (Integer i : indexes)
                if (i != index++) break;

            // Construct the id
            String aNodeId = "b" + type + String.format("%3s", index).replace(' ', '0') + String.format("%2s", floor).replace(' ', '0');
            Node node = new Node(aNodeId, x, y, floor, building, type, longName, shortName, color);

            // Add the node and all of the edges back
            try {
                db.addNode(node);
                for (String edgeID : savedOtherEdgeIDs) {
                    Edge e = new Edge(node.getNodeID() + "_" + edgeID, node.getNodeID(), edgeID);
                    db.addEdge(e);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            Node node = new Node(
                    data.getNodeID(),
                    x,
                    y,
                    floor,
                    building,
                    type,
                    longName,
                    shortName,
                    color);

            // Update database and graph
            try {
                db.updateNode(node);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

        // Remove popup from map and refresh map nodes
        data.getMd().removeAllPopups();
        data.getMd().refreshEditor();
    }

    public void show() {

        VBox nodeEditMenu = null;

        // Load window
        try {
            nodeEditMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/nodePopup/editNodePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(nodeEditMenu);
    }

    @Override
    public void hide() {
        super.hide();
    }
}

package edu.wpi.cs3733.D21.teamB.entities.map.edge;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.DelEdgePopupData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddNodeFromEdgeWindow extends Window<VBox, DelEdgePopupData, VBox> {

    public AddNodeFromEdgeWindow(Pane parent, DelEdgePopupData data, VBox previous) {
        super(parent, data, previous);
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

        // Delete old edge
        try {
            DatabaseHandler.getHandler().removeEdge(
                    data.getStart().getNodeID() + "_" +
                            data.getEnd().getNodeID()
            );
        } catch (SQLException e) { e.printStackTrace(); }

        // Make the new node
        Node aNode = new Node(nodeID, x, y, floor, building, type, longName, shortName);

        try {
            DatabaseHandler.getHandler().addNode(aNode);
        } catch (SQLException e) { e.printStackTrace(); }

        // Make 2 new edges
        try {
            if (DatabaseHandler.getHandler().getEdges().get(data.getStart().getNodeID() + "_" +
                    nodeID) == null)
                DatabaseHandler.getHandler().addEdge(new Edge(
                        data.getStart().getNodeID() + "_" +
                                nodeID, data.getStart().getNodeID(), nodeID
                ));

            if (DatabaseHandler.getHandler().getEdges().get(nodeID + "_" +
                    data.getEnd().getNodeID()) == null)
                DatabaseHandler.getHandler().addEdge(new Edge(
                        nodeID + "_" +
                                data.getEnd().getNodeID(), nodeID, data.getEnd().getNodeID()
                ));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh map editor
        data.getMd().refreshEditor();
        data.getMd().removeAllPopups();
    }

    public void show(){
        VBox addNodeMenu = null;

        // Load window
        try {
            addNodeMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/edgePopup/addNodeFromEdge.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(addNodeMenu);
    }
}

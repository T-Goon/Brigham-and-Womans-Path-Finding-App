package edu.wpi.teamB.entities.map.node;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.entities.map.data.NodeMenuPopupData;
import edu.wpi.teamB.util.Popup.Poppable;
import edu.wpi.teamB.util.Popup.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddEdgeWindow extends Window<VBox, NodeMenuPopupData, VBox> implements Poppable {

    public AddEdgeWindow(Pane parent, NodeMenuPopupData data, VBox previous) {
        super(parent, data, previous);
    }

    /**
     * Show this window in the popup
     */
    public void show() {

        VBox addEdgeMenu = null;

        // Load window
        try {
            addEdgeMenu = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/addEdgePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.show(addEdgeMenu);
    }

    /**
     * Set the start node when adding an edge
     */
    public void setStart(){
        // Set current start back to black
        String start = data.getMc().getNewEdgeStart();
        Circle startC = data.getMc().getStartNode();
        if (start != null && startC != null) {
            startC.setStroke(Color.BLACK);
        }

        // Set data for the start node
        data.getMc().setNewEdgeStart( data.getNodeID() );
        data.getMc().setStartNode( data.getCircle() );

        // Set color of circle to red
        data.getCircle().setStroke(Color.RED);
    }

    /**
     * Set the end node when adding an edge
     */
    public void setEnd(){
        // Set current start back to black
        String end = data.getMc().getNewEdgeEnd();
        Circle endC = data.getMc().getEndNode();

        if (end != null && endC != null) {
            endC.setStroke(Color.BLACK);
        }

        data.getMc().setNewEdgeEnd(data.getNodeID());
        data.getMc().setEndNode(data.getCircle());

        data.getCircle().setStroke(Color.GREEN);
    }

    /**
     * Add an edge
     */
    public void addEdge() {
        String startNodeName = data.getMc().getNewEdgeStart();
        String endNodeName = data.getMc().getNewEdgeEnd();

        if (startNodeName == null || endNodeName == null || startNodeName.equals(endNodeName)) return;

        String edgeIdentifier = startNodeName + "_" + endNodeName;

        Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);

        // Update database and graph
        try {
            DatabaseHandler.getDatabaseHandler("main.db").addEdge(edge);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Remove popup from map and refresh map nodes
        data.getMd().removeAllPopups();
        data.getMd().refreshEditor();
    }

    /**
     * Reset the edges picked
     */
    public void resetEdges() {
        // Reset Node Colors
        if(data.getMc().getStartNode() != null)
            data.getMc().getStartNode().setStroke(Color.BLACK);

        if(data.getMc().getEndNode() != null)
            data.getMc().getEndNode().setStroke(Color.BLACK);

        data.getMc().setStartNode(null);
        data.getMc().setEndNode(null);

        // Reset start and end nodes
        data.getMc().setNewEdgeStart(null);
        data.getMc().setNewEdgeEnd(null);
    }
}

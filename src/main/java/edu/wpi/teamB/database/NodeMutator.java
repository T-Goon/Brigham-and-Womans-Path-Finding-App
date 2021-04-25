package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.pathfinding.Graph;

import java.sql.SQLException;

public class NodeMutator implements IDatabaseEntityMutator<Node> {

    private final DatabaseHandler db;

    public NodeMutator(DatabaseHandler db) {
        this.db = db;
    }

    /**
     * Adds an node to the database with the given information
     *
     * @param node the node to add
     */
    public void addEntity(Node node) throws SQLException {
        String query = "INSERT INTO Nodes VALUES " +
                "('" + node.getNodeID()
                + "', " + node.getXCoord()
                + ", " + node.getYCoord()
                + ", '" + node.getFloor()
                + "', '" + node.getBuilding()
                + "', '" + node.getNodeType()
                + "', '" + node.getLongName()
                + "', '" + node.getShortName()
                + "')";
        db.runStatement(query, false);
        Graph.getGraph().updateGraph();
    }

    /**
     * Removes the node given by the node ID.
     *
     * @param nodeID the node to remove, given by the node ID
     */
    public void removeEntity(String nodeID) throws SQLException {
        String edgesQuery = "DELETE FROM Edges WHERE startNode = '" + nodeID + "' OR endNode = '" + nodeID + "'";
        String nodeQuery = "DELETE FROM Nodes WHERE nodeID = '" + nodeID + "'";
        db.runStatement(edgesQuery, false);
        db.runStatement(nodeQuery, false);
        Graph.getGraph().updateGraph();
    }

    /**
     * Updates the node with the specified ID.
     *
     * @param node the node to update
     */
    public void updateEntity(Node node) throws SQLException {
        String query = "UPDATE Nodes SET xcoord = " + node.getXCoord()
                + ", ycoord = " + node.getYCoord()
                + ", floor = '" + node.getFloor()
                + "', building = '" + node.getBuilding()
                + "', nodeType = '" + node.getNodeType()
                + "', longName = '" + node.getLongName()
                + "', shortName = '" + node.getShortName()
                + "' WHERE nodeID = '" + node.getNodeID() + "'";
        db.runStatement(query, false);
        Graph.getGraph().updateGraph();
    }
}

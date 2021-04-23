package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.map.Node;
import edu.wpi.teamB.pathfinding.Graph;

import java.sql.SQLException;
import java.sql.Statement;

public class NodeMutator implements IDatabaseEntityMutator<Node> {

    /**
     * Adds an node to the database with the given information
     *
     * @param node the node to add
     */
    public void addEntity(Node node) {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();

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

        try {
            assert statement != null;
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();
    }

    /**
     * Removes the node given by the node ID.
     *
     * @param nodeID the node to remove, given by the node ID
     */
    public void removeEntity(String nodeID) {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String edgesQuery = "DELETE FROM Edges WHERE startNode = '" + nodeID + "' OR endNode = '" + nodeID + "'";
        String nodeQuery = "DELETE FROM Nodes WHERE nodeID = '" + nodeID + "'";

        try {
            assert statement != null;
            statement.execute(edgesQuery);
            statement.execute(nodeQuery);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Graph.getGraph().updateGraph();
    }

    /**
     * Updates the node with the specified ID.
     *
     * @param node the node to update
     */
    public void updateEntity(Node node) {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();

        String query = "UPDATE Nodes SET xcoord = " + node.getXCoord()
                + ", ycoord = " + node.getYCoord()
                + ", floor = " + node.getFloor()
                + ", building = '" + node.getBuilding()
                + "', nodeType = '" + node.getNodeType()
                + "', longName = '" + node.getLongName()
                + "', shortName = '" + node.getShortName()
                + "' WHERE nodeID = '" + node.getNodeID() + "'";

        try {
            // If no rows are updated, then the node ID is not in the table
            assert statement != null;
            if (statement.executeUpdate(query) == 0)
                System.err.println("Node ID does not exist in the table!");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();
    }
}

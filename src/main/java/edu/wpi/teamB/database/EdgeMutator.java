package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.map.Edge;
import edu.wpi.teamB.pathfinding.Graph;

import java.sql.SQLException;
import java.sql.Statement;

public class EdgeMutator implements IDatabaseEntityMutator<Edge> {

    /**
     * Adds an edge to the database with the given information
     *
     * @param edge the edge to add
     */
    public void addEntity(Edge edge) {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();

        String query = "INSERT INTO Edges VALUES ('" + edge.getEdgeID() + "', '" + edge.getStartNodeID() + "', '" + edge.getEndNodeID() + "')";

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
     * Removes the edge given by the edge ID.
     *
     * @param edgeID the edge to remove, given by the edge ID
     */
    public void removeEntity(String edgeID) {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String query = "DELETE FROM Edges WHERE edgeID = '" + edgeID + "'";

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
     * Updates the edge with the specified ID.
     *
     * @param edge the edge to update
     */
    public void updateEntity(Edge edge) {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String query = "UPDATE Edges SET startNode = '" + edge.getStartNodeID() + "', endNode = '" + edge.getEndNodeID() + "' WHERE edgeID = '" + edge.getEdgeID() + "'";

        try {
            // If no rows are updated, then the edge ID is not in the table
            assert statement != null;
            if (statement.executeUpdate(query) == 0)
                System.err.println("Edge ID does not exist in the table!");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();
    }
}

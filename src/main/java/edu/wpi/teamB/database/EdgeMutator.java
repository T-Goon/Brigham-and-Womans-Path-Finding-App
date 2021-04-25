package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.pathfinding.Graph;

import java.sql.SQLException;

public class EdgeMutator implements IDatabaseEntityMutator<Edge> {

    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    /**
     * Adds an edge to the database with the given information
     *
     * @param edge the edge to add
     */
    public void addEntity(Edge edge) throws SQLException {
        String query = "INSERT INTO Edges VALUES ('" + edge.getEdgeID() + "', '" + edge.getStartNodeID() + "', '" + edge.getEndNodeID() + "')";
        db.runStatement(query, false);
        Graph.getGraph().updateGraph();
    }

    /**
     * Removes the edge given by the edge ID.
     *
     * @param edgeID the edge to remove, given by the edge ID
     */
    public void removeEntity(String edgeID) throws SQLException {
        String query = "DELETE FROM Edges WHERE edgeID = '" + edgeID + "'";
        db.runStatement(query, false);
        Graph.getGraph().updateGraph();
    }

    /**
     * Updates the edge with the specified ID.
     *
     * @param edge the edge to update
     */
    public void updateEntity(Edge edge) throws SQLException {
        String query = "UPDATE Edges SET startNode = '" + edge.getStartNodeID() + "', endNode = '" + edge.getEndNodeID() + "' WHERE edgeID = '" + edge.getEdgeID() + "'";
        db.runStatement(query, false);
        Graph.getGraph().updateGraph();
    }
}

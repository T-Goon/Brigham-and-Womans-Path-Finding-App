package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EdgeMutator implements IDatabaseEntityMutator<Edge> {

    private final DatabaseHandler db;

    public EdgeMutator(DatabaseHandler db) {
        this.db = db;
    }

    /**
     * Displays the list of edges along with their attributes.
     *
     * @return a map of edge IDs to actual edges
     */
    public Map<String, Edge> getEdges() {
        String query = "SELECT * FROM Edges";
        try {
            ResultSet rs = db.runStatement(query, true);
            Map<String, Edge> edges = new HashMap<>();
            if (rs == null) return edges;
            do {
                Edge outEdge = new Edge(
                        rs.getString("edgeID").trim(),
                        rs.getString("startNode").trim(),
                        rs.getString("endNode").trim()
                );
                edges.put(rs.getString("edgeID").trim(), outEdge);
            } while (rs.next());
            rs.close();
            return edges;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Adds an edge to the database with the given information
     *
     * @param edge the edge to add
     */
    public void addEntity(Edge edge) throws SQLException {
        String query = "INSERT INTO Edges VALUES ('" + edge.getEdgeID() + "', '" + edge.getStartNodeID() + "', '" + edge.getEndNodeID() + "')";
        db.runStatement(query, false);
    }

    /**
     * Removes the edge given by the edge ID.
     *
     * @param edgeID the edge to remove, given by the edge ID
     */
    public void removeEntity(String edgeID) throws SQLException {
        String query = "DELETE FROM Edges WHERE edgeID = '" + edgeID + "'";
        db.runStatement(query, false);
    }

    /**
     * Updates the edge with the specified ID.
     *
     * @param edge the edge to update
     */
    public void updateEntity(Edge edge) throws SQLException {
        String query = "UPDATE Edges SET startNode = '" + edge.getStartNodeID() + "', endNode = '" + edge.getEndNodeID() + "' WHERE edgeID = '" + edge.getEdgeID() + "'";
        db.runStatement(query, false);
    }
}

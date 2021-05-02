package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import javafx.scene.paint.Color;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeMutator implements IDatabaseEntityMutator<Node> {

    private final DatabaseHandler db;

    public NodeMutator(DatabaseHandler db) {
        this.db = db;
    }

    /**
     * Displays the list of nodes along with their attributes.
     *
     * @return a map of node IDs to actual nodes
     */
    public Map<String, Node> getNodes() {
        String query = "SELECT * FROM Nodes";
        try {
            ResultSet rs = db.runStatement(query, true);
            Map<String, Node> nodes = new HashMap<>();
            if (rs == null) return nodes;
            do {
                Node outNode = new Node(
                        rs.getString("NodeID").trim(),
                        rs.getInt("xcoord"),
                        rs.getInt("ycoord"),
                        rs.getString("floor"),
                        rs.getString("building").trim(),
                        rs.getString("nodeType").trim(),
                        rs.getString("longName").trim(),
                        rs.getString("shortName").trim(),
                        Color.web(rs.getString("color").trim())
                );
                nodes.put(rs.getString("NodeID").trim(), outNode);
            } while (rs.next());
            rs.close();
            return nodes;
        } catch (SQLException e) {
            return null;
        }
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
                + "', '" + node.getColor().toString()
                + "')";
        db.runStatement(query, false);
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
                + "', color = '" + node.getColor().toString()
                + "' WHERE nodeID = '" + node.getNodeID() + "'";
        db.runStatement(query, false);
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
    }

    /**
     * Given a node ID, returns a list of all the edges that are adjacent to that node.
     *
     * @param nodeID the node ID of the node to get the adjacent edges of
     * @return the list of all adjacent edges of that node
     */
    public List<Edge> getAdjacentEdgesOfNode(String nodeID) throws SQLException {
        String query = "SELECT DISTINCT * FROM Edges WHERE startNode = '" + nodeID + "' OR endNode = '" + nodeID + "'";
        List<Edge> edges = new ArrayList<>();
        ResultSet set = db.runStatement(query, true);
        do {
            Edge outEdge = new Edge(
                    set.getString("edgeID").trim(),
                    set.getString("startNode").trim(),
                    set.getString("endNode").trim()
            );
            edges.add(outEdge);
        } while (set.next());
        set.close();
        return edges;
    }

    /**
     * Retrieves a list of nodes from the database based on the given nodeType
     *
     * @param rest NodeType
     * @return List of nodes with the given node type
     */
    public List<Node> getNodesByCategory(NodeType rest) throws SQLException {
        String query = "SELECT * FROM Nodes WHERE nodeType = '" + rest.toString() + "'";
        ResultSet rs = db.runStatement(query, true);
        List<Node> nodes = new ArrayList<>();
        do {
            Node outNode = new Node(
                    rs.getString("NodeID").trim(),
                    rs.getInt("xcoord"),
                    rs.getInt("ycoord"),
                    rs.getString("floor"),
                    rs.getString("building").trim(),
                    rs.getString("nodeType").trim(),
                    rs.getString("longName").trim(),
                    rs.getString("shortName").trim(),
                    Color.web(rs.getString("color").trim())
            );
            nodes.add(outNode);
        } while (rs.next());
        rs.close();
        return nodes;
    }

    /**
     * Get specific node information from the database given the node's ID
     *
     * @param ID requested node ID
     * @return Node object with data from database
     */
    public Node getNodeById(String ID) {
        try {
            String query = "SELECT * FROM Nodes WHERE nodeID = '" + ID + "'";
            ResultSet set = db.runStatement(query, true);
            if (set == null) return null;
            Node n = new Node(
                    set.getString("nodeID").trim(),
                    set.getInt("xcoord"),
                    set.getInt("ycoord"),
                    set.getString("floor").trim(),
                    set.getString("building").trim(),
                    set.getString("nodeType").trim(),
                    set.getString("longName").trim(),
                    set.getString("shortName").trim(),
                    Color.web(set.getString("color").trim())
            );
            set.close();
            return n;
        } catch (SQLException e) {
            return null;
        }
    }
}

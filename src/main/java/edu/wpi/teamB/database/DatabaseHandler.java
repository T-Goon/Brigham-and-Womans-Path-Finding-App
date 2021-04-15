package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.Graph;

import java.sql.*;
import java.util.*;

public class DatabaseHandler {

    private static final String URL_BASE = "jdbc:sqlite:";

    private String databaseURL;
    private Connection databaseConnection;

    // Singleton
    private static DatabaseHandler handler;

    private DatabaseHandler() {
    }

    /**
     * @param dbURL the path to the database (should default to "main.db")
     * @return the database handler
     */
    public static DatabaseHandler getDatabaseHandler(String dbURL) {
        if (handler == null) {
            handler = new DatabaseHandler();
            handler.databaseURL = URL_BASE + dbURL;
            handler.databaseConnection = handler.getConnection();
        } else if (!(URL_BASE + dbURL).equals(handler.databaseURL)) {
            // If switching between main.db and test.db, shut down the old database and start
            handler.shutdown();
            handler = new DatabaseHandler();
            handler.databaseURL = URL_BASE + dbURL;
            handler.databaseConnection = handler.getConnection();
        }
        return handler;
    }

    public Connection getConnection() {
        if (this.databaseConnection == null) {
            try {
                this.databaseConnection = DriverManager.getConnection(this.databaseURL);
                System.out.println("Connected to DB using " + this.databaseConnection.getMetaData().getDriverName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.databaseConnection;
    }

    private Statement getStatement() {
        try {
            return this.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Given the list of edges and nodes, clear and fill the database
     * with that data.
     */
    public void loadDatabase(List<Node> nodes, List<Edge> edges) {
        loadDatabaseNodes(nodes);
        loadDatabaseEdges(edges);
    }

    /**
     * Refreshes the nodes table and loads the nodes given into
     * the database
     *
     * @param nodes the list of nodes
     * @throws SQLException if the query is malformed
     */
    public void loadDatabaseNodes(List<Node> nodes) {

        Statement statement = this.getStatement();
        // Drop tables if they exist already
        String query;
        try {
            query = "DROP TABLE Nodes";
            assert statement != null;
            statement.execute(query);
        } catch (SQLException ignored) {
        }

        try {
            // Create the tables
            query = "CREATE TABLE Nodes("
                    + "nodeID CHAR(20) PRIMARY KEY, "
                    + "xcoord INT, "
                    + "ycoord INT, "
                    + "floor CHAR(20), "
                    + "building CHAR(20), "
                    + "nodeType CHAR(20), "
                    + "longName CHAR(50), "
                    + "shortName CHAR(20))";
            statement.execute(query);

            // If either list is empty, then nothing should be put in
            if (nodes == null) return;
            for (Node node : nodes) {
                query = "INSERT INTO Nodes(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) " +
                        "VALUES('"
                        + node.getNodeID() + "', "
                        + node.getXCoord() + ", "
                        + node.getYCoord() + ", '"
                        + node.getFloor() + "', '"
                        + node.getBuilding() + "', '"
                        + node.getNodeType() + "', '"
                        + node.getLongName() + "', '"
                        + node.getShortName() + "')";
                statement.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the edges table and loads the edges given into
     * the database
     *
     * @param edges the list of edges
     * @throws SQLException if the query is malformed
     */
    public void loadDatabaseEdges(List<Edge> edges) {

        Statement statement = this.getStatement();
        // Drop tables if they exist already
        String query;
        try {
            query = "DROP TABLE Edges";
            statement.execute(query);
        } catch (SQLException ignored) {
        }

        try {
            query = "CREATE TABLE Edges("
                    + "edgeID CHAR(30) PRIMARY KEY, "
                    + "startNode CHAR(20) NOT NULL, "
                    + "endNode CHAR(20) NOT NULL CHECK (startNode != endNode), "
                    + "FOREIGN KEY (startNode) REFERENCES Nodes(nodeID), "
                    + "FOREIGN KEY (endNode) REFERENCES Nodes(nodeID))";
            statement.execute(query);

            // If either list is empty, then nothing should be put in
            if (edges == null) return;
            for (Edge edge : edges) {
                query = "INSERT INTO Edges(edgeID, startNode, endNode) "
                        + "VALUES('"
                        + edge.getEdgeID() + "', '"
                        + edge.getStartNodeID() + "', '"
                        + edge.getEndNodeID() + "')";
                statement.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get specific node information from the database given the node's ID
     *
     * @param ID requested node ID
     * @return Node object with data from database
     */
    public Node getNodeById(String ID) {
        Statement statement = this.getStatement();

        String query = "SELECT * FROM Nodes WHERE nodeID = '" + ID + "'";
        assert statement != null;
        try {
            ResultSet rs = statement.executeQuery(query);
            return new Node(
                    rs.getString("nodeID").trim(),
                    rs.getInt("xcoord"),
                    rs.getInt("ycoord"),
                    rs.getString("floor"),
                    rs.getString("building").trim(),
                    rs.getString("nodeType").trim(),
                    rs.getString("longName").trim(),
                    rs.getString("shortName").trim()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Displays the list of nodes along with their attributes.
     * @return a map of node IDs to actual nodes
     */
    public Map<String, Node> getNodes() {
        Statement statement = this.getStatement();
        String query = "SELECT * FROM Nodes";
        assert statement != null;
        try {
            ResultSet rs = statement.executeQuery(query);
            Map<String, Node> nodes = new HashMap<>();
            while (rs.next()) {
                Node outNode = new Node(
                        rs.getString("NodeID").trim(),
                        rs.getInt("xcoord"),
                        rs.getInt("ycoord"),
                        rs.getString("floor"),
                        rs.getString("building").trim(),
                        rs.getString("nodeType").trim(),
                        rs.getString("longName").trim(),
                        rs.getString("shortName").trim()
                );
                nodes.put(rs.getString("NodeID").trim(), outNode);
            }
            return nodes;
        } catch (SQLException ignored) {
            return null;
        }
    }

    /**
     * Displays the list of edges along with their attributes.
     * @return a map of edge IDs to actual edges
     */
    public Map<String, Edge> getEdges() {
        Statement statement = this.getStatement();

        String query = "SELECT * FROM Edges";
        assert statement != null;
        try {
            ResultSet rs = statement.executeQuery(query);
            Map<String, Edge> edges = new HashMap<>();
            while (rs.next()) {
                Edge outEdge = new Edge(
                        rs.getString("edgeID").trim(),
                        rs.getString("startNode").trim(),
                        rs.getString("endNode").trim()
                );
                edges.put(rs.getString("edgeID").trim(), outEdge);
            }
            return edges;
        } catch (SQLException ignored) {
            return null;
        }
    }

    /**
     * Adds an node to the database with the given information
     *
     * @param node the node to add
     */
    public void addNode(Node node) {
        Statement statement = this.getStatement();

        String query = "INSERT INTO Nodes VALUES " +
                "('" + node.getNodeID()
                + "', " + node.getXCoord()
                + ", " + node.getYCoord()
                + ", " + node.getFloor()
                + ", '" + node.getBuilding()
                + "', '" + node.getNodeType()
                + "', '" + node.getLongName()
                + "', '" + node.getShortName()
                + "')";

        try {
            assert statement != null;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();
    }

    /**
     * Adds an edge to the database with the given information
     *
     * @param edge the edge to add
     */
    public void addEdge(Edge edge) {
        Statement statement = this.getStatement();

        String query = "INSERT INTO Edges VALUES ('" + edge.getEdgeID() + "', '" + edge.getStartNodeID() + "', '" + edge.getEndNodeID() + "')";

        try {
            assert statement != null;
            statement.execute(query);
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
    public void updateNode(Node node) {
        Statement statement = this.getStatement();

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
    public void updateEdge(Edge edge) {
        Statement statement = this.getStatement();
        String query = "UPDATE Edges SET startNode = '" + edge.getStartNodeID() + "', endNode = '" + edge.getEndNodeID() + "' WHERE edgeID = '" + edge.getEdgeID() + "'";

        try {
            // If no rows are updated, then the edge ID is not in the table
            assert statement != null;
            if (statement.executeUpdate(query) == 0)
                System.err.println("Edge ID does not exist in the table!");
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
    public void removeNode(String nodeID) {
        Statement statement = this.getStatement();
        String nodeQuery = "DELETE FROM Nodes WHERE nodeID = '" + nodeID + "'";
        String edgesQuery = "DELETE FROM Edges WHERE startNode = '" + nodeID + "' OR endNode = '" + nodeID + "'";

        try {
            assert statement != null;
            statement.execute(nodeQuery);
            statement.execute(edgesQuery);
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
    public void removeEdge(String edgeID) {
        Statement statement = this.getStatement();
        String query = "DELETE FROM Edges WHERE edgeID = '" + edgeID + "'";

        try {
            assert statement != null;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Graph.getGraph().updateGraph();
    }

    /**
     * Given a node ID, returns a list of all the edges that are adjacent to that node.
     *
     * @param nodeID the node ID of the node to get the adjacent edges of
     * @return the list of all adjacent edges of that node
     */
    public List<Edge> getAdjacentEdgesOfNode(String nodeID) {
        Statement statement = this.getStatement();
        String query = "SELECT DISTINCT * FROM Edges WHERE startNode = '" + nodeID + "' OR endNode = '" + nodeID + "'";
        List<Edge> edges = new ArrayList<>();

        try {
            assert statement != null;
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                Edge outEdge = new Edge(
                        set.getString("edgeID").trim(),
                        set.getString("startNode").trim(),
                        set.getString("endNode").trim()
                );
                edges.add(outEdge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return edges;
    }

    /**
     * @return whether the nodes table is initialized or not
     */
    public boolean isNodesInitialized() {
        return getNodes() != null;
    }

    /**
     * @return whether the edges table is initialized or not
     */
    public boolean isEdgesInitialized() {
        return getEdges() != null;
    }

    /**
     * Shutdown the database
     */
    public void shutdown() {
        // Shutdown the database
        try {
            this.getConnection().close();
            System.out.println("Database is closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package edu.wpi.teamB.database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHandler {

    private static final String URL_BASE = "jdbc:sqlite:src/main/resources/edu/wpi/teamB/database/";

    private String databaseURL;
    private Connection databaseConnection;

    // Singleton
    private static final DatabaseHandler handler = new DatabaseHandler();

    private DatabaseHandler() {
    }

    public static DatabaseHandler getDatabaseHandler(String dbURL) {
        handler.databaseURL = URL_BASE + dbURL;
        handler.databaseConnection = handler.getConnection();
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
    public void fillDatabase(List<Edge> edges, List<Node> nodes) throws SQLException {

        Statement statement = this.getStatement();
        // Drop tables if they exist already
        String query;
        try {
            query = "DROP TABLE Nodes";
            statement.execute(query);
            query = "DROP TABLE Edges";
            statement.execute(query);
        } catch (SQLException ignored) {
        }

        // Create the tables
        query = "CREATE TABLE Nodes("
                + "nodeID CHAR(20), "
                + "xcoord INT, "
                + "ycoord INT, "
                + "floor INT, "
                + "building CHAR(20), "
                + "nodeType CHAR(20), "
                + "longName CHAR(50), "
                + "shortName CHAR(20))";
        statement.execute(query);

        for (Node node : nodes) {
            query = "INSERT INTO Nodes(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) " +
                    "VALUES('"
                    + node.getNodeID() + "', "
                    + node.getXCoord() + ", "
                    + node.getYCoord() + ", "
                    + node.getFloor() + ", '"
                    + node.getBuilding() + "', '"
                    + node.getNodeType() + "', '"
                    + node.getLongName() + "', '"
                    + node.getShortName() + "')";
            statement.execute(query);
        }

        query = "CREATE TABLE Edges("
                + "edgeID CHAR(30), "
                + "startNode CHAR(20), "
                + "endNode CHAR(20))";
        statement.execute(query);

        for (Edge edge : edges) {
            query = "INSERT INTO Edges(edgeID, startNode, endNode) "
                    + "VALUES('"
                    + edge.getEdgeID() + "', '"
                    + edge.getStartNodeName() + "', '"
                    + edge.getEndNodeName() + "')";
            statement.execute(query);
        }
    }

    /**
     * Displays the list of nodes along with their attributes.
     */
    public List<Node> getNodeInformation() throws SQLException {

        Statement statement = this.getStatement();

        String query = "SELECT * FROM Nodes";
        ResultSet rs = statement.executeQuery(query);
        LinkedList<Node> nodes = new LinkedList<Node>();
        while (rs.next()) {
            Node outNode = new Node(
                    rs.getString("NodeID").trim(),
                    rs.getInt("xcoord"),
                    rs.getInt("ycoord"),
                    rs.getInt("floor"),
                    rs.getString("building").trim(),
                    rs.getString("nodeType").trim(),
                    rs.getString("longName").trim(),
                    rs.getString("shortName").trim()
            );
            nodes.add(outNode);
        }
        return nodes;
    }

    /**
     * Displays the list of edges along with their attributes.
     */
    public List<Edge> getEdgeInformation() throws SQLException {
        Statement statement = this.getStatement();

        String query = "SELECT edgeID, startNode, endNode FROM Edges";
        ResultSet rs = statement.executeQuery(query);
        LinkedList<Edge> edges = new LinkedList<Edge>();
        while (rs.next()) {
            Edge outEdge = new Edge(
                    rs.getString("edgeID").trim(),
                    rs.getString("startNode").trim(),
                    rs.getString("endNode").trim()
            );
            edges.add(outEdge);
        }

        return edges;
    }

    /**
     * Updates the node with the specified ID.
     */
    public void updateNode(String nodeID, int xcoord, int ycoord, int floor, String building, String nodeType, String longName, String shortName) {

        Statement statement = this.getStatement();

        String query = "UPDATE Nodes SET xcoord = " + xcoord
                + ", ycoord = " + ycoord
                + ", floor = " + floor
                + ", building = '" + building
                + "', nodeType = '" + nodeType
                + "', longName = '" + longName
                + "', shortName = '" + shortName
                + "' WHERE nodeID = '" + nodeID + "'";

        try {
            // If no rows are updated, then the node ID is not in the table
            if (statement.executeUpdate(query) == 0)
                System.err.println("Node ID does not exist in the table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the edge with the specified ID.
     */
    public void updateEdge(String edgeID, String startNode, String endNode) {

        Statement statement = this.getStatement();

        String query = "UPDATE Edges SET startNode = '" + startNode + "', endNode = '" + endNode + "' WHERE edgeID = '" + edgeID + "'";

        try {
            // If no rows are updated, then the edge ID is not in the table
            if (statement.executeUpdate(query) == 0)
                System.err.println("Edge ID does not exist in the table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

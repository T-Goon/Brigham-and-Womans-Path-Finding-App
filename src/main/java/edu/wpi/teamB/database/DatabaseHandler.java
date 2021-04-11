package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHandler {

    private static final String URL_BASE = "jdbc:sqlite:src/main/resources/edu/wpi/teamB/database/";

    private String databaseURL;
    private Connection databaseConnection;

    // Singleton
    private static DatabaseHandler handler;

    private DatabaseHandler() {
    }

    /**
     * @param dbURL the path to the database (should default to "main.db"
     * @return the database handler
     */
    public static DatabaseHandler getDatabaseHandler(String dbURL) {
        if (handler == null) {
            handler = new DatabaseHandler();
            handler.databaseURL = URL_BASE + dbURL;
            handler.databaseConnection = handler.getConnection();
        } else if (!(URL_BASE + dbURL).equals(handler.databaseURL))
            throw new IllegalArgumentException("Trying to change where the database handler points!");
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
    public void fillDatabase(List<Node> nodes, List<Edge> edges) throws SQLException {

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
        LinkedList<Node> nodes = new LinkedList<>();
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
        LinkedList<Edge> edges = new LinkedList<>();
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
            if (statement.executeUpdate(query) == 0)
                System.err.println("Node ID does not exist in the table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the edge with the specified ID.
     */
    public void updateEdge(Edge edge) {
        Statement statement = this.getStatement();
        String query = "UPDATE Edges SET startNode = '" + edge.getStartNodeName() + "', endNode = '" + edge.getEndNodeName() + "' WHERE edgeID = '" + edge.getEdgeID() + "'";

        try {
            // If no rows are updated, then the edge ID is not in the table
            if (statement.executeUpdate(query) == 0)
                System.err.println("Edge ID does not exist in the table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isInitialized() {
        try {
            // Try getting a list of the tables -- If there is a table there, it will return true
            if (handler.getConnection().getMetaData().getTables(null, null, "%", new String[]{"TABLES"}).next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
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

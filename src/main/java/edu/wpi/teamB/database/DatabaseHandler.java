package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.map.Edge;
import edu.wpi.teamB.entities.map.Node;
import edu.wpi.teamB.entities.requests.*;
import edu.wpi.teamB.pathfinding.Graph;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.*;

public class DatabaseHandler {

    private static final String URL_BASE = "jdbc:sqlite:";

    private String databaseURL;
    private Connection databaseConnection;

    // Singleton
    private static DatabaseHandler handler;

    //State
    private static User AuthenticationUser = new User(null, null, null, User.AuthenticationLevel.GUEST, null);

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
    public void loadNodesEdges(List<Node> nodes, List<Edge> edges) {
        resetDatabase(new ArrayList<>(Arrays.asList("Nodes", "Edges")));
        executeSchema();
        loadDatabaseNodes(nodes);
        loadDatabaseEdges(edges);
    }

    public void resetDatabase(List<String> tables) {
        Statement statement = this.getStatement();

        if (tables.isEmpty()) {
            tables.add("SanitationRequests");
            tables.add("MedicineRequests");
            tables.add("InternalTransportRequests");
            tables.add("ReligiousRequests");
            tables.add("FoodRequests");
            tables.add("FloralRequests");
            tables.add("SecurityRequests");
            tables.add("ExternalTransportRequests");
            tables.add("LaundryRequests");
            tables.add("CaseManagerRequests");
            tables.add("SocialWorkerRequests");
            tables.add("Requests");
            tables.add("Edges");
            tables.add("Nodes");
            tables.add("Jobs");
            tables.add("Users");
        }

        List<String> queries = new LinkedList<>();
        for (String table : tables) {
            queries.add("DROP TABLE IF EXISTS " + table);
        }

        try {
            assert statement != null;
            for (String query : queries) {
                statement.execute(query);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeSchema() {
        Statement statement = this.getStatement();
        String configuration = "PRAGMA foreign_keys = ON";

        String nodesTable = "CREATE TABLE IF NOT EXISTS Nodes("
                + "nodeID CHAR(20) PRIMARY KEY, "
                + "xcoord INT, "
                + "ycoord INT, "
                + "floor CHAR(20), "
                + "building CHAR(20), "
                + "nodeType CHAR(20), "
                + "longName CHAR(50), "
                + "shortName CHAR(20))";

        String edgesTable = "CREATE TABLE IF NOT EXISTS Edges("
                + "edgeID CHAR(30) PRIMARY KEY, "
                + "startNode CHAR(20) NOT NULL, "
                + "endNode CHAR(20) NOT NULL CHECK (startNode != endNode), "
                + "FOREIGN KEY (startNode) REFERENCES Nodes(nodeID), "
                + "FOREIGN KEY (endNode) REFERENCES Nodes(nodeID))";

        String requestsTable = "CREATE TABLE IF NOT EXISTS Requests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "requestType CHAR(30), "
                + "requestDate CHAR(10), " // Stored as YYYY-MM-DD
                + "requestTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "complete CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "employeeName CHAR(30), "
                + "location CHAR(20), "
                + "description VARCHAR(200), "
                + "FOREIGN KEY (location) REFERENCES Nodes(nodeID))";

        String sanitationRequestsTable = "CREATE TABLE IF NOT EXISTS SanitationRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "sanitationType CHAR(20), "
                + "sanitationSize CHAR(20), "
                + "hazardous CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "biologicalSubstance CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "occupied CHAR(1)," // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String medicineRequestsTable = "CREATE TABLE IF NOT EXISTS MedicineRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "medicine CHAR(20),"
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String internalTransportRequestsTable = "CREATE TABLE IF NOT EXISTS InternalTransportRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "transportType CHAR(20), "
                + "unconscious CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "infectious CHAR(1)," // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String religiousRequestsTable = "CREATE TABLE IF NOT EXISTS ReligiousRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "startTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "endTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "religiousDate CHAR(10), " // Stored as YYYY-MM-DD
                + "faith CHAR(20), "
                + "infectious CHAR(1)," // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String foodRequestsTable = "CREATE TABLE IF NOT EXISTS FoodRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "arrivalTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "mealChoice CHAR(20),"
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String floralRequestsTable = "CREATE TABLE IF NOT EXISTS FloralRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "deliveryDate CHAR(10), " // Stored as YYYY-MM-DD
                + "startTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "endTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "wantsRoses CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsTulips CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsDaisies CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsLilies CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsSunflowers CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsCarnations CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsOrchids CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String securityRequestsTable = "CREATE TABLE IF NOT EXISTS SecurityRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "urgency INT, "
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String externalTransportTable = "CREATE TABLE IF NOT EXISTS ExternalTransportRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(20), "
                + "transportType CHAR(20), "
                + "destination CHAR(20), "
                + "patientAllergies CHAR(200), "
                + "outNetwork CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "infectious CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "unconscious CHAR(1)," // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String laundryTable = "CREATE TABLE IF NOT EXISTS LaundryRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "serviceType CHAR(20), "
                + "serviceSize CHAR(20), "
                + "dark CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "light CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "occupied CHAR(1)," // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String caseManagerRequestsTable = "CREATE TABLE IF NOT EXISTS CaseManagerRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "timeForArrival CHAR(20)," // Stored as HH:MM (24 hour time)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String socialWorkerRequestsTable = "CREATE TABLE IF NOT EXISTS SocialWorkerRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "timeForArrival CHAR(20)," // Stored as HH:MM (24 hour time)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String users = "CREATE TABLE IF NOT EXISTS Users("
                + "username CHAR(30) PRIMARY KEY, "
                + "firstName CHAR(30), "
                + "lastName CHAR(30), "
                + "authenticationLevel CHAR(30) CHECK (authenticationLevel in ('ADMIN','STAFF','PATIENT', 'GUEST')), "
                + "passwordHash CHAR(30))";

        String jobs = "CREATE TABLE IF NOT EXISTS Jobs("
                + "username CHAR(30), "
                + "job CHAR(30), "
                + "FOREIGN KEY (username) REFERENCES Users(username))";

        try {
            assert statement != null;
            statement.execute(configuration);
            statement.execute(nodesTable);
            statement.execute(edgesTable);
            statement.execute(requestsTable);
            statement.execute(sanitationRequestsTable);
            statement.execute(medicineRequestsTable);
            statement.execute(internalTransportRequestsTable);
            statement.execute(religiousRequestsTable);
            statement.execute(foodRequestsTable);
            statement.execute(floralRequestsTable);
            statement.execute(securityRequestsTable);
            statement.execute(externalTransportTable);
            statement.execute(laundryTable);
            statement.execute(caseManagerRequestsTable);
            statement.execute(socialWorkerRequestsTable);
            statement.execute(users);
            statement.execute(jobs);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the nodes table and loads the nodes given into
     * the database
     *
     * @param nodes the list of nodes
     */
    public void loadDatabaseNodes(List<Node> nodes) {

        Statement statement = this.getStatement();
        String query;

        try {
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
                assert statement != null;
                statement.execute(query);
                statement.close();
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
     */
    public void loadDatabaseEdges(List<Edge> edges) {

        Statement statement = this.getStatement();
        String query;

        try {
            // If either list is empty, then nothing should be put in
            if (edges == null) return;
            for (Edge edge : edges) {
                query = "INSERT INTO Edges(edgeID, startNode, endNode) "
                        + "VALUES('"
                        + edge.getEdgeID() + "', '"
                        + edge.getStartNodeID() + "', '"
                        + edge.getEndNodeID() + "')";
                assert statement != null;
                statement.execute(query);
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the requests tables and loads the requests given into
     * the database
     *
     * @param requests the list of requests
     */
    public void loadDatabaseRequests(List<Request> requests) {

        if (requests == null) return;
        for (Request request : requests) {
            this.addRequest(request);
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
            rs.next();
            Node n = new Node(
                    rs.getString("nodeID").trim(),
                    rs.getInt("xcoord"),
                    rs.getInt("ycoord"),
                    rs.getString("floor").trim(),
                    rs.getString("building").trim(),
                    rs.getString("nodeType").trim(),
                    rs.getString("longName").trim(),
                    rs.getString("shortName").trim()
            );
            rs.close();
            statement.close();
            return n;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param user     User to add
     * @param password Plaintext password for user
     * @return Whether user has been successfully added
     */
    public boolean addUser(User user, String password) {
        Statement statement = this.getStatement();
        String hash = this.passwordHash(password);

        String query = "INSERT INTO Users VALUES " +
                "('" + user.getUsername()
                + "', '" + user.getFirstName()
                + "', '" + user.getLastName()
                + "', '" + user.getAuthenticationLevel().toString()
                + "', '" + hash
                + "')";

        try {
            assert statement != null;
            statement.execute(query);
            if (user.getJobs() != null) {
                for (Request.RequestType job : user.getJobs()) {
                    query = "INSERT INTO Jobs VALUES " +
                            "('" + user.getUsername()
                            + "', '" + job.toString()
                            + "')";
                    statement.execute(query);
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param username username to query by
     * @return User object with that username, null if no user exists
     */
    public User getUserByUsername(String username) {
        Statement statement = this.getStatement();
        String query = "SELECT job FROM Jobs WHERE (username = '" + username + "')";
        ResultSet rs;
        List<Request.RequestType> jobs = new ArrayList<>();
        User outUser;
        try {
            assert statement != null;
            rs = statement.executeQuery(query);
            while (rs.next()) {
                jobs.add(Request.RequestType.valueOf(rs.getString("job")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "SELECT * FROM Users WHERE (username = '" + username + "')";
        try {
            rs = statement.executeQuery(query);
            outUser = new User(
                    rs.getString("username"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    User.AuthenticationLevel.valueOf(rs.getString("authenticationLevel")),
                    jobs
            );
            rs.close();
            statement.close();
        } catch (SQLException e) {
            return null;
        }
        return outUser;
    }

    public List<User> getUsersByJob(Request.RequestType job) throws IllegalArgumentException {
        Statement statement = this.getStatement();
        String query = "SELECT username FROM " +
                "Users NATURAL JOIN Jobs " +
                "WHERE (job = '" + job.toString() + "')";
        ResultSet rs;
        List<User> outusers = new ArrayList<User>();
        try {
            assert statement != null;
            rs = statement.executeQuery(query);
            while (rs.next()) {
                String username = rs.getString("username");
                outusers.add(this.getUserByUsername(username));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return outusers;
    }

    public boolean updateUser(User newUser) {
        Statement statement = this.getStatement();
        String updateUser = "UPDATE Users " +
                "SET username = '" + newUser.getUsername() + "'," +
                "firstName = '" + newUser.getFirstName() + "'," +
                "lastName = '" + newUser.getLastName() + "'," +
                "authenticationLevel = '" + newUser.getAuthenticationLevel().toString() + "'" +
                "WHERE (username = '" + newUser.getUsername() + "')";
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + newUser.getUsername() + "')";
        try {
            if (this.getUserByUsername(newUser.getUsername()) == null) {
                return false;
            } else {
                statement.execute(updateUser);
                statement.execute(deleteJobs);
                for (Request.RequestType job : newUser.getJobs()) {
                    String query = "INSERT INTO Jobs VALUES " +
                            "('" + newUser.getUsername()
                            + "', '" + job.toString()
                            + "')";
                    statement.execute(query);
                }

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String username) {
        Statement statement = this.getStatement();
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + username + "')";
        String deleteUser = "DELETE FROM Users WHERE (username = '" + username + "')";
        try {
            assert statement != null;
            statement.executeUpdate(deleteJobs);
            int update = statement.executeUpdate(deleteUser);
            statement.close();
            return update == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username Claimed username of authenticator
     * @param password Claimed plaintext password of authenticator
     * @return If authentication is successful, return the User object representing the authenticated user
     */
    public User authenticate(String username, String password) {
        this.deauthenticate();
        Statement statement = this.getStatement();
        String query = "SELECT passwordHash FROM Users WHERE (username = '" + username + "')";
        User outUser = null;
        try {
            assert statement != null;
            ResultSet rs = statement.executeQuery(query);
            if (!rs.next()) return null;
            String storedHash = (rs.getString("passwordHash"));
            if (this.passwordHash(password).equals(storedHash)) {
                outUser = this.getUserByUsername(username);
                rs.close();
                statement.close();
            } else {
                rs.close();
                statement.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseHandler.AuthenticationUser = outUser;
        return outUser;
    }

    /**
     * @param plaintext plaintext password to hash
     * @return hashed password
     */
    public String passwordHash(String plaintext) {
        return String.valueOf(plaintext.hashCode());
    }

    /**
     * Sets authentication level to guest
     *
     * @return if the user successfully lowered their authentication level (false if already guest)
     */
    public boolean deauthenticate() {
        if (DatabaseHandler.AuthenticationUser.getAuthenticationLevel() != User.AuthenticationLevel.GUEST) {
            DatabaseHandler.AuthenticationUser = new User(null, null, null, User.AuthenticationLevel.GUEST, null);
            return true;
        } else return false;
    }

    /*
     * @return the User that is currently logged in
     */
    public User getAuthenticationUser() {
        return DatabaseHandler.AuthenticationUser;
    }

    /**
     * Displays the list of nodes along with their attributes.
     *
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
            statement.close();
            return nodes;
        } catch (SQLException ignored) {
            return null;
        }
    }

    /**
     * Displays the list of edges along with their attributes.
     *
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
            statement.close();
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
    public void updateEdge(Edge edge) {
        Statement statement = this.getStatement();
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

    /**
     * Removes the node given by the node ID.
     *
     * @param nodeID the node to remove, given by the node ID
     */
    public void removeNode(String nodeID) {
        Statement statement = this.getStatement();
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
            statement.close();
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
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return edges;
    }

    /**
     * @return whether the nodes table is initialized or not
     */
    public boolean isInitialized() {
        return !getNodes().isEmpty() && !getEdges().isEmpty();
    }


    // REQUESTS ARE BELOW

    /**
     * Adds a request to Requests and the table specific to the given request
     *
     * @param request the request to add
     */
    public void addRequest(Request request) {
        Statement statement = this.getStatement();
        String query = "INSERT INTO Requests VALUES " +
                "('" + request.getRequestID()
                + "', '" + request.getRequestType()
                + "', '" + request.getDate()
                + "', '" + request.getTime()
                + "', '" + request.getComplete()
                + "', '" + request.getEmployeeName()
                + "', '" + request.getLocation()
                + "', '" + request.getDescription()
                + "')";

        try {
            assert statement != null;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (request.getRequestType()) {
            case SANITATION:
                SanitationRequest sanitationRequest = (SanitationRequest) request;
                query = "INSERT INTO SanitationRequests VALUES " +
                        "('" + sanitationRequest.getRequestID()
                        + "', '" + sanitationRequest.getSanitationType()
                        + "', '" + sanitationRequest.getSanitationSize()
                        + "', '" + (sanitationRequest.getHazardous())
                        + "', '" + (sanitationRequest.getBiologicalSubstance())
                        + "', '" + (sanitationRequest.getOccupied())
                        + "')";
                break;
            case MEDICINE:
                MedicineRequest medicineRequest = (MedicineRequest) request;
                query = "INSERT INTO MedicineRequests VALUES " +
                        "('" + medicineRequest.getRequestID()
                        + "', '" + medicineRequest.getPatientName()
                        + "', '" + medicineRequest.getMedicine()
                        + "')";
                break;
            case INTERNAL_TRANSPORT:
                InternalTransportRequest internalTransportRequest = (InternalTransportRequest) request;
                query = "INSERT INTO InternalTransportRequests VALUES " +
                        "('" + internalTransportRequest.getRequestID()
                        + "', '" + internalTransportRequest.getPatientName()
                        + "', '" + internalTransportRequest.getTransportType()
                        + "', '" + (internalTransportRequest.getUnconscious())
                        + "', '" + (internalTransportRequest.getInfectious())
                        + "')";
                break;
            case RELIGIOUS:
                ReligiousRequest religiousRequest = (ReligiousRequest) request;
                query = "INSERT INTO ReligiousRequests VALUES " +
                        "('" + religiousRequest.getRequestID()
                        + "', '" + religiousRequest.getPatientName()
                        + "', '" + religiousRequest.getReligiousDate()
                        + "', '" + religiousRequest.getStartTime()
                        + "', '" + religiousRequest.getEndTime()
                        + "', '" + religiousRequest.getFaith()
                        + "', '" + (religiousRequest.getInfectious())
                        + "')";
                break;
            case FOOD:
                FoodRequest foodRequest = (FoodRequest) request;
                query = "INSERT INTO FoodRequests VALUES " +
                        "('" + foodRequest.getRequestID()
                        + "', '" + foodRequest.getPatientName()
                        + "', '" + foodRequest.getArrivalTime()
                        + "', '" + foodRequest.getMealChoice()
                        + "')";
                break;
            case FLORAL:
                FloralRequest floralRequest = (FloralRequest) request;
                query = "INSERT INTO FloralRequests VALUES " +
                        "('" + floralRequest.getRequestID()
                        + "', '" + floralRequest.getPatientName()
                        + "', '" + floralRequest.getDeliveryDate()
                        + "', '" + floralRequest.getStartTime()
                        + "', '" + floralRequest.getEndTime()
                        + "', '" + floralRequest.getWantsRoses()
                        + "', '" + floralRequest.getWantsTulips()
                        + "', '" + floralRequest.getWantsDaisies()
                        + "', '" + floralRequest.getWantsLilies()
                        + "', '" + floralRequest.getWantsSunflowers()
                        + "', '" + floralRequest.getWantsCarnations()
                        + "', '" + floralRequest.getWantsOrchids()
                        + "')";
                break;
            case SECURITY:
                SecurityRequest securityRequest = (SecurityRequest) request;
                query = "INSERT INTO SecurityRequests VALUES " +
                        "('" + securityRequest.getRequestID()
                        + "', " + securityRequest.getUrgency()
                        + ")";
                break;
            case EXTERNAL_TRANSPORT:
                ExternalTransportRequest externalTransportRequest = (ExternalTransportRequest) request;
                query = "INSERT INTO ExternalTransportRequests VALUES " +
                        "('" + externalTransportRequest.getRequestID()
                        + "', '" + externalTransportRequest.getPatientName()
                        + "', '" + externalTransportRequest.getTransportType()
                        + "', '" + externalTransportRequest.getDestination()
                        + "', '" + externalTransportRequest.getPatientAllergies()
                        + "', '" + (externalTransportRequest.getOutNetwork())
                        + "', '" + (externalTransportRequest.getInfectious())
                        + "', '" + (externalTransportRequest.getUnconscious())
                        + "')";
                break;
            case LAUNDRY:
                LaundryRequest laundryRequest = (LaundryRequest) request;
                query = "INSERT INTO LaundryRequests VALUES " +
                        "('" + laundryRequest.getRequestID()
                        + "', '" + laundryRequest.getServiceType()
                        + "', '" + laundryRequest.getServiceSize()
                        + "', '" + (laundryRequest.getDark())
                        + "', '" + (laundryRequest.getLight())
                        + "', '" + (laundryRequest.getOccupied())
                        + "')";
                break;
            case CASE_MANAGER:
                CaseManagerRequest caseManagerRequest = (CaseManagerRequest) request;
                query = "INSERT INTO CaseManagerRequests VALUES " +
                        "('" + caseManagerRequest.getRequestID()
                        + "', '" + caseManagerRequest.getPatientName()
                        + "', '" + caseManagerRequest.getTimeForArrival()
                        + "')";
                break;
            case SOCIAL_WORKER:
                SocialWorkerRequest socialWorkerRequest = (SocialWorkerRequest) request;
                query = "INSERT INTO SocialWorkerRequests VALUES " +
                        "('" + socialWorkerRequest.getRequestID()
                        + "', '" + socialWorkerRequest.getPatientName()
                        + "', '" + socialWorkerRequest.getTimeForArrival()
                        + "')";
                break;
        }

        try {
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a request from Requests and the table specific to the given request
     *
     * @param request the request to remove
     */
    public void removeRequest(Request request) {
        Statement statement = this.getStatement();
        String querySpecificTable = "DELETE FROM '" + request.getRequestType() + "Requests" + "'WHERE requestID = '" + request.getRequestID() + "'";
        String queryGeneralTable = "DELETE FROM Requests WHERE requestID = '" + request.getRequestID() + "'";

        try {
            assert statement != null;
            statement.execute(querySpecificTable);
            statement.execute(queryGeneralTable);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the given request
     *
     * @param request the request to update
     */
    public void updateRequest(Request request) {
        Statement statement = this.getStatement();

        String query = "UPDATE Requests SET requestType = '" + request.getRequestType()
                + "', requestDate = '" + request.getDate()
                + "', requestTime = '" + request.getTime()
                + "', complete = '" + request.getComplete()
                + "', employeeName = '" + request.getEmployeeName()
                + "', location = '" + request.getLocation()
                + "', description = '" + request.getDescription()
                + "' WHERE requestID = '" + request.getRequestID() + "'";

        try {
            assert statement != null;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (request.getRequestType()) {
            case SANITATION:
                SanitationRequest sanitationRequest = (SanitationRequest) request;
                query = "UPDATE SanitationRequests SET sanitationType = '" + sanitationRequest.getSanitationType()
                        + "', sanitationSize = '" + sanitationRequest.getSanitationSize()
                        + "', hazardous = '" + sanitationRequest.getHazardous()
                        + "', biologicalSubstance = '" + sanitationRequest.getBiologicalSubstance()
                        + "', occupied = '" + sanitationRequest.getOccupied()
                        + "' WHERE requestID = '" + sanitationRequest.getRequestID() + "'";
                break;
            case MEDICINE:
                MedicineRequest medicineRequest = (MedicineRequest) request;
                query = "UPDATE MedicineRequests SET patientName = '" + medicineRequest.getPatientName()
                        + "', medicine = '" + medicineRequest.getMedicine()
                        + "' WHERE requestID = '" + medicineRequest.getRequestID() + "'";
                break;
            case INTERNAL_TRANSPORT:
                InternalTransportRequest internalTransportRequest = (InternalTransportRequest) request;
                query = "UPDATE InternalTransportRequests SET patientName = '" + internalTransportRequest.getPatientName()
                        + "', transportType = '" + internalTransportRequest.getTransportType()
                        + "', unconscious = '" + internalTransportRequest.getUnconscious()
                        + "', infectious = '" + internalTransportRequest.getInfectious()
                        + "' WHERE requestID = '" + internalTransportRequest.getRequestID() + "'";
                break;
            case RELIGIOUS:
                ReligiousRequest religiousRequest = (ReligiousRequest) request;
                query = "UPDATE ReligiousRequests SET patientName = '" + religiousRequest.getPatientName()
                        + "', startTime = '" + religiousRequest.getStartTime()
                        + "', endTime = '" + religiousRequest.getEndTime()
                        + "', religiousDate = '" + religiousRequest.getReligiousDate()
                        + "', faith = '" + religiousRequest.getFaith()
                        + "', infectious = '" + religiousRequest.getInfectious()
                        + "' WHERE requestID = '" + religiousRequest.getRequestID() + "'";
                break;
            case FOOD:
                FoodRequest foodRequest = (FoodRequest) request;
                query = "UPDATE FoodRequests SET patientName = '" + foodRequest.getPatientName()
                        + "', arrivalTime = '" + foodRequest.getArrivalTime()
                        + "', mealChoice = '" + foodRequest.getMealChoice()
                        + "' WHERE requestID = '" + foodRequest.getRequestID() + "'";
                break;
            case FLORAL:
                FloralRequest floralRequest = (FloralRequest) request;
                query = "UPDATE FloralRequests SET patientName = '" + floralRequest.getPatientName()
                        + "', deliveryDate = '" + floralRequest.getDeliveryDate()
                        + "', startTime = '" + floralRequest.getStartTime()
                        + "', endTime = '" + floralRequest.getEndTime()
                        + "', wantsRoses = '" + floralRequest.getWantsRoses()
                        + "', wantsTulips = '" + floralRequest.getWantsTulips()
                        + "', wantsDaisies = '" + floralRequest.getWantsDaisies()
                        + "', wantsLilies = '" + floralRequest.getWantsLilies()
                        + "', wantsSunflowers = '" + floralRequest.getWantsSunflowers()
                        + "', wantsCarnations = '" + floralRequest.getWantsCarnations()
                        + "', wantsOrchids = '" + floralRequest.getWantsOrchids()
                        + "' WHERE requestID = '" + floralRequest.getRequestID() + "'";
                break;
            case SECURITY:
                SecurityRequest securityRequest = (SecurityRequest) request;
                query = "UPDATE SecurityRequests SET urgency = " + securityRequest.getUrgency()
                        + " WHERE requestID = '" + securityRequest.getRequestID() + "'";
                break;
            case EXTERNAL_TRANSPORT:
                ExternalTransportRequest externalTransportRequest = (ExternalTransportRequest) request;
                query = "UPDATE ExternalTransportRequests SET patientName = '" + externalTransportRequest.getPatientName()
                        + "', transportType = '" + externalTransportRequest.getTransportType()
                        + "', destination = '" + externalTransportRequest.getDestination()
                        + "', patientAllergies = '" + externalTransportRequest.getPatientAllergies()
                        + "', outNetwork = '" + (externalTransportRequest.getOutNetwork())
                        + "', infectious = '" + (externalTransportRequest.getInfectious())
                        + "', unconscious = '" + (externalTransportRequest.getUnconscious())
                        + "' WHERE requestID = '" + externalTransportRequest.getRequestID() + "'";
                break;
            case LAUNDRY:
                LaundryRequest laundryRequest = (LaundryRequest) request;
                query = "UPDATE LaundryRequests SET serviceType = '" + laundryRequest.getServiceType()
                        + "', serviceSize = '" + laundryRequest.getServiceSize()
                        + "', dark = '" + (laundryRequest.getDark())
                        + "', light = '" + (laundryRequest.getLight())
                        + "', occupied = '" + (laundryRequest.getOccupied())
                        + "' WHERE requestID = '" + laundryRequest.getRequestID() + "'";
                break;
            case CASE_MANAGER:
                CaseManagerRequest caseManagerRequest = (CaseManagerRequest) request;
                query = "UPDATE CaseManagerRequests SET patientName = '" + caseManagerRequest.getPatientName()
                        + "', timeForArrival = '" + caseManagerRequest.getTimeForArrival()
                        + "' WHERE requestID = '" + caseManagerRequest.getRequestID() + "'";
                break;
            case SOCIAL_WORKER:
                SocialWorkerRequest socialWorkerRequest = (SocialWorkerRequest) request;
                query = "UPDATE SocialWorkerRequest SET patientName = '" + socialWorkerRequest.getPatientName()
                        + "', timeForArrival = '" + socialWorkerRequest.getTimeForArrival()
                        + "' WHERE requestID = '" + socialWorkerRequest.getRequestID() + "'";
                break;
        }

        try {
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();
    }

    /**
     * Displays the list of requests along with their attributes.
     *
     * @return a map of request IDs to actual requests
     */
    public Map<String, Request> getRequests() {
        Statement statement = this.getStatement();
        String query = "SELECT * FROM Requests";

        assert statement != null;
        try {
            ResultSet rs = statement.executeQuery(query);
            Map<String, Request> requests = new HashMap<>();

            Request outRequest;
            while (rs.next()) {
                outRequest = new Request(
                        rs.getString("requestID"),
                        Request.RequestType.valueOf(rs.getString("requestType")),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                requests.put(rs.getString("requestID"), outRequest);
            }
            rs.close();
            statement.close();
            return requests;
        } catch (SQLException ignored) {
            return null;
        }
    }

    /**
     * Get specific request information from the database given the request's ID and type
     *
     * @param requestID   the ID of the request
     * @param requestType the type of the request
     * @return the request
     */
    public Request getSpecificRequestById(String requestID, String requestType) {
        Statement statement = this.getStatement();

        String tableName = requestType.replaceAll("\\s", "") + "Requests";
        String query = "SELECT * FROM Requests LEFT JOIN " + tableName + " ON Requests.requestID = " + tableName + ".requestID WHERE Requests.requestID = '" + requestID + "'";

        assert statement != null;
        Request outRequest = null;
        try {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                switch (requestType) {
                    case "Sanitation":
                        outRequest = new SanitationRequest(
                                rs.getString("sanitationType"),
                                rs.getString("sanitationSize"),
                                rs.getString("hazardous"),
                                rs.getString("biologicalSubstance"),
                                rs.getString("occupied"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "Medicine":
                        outRequest = new MedicineRequest(
                                rs.getString("patientName"),
                                rs.getString("medicine"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "InternalTransport":
                        outRequest = new InternalTransportRequest(
                                rs.getString("patientName"),
                                rs.getString("transportType"),
                                rs.getString("unconscious"),
                                rs.getString("infectious"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "Religious":
                        outRequest = new ReligiousRequest(
                                rs.getString("patientName"),
                                rs.getString("startTime"),
                                rs.getString("endTime"),
                                rs.getString("religiousDate"),
                                rs.getString("faith"),
                                rs.getString("infectious"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "Food":
                        outRequest = new FoodRequest(
                                rs.getString("patientName"),
                                rs.getString("arrivalTime"),
                                rs.getString("mealChoice"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "Floral":
                        outRequest = new FloralRequest(
                                rs.getString("patientName"),
                                rs.getString("deliveryDate"),
                                rs.getString("startTime"),
                                rs.getString("endTime"),
                                rs.getString("wantsRoses"),
                                rs.getString("wantsTulips"),
                                rs.getString("wantsDaisies"),
                                rs.getString("wantsLilies"),
                                rs.getString("wantsSunflowers"),
                                rs.getString("wantsCarnations"),
                                rs.getString("wantsOrchids"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "Security":
                        outRequest = new SecurityRequest(
                                rs.getInt("urgency"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "ExternalTransport":
                        outRequest = new ExternalTransportRequest(
                                rs.getString("patientName"),
                                rs.getString("transportType"),
                                rs.getString("destination"),
                                rs.getString("patientAllergies"),
                                rs.getString("outNetwork"),
                                rs.getString("infectious"),
                                rs.getString("unconscious"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "Laundry":
                        outRequest = new LaundryRequest(
                                rs.getString("serviceType"),
                                rs.getString("serviceSize"),
                                rs.getString("dark"),
                                rs.getString("light"),
                                rs.getString("occupied"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                    case "CaseManager":
                        outRequest = new CaseManagerRequest(
                                rs.getString("patientName"),
                                rs.getString("timeForArrival"),
                                rs.getString("requestID"),
                                rs.getString("requestTime"),
                                rs.getString("requestDate"),
                                rs.getString("complete"),
                                rs.getString("employeeName"),
                                rs.getString("location"),
                                rs.getString("description")
                        );
                        break;
                }
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return outRequest;
    }


    /**
     * Retrieves a list of nodes from the database based on the given nodeType
     *
     * @param rest NodeType
     * @return List of nodes with the given node type
     */
    public List<Node> getNodesByCategory(NodeType rest) {
        Statement statement = this.getStatement();
        String query = "SELECT * FROM Nodes WHERE nodeType = '" + rest.toString() + "'";
        assert statement != null;
        try {
            ResultSet rs = statement.executeQuery(query);
            List<Node> nodes = new ArrayList<>();
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
                nodes.add(outNode);
            }
            rs.close();
            statement.close();
            return nodes;
        } catch (SQLException ignored) {
            return null;
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

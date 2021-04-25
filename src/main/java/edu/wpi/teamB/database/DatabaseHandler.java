package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.entities.map.data.NodeType;
import edu.wpi.teamB.entities.requests.*;

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

    Statement getStatement() {
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
     *
     * @throws SQLException if the nodes and edges are malformed
     */
    public void loadNodesEdges(List<Node> nodes, List<Edge> edges) throws SQLException {
        resetDatabase(Arrays.asList("SanitationRequests", "MedicineRequests", "InternalTransportRequests", "ReligiousRequests", "FoodRequests", "FloralRequests",
                "SecurityRequests", "ExternalTransportRequests", "LaundryRequests", "CaseManagerRequests", "SocialWorkerRequests", "Requests", "Edges", "Nodes"));
        executeSchema();
        loadDatabaseNodes(nodes);
        loadDatabaseEdges(edges);
    }

    /**
     * Resets the inputted tables
     *
     * @param tables the list of tables to reset
     * @throws SQLException if the query is malformed
     */
    public void resetDatabase(List<String> tables) throws SQLException {
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
            tables.add("FavoriteLocations");
        }

        for (String table : tables) {
            String query = "DROP TABLE IF EXISTS " + table;
            runStatement(query, false);
        }
    }

    /**
     * Executes the schema and creates the tables
     *
     * @throws SQLException if any of the queries are malformed
     */
    public void executeSchema() throws SQLException {
        String configuration = "PRAGMA foreign_keys = ON";

        String nodesTable = "CREATE TABLE IF NOT EXISTS Nodes("
                + "nodeID CHAR(20) PRIMARY KEY, "
                + "xcoord INT, "
                + "ycoord INT, "
                + "floor CHAR(20), "
                + "building CHAR(20), "
                + "nodeType CHAR(20), "
                + "longName CHAR(50), "
                + "shortName CHAR(20), "
                + "CHECK (xcoord >= 0), "
                + "CHECK (ycoord >= 0))";

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
                + "submitter CHAR(30), "
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

        String usersTable = "CREATE TABLE IF NOT EXISTS Users("
                + "username CHAR(30) PRIMARY KEY, "
                + "firstName CHAR(30), "
                + "lastName CHAR(30), "
                + "authenticationLevel CHAR(30) CHECK (authenticationLevel in ('ADMIN','STAFF','PATIENT', 'GUEST')), "
                + "passwordHash CHAR(30))";

        String jobsTable = "CREATE TABLE IF NOT EXISTS Jobs("
                + "username CHAR(30), "
                + "job CHAR(30), "
                + "FOREIGN KEY (username) REFERENCES Users(username))";

        String favoriteLocationsTable = "CREATE TABLE IF NOT EXISTS FavoriteLocations("
                + "username CHAR(30), "
                + "favoriteLocation CHAR(30), "
                + "FOREIGN KEY (username) REFERENCES Users(username))";

        runStatement(configuration, false);
        runStatement(nodesTable, false);
        runStatement(edgesTable, false);
        runStatement(requestsTable, false);
        runStatement(sanitationRequestsTable, false);
        runStatement(medicineRequestsTable, false);
        runStatement(internalTransportRequestsTable, false);
        runStatement(religiousRequestsTable, false);
        runStatement(foodRequestsTable, false);
        runStatement(floralRequestsTable, false);
        runStatement(securityRequestsTable, false);
        runStatement(externalTransportTable, false);
        runStatement(laundryTable, false);
        runStatement(caseManagerRequestsTable, false);
        runStatement(socialWorkerRequestsTable, false);
        runStatement(usersTable, false);
        runStatement(jobsTable, false);
        runStatement(favoriteLocationsTable, false);
    }

    /**
     * Refreshes the nodes table and loads the nodes given into
     * the database
     *
     * @param nodes the list of nodes
     * @throws SQLException if the nodes are malformed somehow
     */
    public void loadDatabaseNodes(List<Node> nodes) throws SQLException {

        String query = "INSERT INTO Nodes(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = databaseConnection.prepareStatement(query);
        if (nodes != null) {
            for (Node node : nodes) {
                statement.setString(1, node.getNodeID());
                statement.setInt(2, node.getXCoord());
                statement.setInt(3, node.getYCoord());
                statement.setString(4, node.getFloor());
                statement.setString(5, node.getBuilding());
                statement.setString(6, node.getNodeType());
                statement.setString(7, node.getLongName());
                statement.setString(8, node.getShortName());
                statement.executeUpdate();
            }
        }
        statement.close();
    }

    /**
     * Refreshes the edges table and loads the edges given into
     * the database
     *
     * @param edges the list of edges
     * @throws SQLException if the edges are malformed somehow
     */
    public void loadDatabaseEdges(List<Edge> edges) throws SQLException {
        String query = "INSERT INTO Edges(edgeID, startNode, endNode) "
                + "VALUES(?, ?, ?)";
        PreparedStatement statement = databaseConnection.prepareStatement(query);
        if (edges != null) {
            for (Edge edge : edges) {
                statement.setString(1, edge.getEdgeID());
                statement.setString(2, edge.getStartNodeID());
                statement.setString(3, edge.getEndNodeID());
                statement.executeUpdate();
            }
        }
        statement.close();
    }

    /**
     * Refreshes the requests tables and loads the requests given into
     * the database
     *
     * @param requests the list of requests
     * @throws SQLException if any requests are malformed
     */
    public void loadDatabaseRequests(List<Request> requests) throws SQLException {
        if (requests == null) return;
        for (Request request : requests)
            this.addRequest(request);
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
            ResultSet set = runStatement(query, true);
            Node n = new Node(
                    set.getString("nodeID").trim(),
                    set.getInt("xcoord"),
                    set.getInt("ycoord"),
                    set.getString("floor").trim(),
                    set.getString("building").trim(),
                    set.getString("nodeType").trim(),
                    set.getString("longName").trim(),
                    set.getString("shortName").trim()
            );
            set.close();
            return n;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * @param user     User to add
     * @param password Plaintext password for user
     * @throws SQLException if the user or password is malformed
     */
    public void addUser(User user, String password) throws SQLException {
        new UserMutator(this).addEntity(new UserMutator.UserPasswordMatch(user, password));
    }

    /**
     * @param username username to query by
     * @return User object with that username, or null if that user doesn't exist
     */
    public User getUserByUsername(String username) {
        try {
            String query = "SELECT job FROM Jobs WHERE (username = '" + username + "')";
            List<Request.RequestType> jobs = new ArrayList<>();
            ResultSet rs = runStatement(query, true);
            if (rs != null) {
                do {
                    jobs.add(Request.RequestType.valueOf(rs.getString("job")));
                } while (rs.next());
                rs.close();
            }

            query = "SELECT * FROM Users WHERE (username = '" + username + "')";
            rs = runStatement(query, true);
            if (rs == null) return null;
            User outUser = new User(
                    rs.getString("username"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    User.AuthenticationLevel.valueOf(rs.getString("authenticationLevel")),
                    jobs
            );
            rs.close();
            return outUser;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Gets a list of users who are assigned to handle jobs of certain type
     *
     * @param job RequestType enum of the type of job you want the users for
     * @return a list of users who are assigned to jobs of the given type, or null if none of them do
     */
    public List<User> getUsersByJob(Request.RequestType job) {
        try {
            String query = "SELECT username FROM " +
                    "Users NATURAL JOIN Jobs " +
                    "WHERE (job = '" + job.toString() + "')";
            ResultSet rs = runStatement(query, true);
            List<User> outUsers = new ArrayList<>();
            if (rs == null) return outUsers;
            do {
                String username = rs.getString("username");
                outUsers.add(this.getUserByUsername(username));
            } while (rs.next());
            rs.close();
            return outUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of users with the given authentication level
     *
     * @param authenticationLevel the EXACT authentication level you want the users for
     * @return list of users, or null if no users have that level
     */
    public List<User> getUsersByAuthenticationLevel(User.AuthenticationLevel authenticationLevel) {
        try {
            String query = "SELECT username, authenticationLevel FROM " +
                    "Users WHERE authenticationLevel='" + authenticationLevel.toString() + "'";
            ResultSet rs = runStatement(query, true);
            List<User> outUsers = new ArrayList<>();
            if (rs == null) return outUsers;
            do {
                String username = rs.getString("username");
                outUsers.add(this.getUserByUsername(username));
            } while (rs.next());
            rs.close();
            return outUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the information for a given user
     *
     * @param newUser the updated user
     * @throws SQLException if the user is malformed
     */
    public void updateUser(User newUser) throws SQLException {
        new UserMutator(this).updateEntity(new UserMutator.UserPasswordMatch(newUser, ""));
    }

    /**
     * Given the username of a user, delete them from the database
     *
     * @param username the username of the user to delete
     * @throws SQLException if the user is malformed
     */
    public void deleteUser(String username) throws SQLException {
        new UserMutator(this).removeEntity(username);
    }

    /**
     * @param username Claimed username of authenticator
     * @param password Claimed plaintext password of authenticator
     * @return If authentication is successful, return the User object representing the authenticated user, or null if not found
     */
    public User authenticate(String username, String password) {
        try {
            this.deauthenticate();
            String query = "SELECT passwordHash FROM Users WHERE (username = '" + username + "')";
            ResultSet rs = runStatement(query, true);
            if (rs == null) return null;
            String storedHash = (rs.getString("passwordHash"));
            rs.close();

            // Make sure the hashed password matches
            if (!this.passwordHash(password).equals(storedHash)) return null;
            User outUser = this.getUserByUsername(username);
            DatabaseHandler.AuthenticationUser = outUser;
            return outUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
        String query = "SELECT * FROM Nodes";
        try {
            ResultSet rs = runStatement(query, true);
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
                        rs.getString("shortName").trim()
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
     * Displays the list of edges along with their attributes.
     *
     * @return a map of edge IDs to actual edges
     */
    public Map<String, Edge> getEdges() {
        String query = "SELECT * FROM Edges";
        try {
            ResultSet rs = runStatement(query, true);
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
     * Adds an node to the database with the given information
     *
     * @param node the node to add
     */
    public void addNode(Node node) throws SQLException {
        new NodeMutator(this).addEntity(node);
    }

    /**
     * Adds an edge to the database with the given information
     *
     * @param edge the edge to add
     */
    public void addEdge(Edge edge) throws SQLException {
        new EdgeMutator(this).addEntity(edge);
    }

    /**
     * Updates the node with the specified ID.
     *
     * @param node the node to update
     */
    public void updateNode(Node node) throws SQLException {
        new NodeMutator(this).updateEntity(node);
    }

    /**
     * Updates the edge with the specified ID.
     *
     * @param edge the edge to update
     */
    public void updateEdge(Edge edge) throws SQLException {
        new EdgeMutator(this).updateEntity(edge);
    }

    /**
     * Removes the node given by the node ID.
     *
     * @param nodeID the node to remove, given by the node ID
     */
    public void removeNode(String nodeID) throws SQLException {
        new NodeMutator(this).removeEntity(nodeID);
    }

    /**
     * Removes the edge given by the edge ID.
     *
     * @param edgeID the edge to remove, given by the edge ID
     */
    public void removeEdge(String edgeID) throws SQLException {
        new EdgeMutator(this).removeEntity(edgeID);
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
        ResultSet set = runStatement(query, true);
        while (set.next()) {
            Edge outEdge = new Edge(
                    set.getString("edgeID").trim(),
                    set.getString("startNode").trim(),
                    set.getString("endNode").trim()
            );
            edges.add(outEdge);
        }
        set.close();
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
    public void addRequest(Request request) throws SQLException {
        new RequestMutator(this).addEntity(request);
    }

    /**
     * Updates the given request
     *
     * @param request the request to update
     */
    public void updateRequest(Request request) throws SQLException {
        new RequestMutator(this).updateEntity(request);
    }

    /**
     * Removes a request from Requests and the table specific to the given request
     *
     * @param request the request to remove
     */
    public void removeRequest(Request request) throws SQLException {
        new RequestMutator(this).removeEntity(request.getRequestID());
    }

    /**
     * Displays the list of requests along with their attributes.
     *
     * @return a map of request IDs to actual requests
     */
    public Map<String, Request> getRequests() throws SQLException {
        String query = "SELECT * FROM Requests";
        ResultSet rs = runStatement(query, true);
        Map<String, Request> requests = new HashMap<>();
        if (rs == null) return requests;
        do {
            Request outRequest = new Request(
                    rs.getString("requestID"),
                    Request.RequestType.valueOf(rs.getString("requestType")),
                    rs.getString("requestTime"),
                    rs.getString("requestDate"),
                    rs.getString("complete"),
                    rs.getString("employeeName"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("submitter")
            );
            requests.put(rs.getString("requestID"), outRequest);
        } while (rs.next());
        rs.close();
        return requests;
    }

    /**
     * Get specific request information from the database given the request's ID and type
     *
     * @param requestID   the ID of the request
     * @param requestType the type of the request
     * @return the request
     */
    public Request getSpecificRequestById(String requestID, Request.RequestType requestType) throws SQLException {
        String tableName = Request.RequestType.prettify(requestType).replaceAll("\\s", "") + "Requests";
        String query = "SELECT * FROM Requests LEFT JOIN " + tableName + " ON Requests.requestID = " + tableName + ".requestID WHERE Requests.requestID = '" + requestID + "'";
        Request outRequest = null;
        ResultSet rs = runStatement(query, true);
        if (rs == null) return null;
        switch (requestType) {
            case SANITATION:
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
            case MEDICINE:
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
            case INTERNAL_TRANSPORT:
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
            case RELIGIOUS:
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
            case FOOD:
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
            case FLORAL:
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
            case SECURITY:
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
            case EXTERNAL_TRANSPORT:
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
            case LAUNDRY:
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
            case CASE_MANAGER:
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
            case SOCIAL_WORKER:
                outRequest = new SocialWorkerRequest(
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
        rs.close();
        return outRequest;
    }


    /**
     * Retrieves a list of nodes from the database based on the given nodeType
     *
     * @param rest NodeType
     * @return List of nodes with the given node type
     */
    public List<Node> getNodesByCategory(NodeType rest) throws SQLException {
        String query = "SELECT * FROM Nodes WHERE nodeType = '" + rest.toString() + "'";
        ResultSet rs = runStatement(query, true);
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
                    rs.getString("shortName").trim()
            );
            nodes.add(outNode);
        } while (rs.next());
        rs.close();
        return nodes;
    }

    /**
     * Adds a favorite location to FavoriteLocations
     *
     * @param favoriteLocation the favorite location to add
     */
    public void addFavoriteLocation(String favoriteLocation) throws SQLException {
        new UserMutator(this).addFavoriteToUser(favoriteLocation);
    }

    /**
     * Removes a favorite location from FavoriteLocations
     *
     * @param favoriteLocation the favorite location to remove
     */
    public void removeFavoriteLocation(String favoriteLocation) throws SQLException {
        new UserMutator(this).removeFavoriteFromUser(favoriteLocation);
    }

    /**
     * Runs a given sql command and returns the result set if its a query
     * or null otherwise
     *
     * @param query     the query to run
     * @param returnSet whether it's a query
     * @return the result set if it's a query, or null otherwise
     * @throws SQLException if the query is malformed
     */
    public ResultSet runStatement(String query, boolean returnSet) throws SQLException {
        Statement statement = this.getStatement();
        assert statement != null;
        ResultSet set = null;
        if (returnSet) {
            set = statement.executeQuery(query);
            if (!set.next()) return null;
        } else {
            statement.execute(query);
            statement.close();
        }
        return set;
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

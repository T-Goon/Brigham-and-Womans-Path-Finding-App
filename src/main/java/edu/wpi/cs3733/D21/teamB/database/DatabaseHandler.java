package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.entities.requests.*;
import edu.wpi.cs3733.D21.teamB.entities.User;

import java.sql.*;
import java.util.*;

public class DatabaseHandler {

    private static final String URL_BASE = "jdbc:sqlite:";

    private String databaseURL;
    private Connection databaseConnection;

    // Singleton
    private static DatabaseHandler handler;
    private final NodeMutator nodeMutator;
    private final EdgeMutator edgeMutator;
    private final RequestMutator requestMutator;
    private final UserMutator userMutator;

    //State
    static User AuthenticationUser = new User(null, null, null, User.AuthenticationLevel.GUEST, null);

    private DatabaseHandler() {
        nodeMutator = new NodeMutator(this);
        edgeMutator = new EdgeMutator(this);
        requestMutator = new RequestMutator(this);
        userMutator = new UserMutator(this);
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


    // USERS

    /**
     * @param user     User to add
     * @param password Plaintext password for user
     * @throws SQLException if the user or password is malformed
     */
    public void addUser(User user, String password) throws SQLException {
        userMutator.addEntity(new UserMutator.UserPasswordMatch(user, password));
    }


    /**
     * Updates the information for a given user
     *
     * @param newUser the updated user
     * @throws SQLException if the user is malformed
     */
    public void updateUser(User newUser) throws SQLException {
        userMutator.updateEntity(new UserMutator.UserPasswordMatch(newUser, ""));
    }

    /**
     * Given the username of a user, delete them from the database
     *
     * @param username the username of the user to delete
     * @throws SQLException if the user is malformed
     */
    public void deleteUser(String username) throws SQLException {
        userMutator.removeEntity(username);
    }

    /**
     * Retreive all users in database
     * @return List of users in database
     */
    public List<User> getUsers(){
        return userMutator.getUsers();
    }

    /**
     * @param username username to query by
     * @return User object with that username, or null if that user doesn't exist
     */
    public User getUserByUsername(String username) {
        return userMutator.getUserByUsername(username);
    }

    /**
     * Gets a list of users who are assigned to handle jobs of certain type
     *
     * @param job RequestType enum of the type of job you want the users for
     * @return a list of users who are assigned to jobs of the given type, or null if none of them do
     */
    public List<User> getUsersByJob(Request.RequestType job) {
        return userMutator.getUsersByJob(job);
    }

    /**
     * Returns a list of users with the given authentication level
     *
     * @param authenticationLevel the EXACT authentication level you want the users for
     * @return list of users, or null if no users have that level
     */
    public List<User> getUsersByAuthenticationLevel(User.AuthenticationLevel authenticationLevel) {
        return userMutator.getUsersByAuthenticationLevel(authenticationLevel);
    }

    /**
     * @param username Claimed username of authenticator
     * @param password Claimed plaintext password of authenticator
     * @return If authentication is successful, return the User object representing the authenticated user, or null if not found
     */
    public User authenticate(String username, String password) {
        return userMutator.authenticate(username, password);
    }

    /**
     * Sets authentication level to guest
     */
    public void deauthenticate() {
        userMutator.deauthenticate();
    }

    /*
     * @return the User that is currently logged in
     */
    public User getAuthenticationUser() {
        return DatabaseHandler.AuthenticationUser;
    }


    // NODES

    /**
     * Displays the list of nodes along with their attributes.
     *
     * @return a map of node IDs to actual nodes
     */
    public Map<String, Node> getNodes() {
        return nodeMutator.getNodes();
    }

    /**
     * Adds an node to the database with the given information
     *
     * @param node the node to add
     */
    public void addNode(Node node) throws SQLException {
        nodeMutator.addEntity(node);
    }

    /**
     * Updates the node with the specified ID.
     *
     * @param node the node to update
     */
    public void updateNode(Node node) throws SQLException {
        nodeMutator.updateEntity(node);
    }

    /**
     * Removes the node given by the node ID.
     *
     * @param nodeID the node to remove, given by the node ID
     */
    public void removeNode(String nodeID) throws SQLException {
        nodeMutator.removeEntity(nodeID);
    }

    /**
     * Given a node ID, returns a list of all the edges that are adjacent to that node.
     *
     * @param nodeID the node ID of the node to get the adjacent edges of
     * @return the list of all adjacent edges of that node
     */
    public List<Edge> getAdjacentEdgesOfNode(String nodeID) throws SQLException {
        return nodeMutator.getAdjacentEdgesOfNode(nodeID);
    }

    /**
     * Retrieves a list of nodes from the database based on the given nodeType
     *
     * @param rest NodeType
     * @return List of nodes with the given node type
     */
    public List<Node> getNodesByCategory(NodeType rest) throws SQLException {
        return nodeMutator.getNodesByCategory(rest);
    }

    /**
     * Get specific node information from the database given the node's ID
     *
     * @param ID requested node ID
     * @return Node object with data from database
     */
    public Node getNodeById(String ID) {
        return nodeMutator.getNodeById(ID);
    }


    // EDGES

    /**
     * Displays the list of edges along with their attributes.
     *
     * @return a map of edge IDs to actual edges
     */
    public Map<String, Edge> getEdges() {
        return edgeMutator.getEdges();
    }

    /**
     * Adds an edge to the database with the given information
     *
     * @param edge the edge to add
     */
    public void addEdge(Edge edge) throws SQLException {
        edgeMutator.addEntity(edge);
    }

    /**
     * Updates the edge with the specified ID.
     *
     * @param edge the edge to update
     */
    public void updateEdge(Edge edge) throws SQLException {
        edgeMutator.updateEntity(edge);
    }

    /**
     * Removes the edge given by the edge ID.
     *
     * @param edgeID the edge to remove, given by the edge ID
     */
    public void removeEdge(String edgeID) throws SQLException {
        edgeMutator.removeEntity(edgeID);
    }


    // REQUESTS

    /**
     * Displays the list of requests along with their attributes.
     *
     * @return a map of request IDs to actual requests
     */
    public Map<String, Request> getRequests() throws SQLException {
        return requestMutator.getRequests();
    }

    /**
     * Adds a request to Requests and the table specific to the given request
     *
     * @param request the request to add
     */
    public void addRequest(Request request) throws SQLException {
        requestMutator.addEntity(request);
    }

    /**
     * Updates the given request
     *
     * @param request the request to update
     */
    public void updateRequest(Request request) throws SQLException {
        requestMutator.updateEntity(request);
    }

    /**
     * Removes a request from Requests and the table specific to the given request
     *
     * @param request the request to remove
     */
    public void removeRequest(Request request) throws SQLException {
        requestMutator.removeEntity(request.getRequestID());
    }

    /**
     * Get specific request information from the database given the request's ID and type
     *
     * @param requestID   the ID of the request
     * @param requestType the type of the request
     * @return the request
     */
    public Request getSpecificRequestById(String requestID, Request.RequestType requestType) throws SQLException {
        return requestMutator.getSpecificRequestById(requestID, requestType);
    }


    // MISCELLANEOUS

    /**
     * @return whether the nodes table is initialized or not
     */
    public boolean isInitialized() {
        return !getNodes().isEmpty() && !getEdges().isEmpty();
    }

    /**
     * @param plaintext plaintext password to hash
     * @return hashed password
     */
    public String passwordHash(String plaintext) {
        return String.valueOf(plaintext.hashCode());
    }

    /**
     * Adds a favorite location to FavoriteLocations
     *
     * @param favoriteLocation the favorite location to add
     */
    public void addFavoriteLocation(String favoriteLocation) throws SQLException {
        userMutator.addFavoriteToUser(favoriteLocation);
    }

    /**
     * Removes a favorite location from FavoriteLocations
     *
     * @param favoriteLocation the favorite location to remove
     */
    public void removeFavoriteLocation(String favoriteLocation) throws SQLException {
        userMutator.removeFavoriteFromUser(favoriteLocation);
    }

    /**
     * Displays the list of favorite locations
     *
     * @return a list of favorite locations
     * @throws SQLException
     */
    public List<String> getFavorites() throws SQLException {
        return userMutator.getFavoritesForUser();
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

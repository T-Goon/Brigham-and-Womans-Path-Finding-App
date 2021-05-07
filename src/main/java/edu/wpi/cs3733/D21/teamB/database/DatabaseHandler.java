package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.entities.requests.*;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import org.mindrot.jbcrypt.BCrypt;

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
    static User AuthenticationUser = new User("temporary", null, null, null, User.AuthenticationLevel.GUEST, "F", null);
    private final String salt = BCrypt.gensalt();

    private DatabaseHandler() {
        nodeMutator = new NodeMutator(this);
        edgeMutator = new EdgeMutator(this);
        requestMutator = new RequestMutator(this);
        userMutator = new UserMutator(this);
    }

    /**
     * @return the main database
     */
    public static DatabaseHandler getHandler() {
        return getDatabaseHandler("main.db");
    }

    /**
     * @param dbURL the path to the database (should default to "main.db")
     * @return the database handler
     */
    public static DatabaseHandler getDatabaseHandler(String dbURL) {
        if (!dbURL.equals("main.db") && !dbURL.equals("test.db"))
            throw new IllegalArgumentException("Illegal database type!");

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
        notifyObservers();
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
            tables.add("GiftRequests");
            tables.add("LanguageRequests");
            tables.add("EmergencyRequests");
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
                + "color CHAR(20),"
                + "CHECK (xcoord >= 0 AND xcoord <= 5000), "
                + "CHECK (ycoord >= 0 AND ycoord <= 3400))";

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
                + "arrivalDate CHAR(10), " // Stored as YYYY-MM-DD
                + "timeForArrival CHAR(20)," // Stored as HH:MM (24 hour time)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String socialWorkerRequestsTable = "CREATE TABLE IF NOT EXISTS SocialWorkerRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "arrivalDate CHAR(10), " // Stored as YYYY-MM-DD
                + "timeForArrival CHAR(20)," // Stored as HH:MM (24 hour time)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String giftRequestsTable = "CREATE TABLE IF NOT EXISTS GiftRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "deliveryDate CHAR(10), " // Stored as YYYY-MM-DD
                + "startTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "endTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "wantsBalloons CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsTeddyBear CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "wantsChocolate CHAR(1), " // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String languageRequestsTable = "CREATE TABLE IF NOT EXISTS LanguageInterpretationRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "patientName CHAR(30), "
                + "language CHAR(30), "
                + "arrivalDate CHAR(10), " // Stored as YYYY-MM-DD
                + "arrivalTime CHAR(5), " // Stored as HH:MM (24 hour time)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String emergencyRequestsTable = "CREATE TABLE IF NOT EXISTS EmergencyRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "medicalEmergency CHAR(20)," // Stored as T/F (no boolean data type in SQL)
                + "securityEmergency CHAR(20)," // Stored as T/F (no boolean data type in SQL)
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String covidSurveyRequestsTable = "CREATE TABLE IF NOT EXISTS CovidSurveyRequests("
                + "requestID CHAR(20) PRIMARY KEY, "
                + "status CHAR(10) CHECK (status in ('UNCHECKED','PENDING','DANGEROUS', 'SAFE')), "
                + "fever CHAR(1) CHECK (fever in ('T','F')), "
                + "chills CHAR(1) CHECK (chills in ('T','F')), "
                + "cough CHAR(1) CHECK (cough in ('T','F')), "
                + "shortBreath CHAR(1) CHECK (shortBreath in ('T','F')), "
                + "soreTht CHAR(1) CHECK (soreTht in ('T','F')), "
                + "headache CHAR(1) CHECK (headache in ('T','F')), "
                + "aches CHAR(1) CHECK (aches in ('T','F')), "
                + "nose CHAR(1) CHECK (nose in ('T','F')), "
                + "lostTaste CHAR(1) CHECK (lostTaste in ('T','F')), "
                + "nausea CHAR(1) CHECK (nausea in ('T','F')), "
                + "closeContact CHAR(1) CHECK (closeContact in ('T','F')), "
                + "positiveTest CHAR(1) CHECK (positiveTest in ('T','F')), "
                + "FOREIGN KEY (requestID) REFERENCES Requests(requestID))";

        String usersTable = "CREATE TABLE IF NOT EXISTS Users("
                + "username CHAR(30) PRIMARY KEY, "
                + "email CHAR(40) NOT NULL UNIQUE, "
                + "firstName CHAR(30), "
                + "lastName CHAR(30), "
                + "authenticationLevel CHAR(30) CHECK (authenticationLevel in ('ADMIN','STAFF','PATIENT', 'GUEST')), "
                + "covidStatus CHAR(10) CHECK (covidStatus in ('UNCHECKED','PENDING','DANGEROUS', 'SAFE')), "
                + "ttsEnabled CHAR(1) CHECK (ttsEnabled in ('T', 'F')), "
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
        runStatement(giftRequestsTable, false);
        runStatement(languageRequestsTable, false);
        runStatement(emergencyRequestsTable, false);
        runStatement(covidSurveyRequestsTable, false);
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

        String query = "INSERT INTO Nodes(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName, color) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                statement.setString(9, node.getColor().toString());
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
        //Make sure state is accurately reflective
        if (newUser.getUsername() != null && newUser.getUsername().equals(this.getAuthenticationUser().getUsername())) {
            DatabaseHandler.AuthenticationUser = newUser;
        }
    }

    /**
     * Given the username of a user, delete them from the database
     *
     * @param username the username of the user to delete
     * @throws SQLException if the user is malformed
     */
    public void deleteUser(String username) throws SQLException {
        userMutator.removeEntity(username);
        Collection<Request> requests = requestMutator.getRequests().values();
        for (Request r : requests) {
            if (r.getSubmitter().equals(username)) {
                requestMutator.removeEntity(r.getRequestID());
            }
        }
    }

    /**
     * Retrieve all users in database
     *
     * @return List of users in database
     */
    public List<User> getUsers() {
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
     * @param email email to query by
     * @return User object with that email, or null if that user doesn't exist
     */
    public User getUserByEmail(String email) {
        return userMutator.getUserByEmail(email);
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
     * @return If authentication is successful, return the User object representing the authenticated user, or null if not found. Also modifies the state to update authenticated User.
     */
    public User authenticate(String username, String password) {
        User attempt = userMutator.authenticate(username, password);
        if (attempt != null) {
            DatabaseHandler.AuthenticationUser = attempt;
        }
        return attempt;
    }

    /**
     * Sets authentication level to guest
     */
    public void deauthenticate() {
        DatabaseHandler.AuthenticationUser = authenticate("temporary", "");
    }

    /**
     * Resets the temporary user
     */
    public User resetTemporaryUser() {
        User user = new User("temporary", null, null, null, User.AuthenticationLevel.GUEST, User.CovidStatus.UNCHECKED, "F", null);
        try {
            deleteUser("temporary");
        } catch (SQLException ignored) {
        }
        try {
            addUser(user, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @return the User that is currently logged in
     **/
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
        notifyObservers();
    }

    /**
     * Updates the node with the specified ID.
     *
     * @param node the node to update
     */
    public void updateNode(Node node) throws SQLException {
        nodeMutator.updateEntity(node);
        notifyObservers();
    }

    /**
     * Removes the node given by the node ID.
     *
     * @param nodeID the node to remove, given by the node ID
     */
    public void removeNode(String nodeID) throws SQLException {
        nodeMutator.removeEntity(nodeID);
        notifyObservers();
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
        notifyObservers();
    }

    /**
     * Updates the edge with the specified ID.
     *
     * @param edge the edge to update
     */
    public void updateEdge(Edge edge) throws SQLException {
        edgeMutator.updateEntity(edge);
        notifyObservers();
    }

    /**
     * Removes the edge given by the edge ID.
     *
     * @param edgeID the edge to remove, given by the edge ID
     */
    public void removeEdge(String edgeID) throws SQLException {
        edgeMutator.removeEntity(edgeID);
        notifyObservers();
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

        if (request instanceof CovidSurveyRequest) {
            User user = getUserByUsername(request.getSubmitter());
            if (user != null) {
                user.setCovidStatus(((CovidSurveyRequest) request).getStatus());
                DatabaseHandler.getHandler().updateUser(user);
            }
        }
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
        return BCrypt.hashpw(plaintext, salt);
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
     * Updates a favorite location (parking spot) in FavoriteLocations
     *
     * @param favoriteLocation the parking spot to update
     */
    public void updateParkingSpot(String favoriteLocation) throws SQLException {
        userMutator.updateParkingForUser(favoriteLocation);
    }

    /**
     * Displays the list of favorite locations
     *
     * @return a list of favorite locations
     * @throws SQLException if the DB is malformed
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
     * Update the Graph class with new info
     */
    private void notifyObservers() {
        Graph.getGraph().updateGraph();
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

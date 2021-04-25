package edu.wpi.teamB;

import edu.wpi.teamB.database.*;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.map.data.NodeType;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.entities.requests.FoodRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.entities.requests.SanitationRequest;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseHandlerTest {
    private static String resourcesPath;
    private static DatabaseHandler db;
    private static List<Node> testNodes;

    @BeforeAll
    static void initDB() {
        db = DatabaseHandler.getDatabaseHandler("test.db");
        resourcesPath = "/edu/wpi/teamB/database/load";
        Graph.setGraph(db);

        testNodes = new ArrayList<>();

        Node targetNode0 = new Node("bWALK00501", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode1 = new Node("bWALK00502", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode2 = new Node("bWALK00503", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode3 = new Node("bWALK00504", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode4 = new Node("bWALK00505", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode5 = new Node("bWALK00506", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode6 = new Node("bWALK00507", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode7 = new Node("bWALK00508", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode8 = new Node("bWALK00509", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode9 = new Node("bWALK00510", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode10 = new Node("bPARK00101", 179, 1895, "1", "Parking", "PARK", "Left Parking Lot Spot 1", "LLot1");
        testNodes.add(targetNode0);
        testNodes.add(targetNode1);
        testNodes.add(targetNode2);
        testNodes.add(targetNode3);
        testNodes.add(targetNode4);
        testNodes.add(targetNode5);
        testNodes.add(targetNode6);
        testNodes.add(targetNode7);
        testNodes.add(targetNode8);
        testNodes.add(targetNode9);
        testNodes.add(targetNode10);
    }

    @BeforeEach
    void resetDB() {
        try {
            db.resetDatabase(new ArrayList<>());
            db.executeSchema();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleParseNodes() {
        Node target = new Node("testNode",
                0,
                -992,
                "1",
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");
        Node actual = CSVHandler.loadCSVNodes(resourcesPath + "/SimpleTestNodes.csv").get(0);
        assertEquals(target.toString(), actual.toString());
    }

    @Test
    public void complexParseNodesLength() {
        List<Node> nodes = CSVHandler.loadCSVNodes(resourcesPath + "/ComplexTestNodes.csv");
        assertEquals(32, nodes.size());
    }

    @Test
    public void complexParseNodesValues() {
        Node target = new Node("bWALK00501", 1872, 1965, "1", "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        List<Node> nodes = CSVHandler.loadCSVNodes(resourcesPath + "/ComplexTestNodes.csv");
        List<String> expanded = nodes.stream().map(Node::toString).collect(Collectors.toList());
        assertTrue(expanded.contains(target.toString()));
    }

    @Test
    public void simpleParseEdges() {
        Edge target = new Edge("bPARK00101_bWALK00101", "bPARK00101", "bWALK00101");
        Edge actual = CSVHandler.loadCSVEdges(resourcesPath + "/SimpleTestEdges.csv").get(0);
        assertEquals(target.toString(), actual.toString());
    }

    @Test
    public void complexParseEdgesLength() {
        List<Edge> nodes = CSVHandler.loadCSVEdges(resourcesPath + "/ComplexTestEdges.csv");
        assertEquals(31, nodes.size());
    }

    @Test
    public void complexParseEdgesValues() {
        Edge target = new Edge("bPARK01201_bWALK00501", "bPARK01201", "bWALK00501");
        List<Edge> nodes = CSVHandler.loadCSVEdges(resourcesPath + "/ComplexTestEdges.csv");
        List<String> expanded = nodes.stream().map(Edge::toString).collect(Collectors.toList());
        assertTrue(expanded.contains(target.toString()));
    }

    @Test
    void fillDatabase() {
        List<Edge> edges = new ArrayList<>();

        Edge targetEdge = new Edge("bPARK01201_bWALK00501", "bWALK00502", "bWALK00501");
        edges.add(targetEdge);

        try {
            db.loadNodesEdges(testNodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Node> outNodes = db.getNodes();
        assert (outNodes.values().containsAll(testNodes));
        Map<String, Edge> outEdges = db.getEdges();
        assertEquals(outEdges.values().toArray()[0], targetEdge);
    }

    @Test
    public void testUpdateNode() {
        List<Node> actual = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Node target = new Node("testNode",
                0,
                992,
                "1",
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");
        actual.add(target);
        try {
            db.loadNodesEdges(actual, edges);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String nodeID = target.getNodeID();
        int xcoord = 1;
        int ycoord = 2;
        String floor = "3";
        String building = "Parking";
        String nodeType = "PARK";
        String longName = "Left Parking Lot Spot 10";
        String shortName = "LLot10";
        try {
            db.updateNode(new Node(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Node> nodes = db.getNodes();
        assertEquals(1, nodes.get("testNode").getXCoord());
        assertEquals(2, nodes.get("testNode").getYCoord());
        assertEquals("3", nodes.get("testNode").getFloor());
        assertEquals("Parking", nodes.get("testNode").getBuilding());
        assertEquals("PARK", nodes.get("testNode").getNodeType());
        assertEquals("Left Parking Lot Spot 10", nodes.get("testNode").getLongName());
        assertEquals("LLot10", nodes.get("testNode").getShortName());
    }

    @Test
    public void testUpdateEdge() {
        List<Node> nodes = new ArrayList<>();
        List<Edge> actual = new ArrayList<>();
        Edge target = new Edge("bPARK01201_bWALK00501", "test_start", "test_end");
        Node start = new Node("test_start", 0, 0, "0", "0", "0", "test", "t");
        Node end = new Node("test_end", 0, 0, "0", "0", "0", "test", "t");
        actual.add(target);
        nodes.add(start);
        nodes.add(end);
        try {
            db.loadNodesEdges(nodes, actual);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String edgeID = target.getEdgeID();
        String startNode = "test_start";
        String endNode = "test_end";
        try {
            db.updateEdge(new Edge(edgeID, startNode, endNode));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Edge> edges = db.getEdges();
        assertEquals("test_start", edges.get("bPARK01201_bWALK00501").getStartNodeID());
        assertEquals("test_end", edges.get("bPARK01201_bWALK00501").getEndNodeID());
    }

    @Test
    public void testAddNode() {
        Node target = new Node("testNode",
                0,
                -992,
                "1",
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");

        try {
            db.addNode(target);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Node> nodes = db.getNodes();
        assertEquals(0, nodes.get("testNode").getXCoord());
        assertEquals(-992, nodes.get("testNode").getYCoord());
        assertEquals("1", nodes.get("testNode").getFloor());
        assertEquals("test_building", nodes.get("testNode").getBuilding());
        assertEquals("NODETYPE", nodes.get("testNode").getNodeType());
        assertEquals("Name With Many Spaces", nodes.get("testNode").getLongName());
        assertEquals("N W M S", nodes.get("testNode").getShortName());
    }

    @Test
    public void testAddEdge() {
        Node start = new Node("test_start", 0, 0, "0", "0", "0", "test", "t");
        Node end = new Node("test_end", 0, 0, "0", "0", "0", "test", "t");

        try {
            db.addNode(start);
            db.addNode(end);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Edge target = new Edge("bPARK01201_bWALK00501", "test_start", "test_end");

        try {
            db.addEdge(target);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Edge> edges = db.getEdges();
        assertEquals("test_start", edges.get("bPARK01201_bWALK00501").getStartNodeID());
        assertEquals("test_end", edges.get("bPARK01201_bWALK00501").getEndNodeID());
    }

    @Test
    public void testRemoveNode() {
        Node target = new Node("testNode",
                0,
                -992,
                "1",
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");

        try {
            db.addNode(target);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        assertFalse(db.getNodes().isEmpty());

        try {
            db.removeNode(target.getNodeID());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertTrue(db.getNodes().isEmpty());
    }

    @Test
    public void testRemoveEdge() {
        Node start = new Node("test_start", 0, 0, "0", "0", "0", "test", "t");
        Node end = new Node("test_end", 0, 0, "0", "0", "0", "test", "t");
        try {
            db.addNode(start);
            db.addNode(end);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Edge target = new Edge("bPARK01201_bWALK00501", "test_start", "test_end");

        try {
            db.addEdge(target);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertFalse(db.getEdges().isEmpty());

        try {
            db.removeEdge(target.getEdgeID());
        } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        assertTrue(db.getEdges().isEmpty());
    }

    @Test
    public void testAddRequest() {
        // populate nodes table with nodes
        Node node1 = new Node("node1", 0, 0, "0", "0", "0", "test", "t");
        Node node2 = new Node("node2", 0, 0, "0", "0", "0", "test", "t");
        try {
            db.addNode(node1);
            db.addNode(node2);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        SanitationRequest request1 = new SanitationRequest("Glass",
                "Small",
                "T",
                "F",
                "F",
                "testRequest1",
                "12:24",
                "2021-04-02",
                "F",
                "Bob",
                "node1",
                "None");
        FoodRequest request2 = new FoodRequest("Jane",
                "11:30",
                "salad",
                "testRequest2",
                "10:00",
                "2021-05-10",
                "F",
                "Bob",
                "node2",
                "test");
        try {
            db.addRequest(request1);
            db.addRequest(request2);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        SanitationRequest test1;
        try {
            test1 = (SanitationRequest) db.getSpecificRequestById(request1.getRequestID(), Request.RequestType.SANITATION);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertEquals("Glass", test1.getSanitationType());
        assertEquals("Small", test1.getSanitationSize());
        assertEquals("T", test1.getHazardous());
        assertEquals("F", test1.getBiologicalSubstance());
        assertEquals("F", test1.getOccupied());
        assertEquals("12:24", test1.getTime());
        assertEquals("2021-04-02", test1.getDate());
        assertEquals("F", test1.getComplete());
        assertEquals("Bob", test1.getEmployeeName());
        assertEquals("node1", test1.getLocation());
        assertEquals("None", test1.getDescription());

        FoodRequest test2 = null;
        try {
            test2 = (FoodRequest) db.getSpecificRequestById(request2.getRequestID(), Request.RequestType.FOOD);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertEquals("Jane", test2.getPatientName());
        assertEquals("11:30", test2.getArrivalTime());
        assertEquals("salad", test2.getMealChoice());
        assertEquals("10:00", test2.getTime());
        assertEquals("2021-05-10", test2.getDate());
        assertEquals("F", test2.getComplete());
        assertEquals("Bob", test2.getEmployeeName());
        assertEquals("node2", test2.getLocation());
        assertEquals("test", test2.getDescription());
    }

    @Test
    public void testRemoveRequest() {
        // populate nodes table with nodes
        Node node1 = new Node("node1", 0, 0, "0", "0", "0", "test", "t");
        Node node2 = new Node("node2", 0, 0, "0", "0", "0", "test", "t");
        try {
            db.addNode(node1);
            db.addNode(node2);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        SanitationRequest request1 = new SanitationRequest("Glass",
                "Small",
                "T",
                "F",
                "F",
                "testRequest1",
                "12:24",
                "2021-04-02",
                "F",
                "Bob",
                "node1",
                "None");
        FoodRequest request2 = new FoodRequest("Jane",
                "11:30",
                "salad",
                "testRequest2",
                "10:00",
                "2021-05-10",
                "F",
                "Bob",
                "node2",
                "test");

        try {
            db.addRequest(request1);
            db.addRequest(request2);
            assertFalse(db.getRequests().isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            db.removeRequest(request1);
            db.removeRequest(request2);
            assertTrue(db.getRequests().isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

    }

    @Test
    public void testUpdateRequest() {
        // populate nodes table with nodes
        Node node1 = new Node("node1", 0, 0, "0", "0", "0", "test", "t");
        Node node2 = new Node("node2", 0, 0, "0", "0", "0", "test", "t");
        try {
            db.addNode(node1);
            db.addNode(node2);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        List<Request> actual = new ArrayList<>();
        SanitationRequest request1 = new SanitationRequest("Glass",
                "Small",
                "T",
                "F",
                "F",
                "testRequest1",
                "12:24",
                "2021-04-02",
                "F",
                "Bob",
                "node1",
                "None");
        FoodRequest request2 = new FoodRequest("Jane",
                "11:30",
                "salad",
                "testRequest2",
                "10:00",
                "2021-05-10",
                "F",
                "Bob",
                "node2",
                "test");
        actual.add(request1);
        actual.add(request2);
        try {
            db.loadDatabaseRequests(actual);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            db.updateRequest(new SanitationRequest("Dry", "Medium", "F", "T", "T", request1.getRequestID(), "13:30", "2021-04-18", "T", "Mike", "node2", "test"));
            db.updateRequest(new FoodRequest("Alice", "12:00", "chicken", request2.getRequestID(), "10:05", "2021-05-30", "T", "Mike", "node1", "None"));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        SanitationRequest test1 = null;
        try {
            test1 = (SanitationRequest) db.getSpecificRequestById(request1.getRequestID(), Request.RequestType.SANITATION);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertEquals("Dry", test1.getSanitationType());
        assertEquals("Medium", test1.getSanitationSize());
        assertEquals("F", test1.getHazardous());
        assertEquals("T", test1.getBiologicalSubstance());
        assertEquals("T", test1.getOccupied());
        assertEquals("13:30", test1.getTime());
        assertEquals("2021-04-18", test1.getDate());
        assertEquals("T", test1.getComplete());
        assertEquals("Mike", test1.getEmployeeName());
        assertEquals("node2", test1.getLocation());
        assertEquals("test", test1.getDescription());

        FoodRequest test2 = null;
        try {
            test2 = (FoodRequest) db.getSpecificRequestById(request2.getRequestID(), Request.RequestType.FOOD);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertEquals("Alice", test2.getPatientName());
        assertEquals("12:00", test2.getArrivalTime());
        assertEquals("chicken", test2.getMealChoice());
        assertEquals("10:05", test2.getTime());
        assertEquals("2021-05-30", test2.getDate());
        assertEquals("T", test2.getComplete());
        assertEquals("Mike", test2.getEmployeeName());
        assertEquals("node1", test2.getLocation());
        assertEquals("None", test2.getDescription());
    }

    @Test
    public void testGetNodesByCategory() {
        try {
            db.loadDatabaseNodes(testNodes);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        List<Node> result = null;
        try {
            result = db.getNodesByCategory(NodeType.PARK);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        List<Node> expected = new ArrayList<>();
        expected.add(testNodes.get(10));

        assertEquals(expected, result);


    }

    @Test
    public void testPasswordHash() {
        assertEquals(db.passwordHash("Sphinx"), db.passwordHash("Sphinx"));
        assertNotEquals(db.passwordHash("Sphinx"), db.passwordHash("Quartz"));
    }

    @Test
    public void testAddGetUser() {
        User user = new User("testuser", "Testing", "User", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.LAUNDRY));
        try {
            db.addUser(user, "password");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        User out = db.getUserByUsername("testuser");
        assertEquals(out, user);

    }

    @Test
    public void testAuthentication() {
        User user = new User("testuser", "Testing", "User", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.CASE_MANAGER));
        try {
            db.addUser(user, "password");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        User authentication = db.authenticate("testuser", "password");
        assertEquals(authentication, user);
        assertEquals(db.getAuthenticationUser(), authentication);
        assertNull(db.authenticate("testuser", "pasword"));
        assertEquals(db.getAuthenticationUser(), new User(null, null, null, User.AuthenticationLevel.GUEST, null));
    }

    @Test
    public void testGetUserByJob() {
        List<Request.RequestType> jobs = new ArrayList<>();
        jobs.add(Request.RequestType.FOOD);
        jobs.add(Request.RequestType.INTERNAL_TRANSPORT);
        User user1 = new User("testuser1", "Testing1", "User1", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.FOOD));
        User user2 = new User("testuser2", "Testing2", "User2", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.INTERNAL_TRANSPORT));
        User user3 = new User("testuser3", "Testing3", "User3", User.AuthenticationLevel.STAFF, jobs);

        try {
            db.addUser(user1, "p1");
            db.addUser(user2, "p2");
            db.addUser(user3, "p3");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        List<User> set1 = db.getUsersByJob(Request.RequestType.FOOD);
        List<User> set2 = db.getUsersByJob(Request.RequestType.INTERNAL_TRANSPORT);

        assertEquals(2, set1.size());
        assertEquals(2, set2.size());
        assert (set1.contains(user1));
        assert (set2.contains(user2));
        assert (set1.contains(user3));

    }

    @Test
    public void testDeleteUser() {
        User user = new User("testuser", "Testing", "User", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.CASE_MANAGER));
        try {
            db.addUser(user, "password");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertEquals(db.getUserByUsername("testuser"), user);
        try {
            db.deleteUser("testuser");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertNull(db.getUserByUsername("testuser"));
    }

    @Test
    public void testUpdateUser() {
        User user = new User("testuser", "Testing", "User", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.CASE_MANAGER));
        User altuser = new User("testuser", "Alternate", "User", User.AuthenticationLevel.ADMIN, Collections.singletonList(Request.RequestType.FOOD));
        try {
            db.addUser(user, "password");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        assertEquals(db.getUserByUsername("testuser"), user);
        assert (db.getUsersByJob(Request.RequestType.CASE_MANAGER).contains(user));
        assertNotNull(db.authenticate("testuser", "password"));
        try {
            db.updateUser(altuser);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(db.getUserByUsername("testuser").toString());
        System.out.println(altuser.toString());

        assertEquals(db.getUserByUsername("testuser"), altuser);
        assert (!db.getUsersByJob(Request.RequestType.CASE_MANAGER).contains(user));
        assert (db.getUsersByJob(Request.RequestType.FOOD).contains(altuser));
        assertNotNull(db.authenticate("testuser", "password"));
    }

    @Test
    public void testGetUsersByAuthenticationLevel() {
        User user = new User("testUser", "Testing", "User", User.AuthenticationLevel.STAFF, Collections.singletonList(Request.RequestType.CASE_MANAGER));
        User altuser = new User("altUser", "Alternate", "User", User.AuthenticationLevel.ADMIN, Collections.singletonList(Request.RequestType.FOOD));

        try {
            db.addUser(user, "user");
            db.addUser(altuser, "altuser");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        assertEquals(db.getUsersByAuthenticationLevel(User.AuthenticationLevel.STAFF).get(0), user);
    }
}
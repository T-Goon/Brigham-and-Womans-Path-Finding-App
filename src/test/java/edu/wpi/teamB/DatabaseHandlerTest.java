package edu.wpi.teamB;

import edu.wpi.teamB.database.*;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseHandlerTest {
    private static Path resourcesPath;
    private static DatabaseHandler db;

    @BeforeAll
    static void initDB() {
        db = DatabaseHandler.getDatabaseHandler("test.db");
        resourcesPath = Paths.get("src/test/resources/edu/wpi/teamB/database/load");
    }

    @BeforeEach
    void resetDB() {
        try {
            db.loadDatabase(null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleParseNodes() {
        Node target = new Node("testNode",
                0,
                -992,
                1,
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");
        Path nodes = Paths.get(resourcesPath + "/SimpleTestNodes.csv");
        Node actual = CSVHandler.loadCSVNodes(nodes).get(0);
        assertEquals(target.toString(), actual.toString());
    }

    @Test
    public void complexParseNodesLength() {
        Path nodePath = Paths.get(resourcesPath + "/ComplexTestNodes.csv");
        List<Node> nodes = CSVHandler.loadCSVNodes(nodePath);
        assertEquals(32, nodes.size());
    }

    @Test
    public void complexParseNodesValues() {
        Node target = new Node("bWALK00501", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Path nodePath = Paths.get(resourcesPath + "/ComplexTestNodes.csv");
        List<Node> nodes = CSVHandler.loadCSVNodes(nodePath);
        List<String> expanded = nodes.stream().map(Node::toString).collect(Collectors.toList());
        assertTrue(expanded.contains(target.toString()));
    }

    @Test
    public void simpleParseEdges() {
        Edge target = new Edge("bPARK00101_bWALK00101", "bPARK00101", "bWALK00101");
        Path edges = Paths.get(resourcesPath + "/SimpleTestEdges.csv");
        Edge actual = CSVHandler.loadCSVEdges(edges).get(0);
        assertEquals(target.toString(), actual.toString());
    }

    @Test
    public void complexParseEdgesLength() {
        Path nodePath = Paths.get(resourcesPath + "/ComplexTestEdges.csv");
        List<Edge> nodes = CSVHandler.loadCSVEdges(nodePath);
        assertEquals(31, nodes.size());
    }

    @Test
    public void complexParseEdgesValues() {
        Edge target = new Edge("bPARK01201_bWALK00501", "bPARK01201", "bWALK00501");
        Path nodePath = Paths.get(resourcesPath + "/ComplexTestEdges.csv");
        List<Edge> nodes = CSVHandler.loadCSVEdges(nodePath);
        List<String> expanded = nodes.stream().map(Edge::toString).collect(Collectors.toList());
        assertTrue(expanded.contains(target.toString()));
    }

    @Test
    void fillDatabase() {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        //Theres a less scuffed way to do this, but hey, it works and is easy to tweak.
        Node targetNode0 = new Node("bWALK00501", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode1 = new Node("bWALK00502", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode2 = new Node("bWALK00503", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode3 = new Node("bWALK00504", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode4 = new Node("bWALK00505", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode5 = new Node("bWALK00506", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode6 = new Node("bWALK00507", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode7 = new Node("bWALK00508", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode8 = new Node("bWALK00509", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        Node targetNode9 = new Node("bWALK00510", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        nodes.add(targetNode0);
        nodes.add(targetNode1);
        nodes.add(targetNode2);
        nodes.add(targetNode3);
        nodes.add(targetNode4);
        nodes.add(targetNode5);
        nodes.add(targetNode6);
        nodes.add(targetNode7);
        nodes.add(targetNode8);
        nodes.add(targetNode9);
        Edge targetEdge = new Edge("bPARK01201_bWALK00501", "bPARK01201", "bWALK00501");
        edges.add(targetEdge);

        try {
            db.loadDatabase(nodes, edges);
            Map<String, Node> outNodes = db.getNodes();
            assert (outNodes.values().containsAll(nodes));
            Map<String, Edge> outEdges = db.getEdges();
            assertEquals(outEdges.values().toArray()[0], targetEdge);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUpdateNode() throws SQLException {
        List<Node> actual = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Node target = new Node("testNode",
                0,
                -992,
                1,
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");
        actual.add(target);
        db.loadDatabase(actual, edges);

        String nodeID = target.getNodeID();
        int xcoord = 1;
        int ycoord = 2;
        int floor = 3;
        String building = "Parking";
        String nodeType = "PARK";
        String longName = "Left Parking Lot Spot 10";
        String shortName = "LLot10";
        db.updateNode(new Node(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName));

        Map<String, Node> nodes = db.getNodes();
        assertEquals(1, nodes.get("testNode").getXCoord());
        assertEquals(2, nodes.get("testNode").getYCoord());
        assertEquals(3, nodes.get("testNode").getFloor());
        assertEquals("Parking", nodes.get("testNode").getBuilding());
        assertEquals("PARK", nodes.get("testNode").getNodeType());
        assertEquals("Left Parking Lot Spot 10", nodes.get("testNode").getLongName());
        assertEquals("LLot10", nodes.get("testNode").getShortName());
    }

    @Test
    public void testUpdateEdge() throws SQLException {
        List<Node> nodes = new ArrayList<>();
        List<Edge> actual = new ArrayList<>();
        Edge target = new Edge("bPARK01201_bWALK00501", "test_start", "test_end");
        actual.add(target);
        db.loadDatabase(nodes, actual);

        String edgeID = target.getEdgeID();
        String startNode = "bPARK01201";
        String endNode = "bWALK00501";
        db.updateEdge(new Edge(edgeID, startNode, endNode));

        Map<String, Edge> edges = db.getEdges();
        assertEquals("bPARK01201", edges.get("bPARK01201_bWALK00501").getStartNodeName());
        assertEquals("bWALK00501", edges.get("bPARK01201_bWALK00501").getEndNodeName());
    }

    @Test
    public void testAddNode() throws SQLException {
        Node target = new Node("testNode",
                0,
                -992,
                1,
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");

        db.addNode(target);

        Map<String, Node> nodes = db.getNodes();
        assertEquals(0, nodes.get("testNode").getXCoord());
        assertEquals(-992, nodes.get("testNode").getYCoord());
        assertEquals(1, nodes.get("testNode").getFloor());
        assertEquals("test_building", nodes.get("testNode").getBuilding());
        assertEquals("NODETYPE", nodes.get("testNode").getNodeType());
        assertEquals("Name With Many Spaces", nodes.get("testNode").getLongName());
        assertEquals("N W M S", nodes.get("testNode").getShortName());
    }

    @Test
    public void testAddEdge() throws SQLException {
        Edge target = new Edge("bPARK01201_bWALK00501", "test_start", "test_end");

        db.addEdge(target);

        Map<String, Edge> edges = db.getEdges();
        assertEquals("test_start", edges.get("bPARK01201_bWALK00501").getStartNodeName());
        assertEquals("test_end", edges.get("bPARK01201_bWALK00501").getEndNodeName());
    }

    @Test
    public void testRemoveNode() throws SQLException {
        Node target = new Node("testNode",
                0,
                -992,
                1,
                "test_building",
                "NODETYPE",
                "Name With Many Spaces",
                "N W M S");

        db.addNode(target);
        assertTrue(!db.getNodes().isEmpty());

        db.removeNode(target.getNodeID());
        assertTrue(db.getNodes().isEmpty());
    }

    @Test
    public void testRemoveEdge() throws SQLException {
        Edge target = new Edge("bPARK01201_bWALK00501", "test_start", "test_end");

        db.addEdge(target);
        assertTrue(!db.getEdges().isEmpty());

        db.removeEdge(target.getEdgeID());
        assertTrue(db.getEdges().isEmpty());
    }
}
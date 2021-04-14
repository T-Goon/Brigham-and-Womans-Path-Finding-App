package edu.wpi.teamB;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.CSVHandler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PathfindingTests {

    private static DatabaseHandler db;

    @BeforeAll
    static void initDB() {
        db = DatabaseHandler.getDatabaseHandler("test.db");
        List<Node> nodes = CSVHandler.loadCSVNodes(App.NODES_PATH);
        List<Edge> edges = CSVHandler.loadCSVEdges(App.EDGES_PATH);
        try {
            db.loadDatabase(nodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adjNodes() {
        //Testing only one adj node
        List<Node> adjNodes = new ArrayList<>();
        Node bWALK00101 = new Node("bWALK00101", 568, 1894, 1, "Parking", "WALK", "Left Parking Lot Walkway", "Llot Walk");
        adjNodes.add(bWALK00101);
        assertEquals(adjNodes, Graph.getGraph(db).getAdjNodesById("bPARK00101"));

        //testing bidirectional and multiple adj nodes
        List<Node> adjNodes3 = new ArrayList<>();

        /*
        * bWALK00301_bWALK00401,bWALK00301,bWALK00401
        bWALK00401_bEXIT00101,bWALK00401,bEXIT00101
        bWALK00401_bEXIT00201,bWALK00401,bEXIT00201
        * */

        Node bWALK00301 = new Node("bWALK00301", 1874, 1611, 1, "Parking", "WALK", "Francis Street Vining Street Intersection", "FrancisViningInt");
        Node bEXIT00101 = new Node("bEXIT00101", 1749, 1389, 1, "Parking", "EXIT", "75 Francis Lobby Entrance", "FrancisLobbyEnt");
        Node bEXIT00201 = new Node("bEXIT00201", 2061, 1390, 1, "Parking", "EXIT", "Emergency Entrance", "EmergencyEnt");

        adjNodes3.add(bWALK00301);
        adjNodes3.add(bEXIT00101);
        adjNodes3.add(bEXIT00201);

        assertTrue(Graph.getGraph(db).getAdjNodesById("bWALK00401").containsAll(adjNodes3));

    }

    @Test
    public void testDist() {
        Node start = new Node("bWALK00301", 3, 0, 1, "Parking", "WALK", "Francis Street Vining Street Intersection", "FrancisViningInt");
        Node end = new Node("bEXIT00101", 0, 4, 1, "Parking", "EXIT", "75 Francis Lobby Entrance", "FrancisLobbyEnt");
        assertEquals(5.0, Graph.dist(start, end));
    }

    @Test
    public void testAStar() {
        LinkedList<String> expectedPath = new LinkedList<>();
        expectedPath.add("bPARK00101");
        expectedPath.add("bWALK00101");
        expectedPath.add("bWALK00201");
        expectedPath.add("bWALK00301");
        expectedPath.add("bWALK00501");
        expectedPath.add("bWALK00601");
        expectedPath.add("bWALK00701");
        expectedPath.add("bWALK00801");
        expectedPath.add("bWALK00901");
        expectedPath.add("bPARK02501");
        List<String> path = AStar.findPath("bPARK00101", "bPARK02501", true);
        assertEquals(expectedPath, path);
    }

}

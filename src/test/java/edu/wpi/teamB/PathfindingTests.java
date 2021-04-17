package edu.wpi.teamB;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PathfindingTests {

    private static DatabaseHandler db;

    @BeforeAll
    static void initDB() {
        db = DatabaseHandler.getDatabaseHandler("test.db");
        List<Node> nodes = CSVHandler.loadCSVNodes("/edu/wpi/teamB/csvFiles/bwBnodes.csv");
        List<Edge> edges = CSVHandler.loadCSVEdges("/edu/wpi/teamB/csvFiles/bwBedges.csv");

        db.loadDatabase(nodes, edges);

        Graph.setGraph(db);

    }

    @Test
    public void adjNodes() {
        //Testing only one adj node
        List<Node> adjNodes = new ArrayList<>();
        Node bWALK00101 = new Node("bWALK00101", 568, 1894, "1", "Parking", "WALK", "Left Parking Lot Walkway", "Llot Walk");
        adjNodes.add(bWALK00101);
        assertEquals(adjNodes, Graph.getGraph().getAdjNodesById("bPARK00101"));
    }

    @Test
    public void testDist() {
        Node start = new Node("bWALK00301", 3, 0, "1", "Parking", "WALK", "Francis Street Vining Street Intersection", "FrancisViningInt");
        Node end = new Node("bEXIT00101", 0, 4, "1", "Parking", "EXIT", "75 Francis Lobby Entrance", "FrancisLobbyEnt");
        assertEquals(5.0, Graph.dist(start, end));
    }

    @Test
    public void testAStar() {
        LinkedList<String> expectedPath = new LinkedList<>();
        expectedPath.add("bPARK00101");
        expectedPath.add("bWALK00101");
        expectedPath.add("bWALK00201");
        expectedPath.add("bWALK00301");
        expectedPath.add("bWALK00401");
        expectedPath.add("bWALK00501");
        expectedPath.add("bWALK00601");
        expectedPath.add("bWALK00701");
        expectedPath.add("bWALK00801");
        expectedPath.add("bWALK01001");
        expectedPath.add("bWALK01101");
        expectedPath.add("bWALK01201");
        expectedPath.add("bWALK01301");
        expectedPath.add("bWALK01401");
        expectedPath.add("bWALK01501");
        expectedPath.add("bWALK01601");
        expectedPath.add("bPARK02501");

        List<String> path = AStar.findPath("bPARK00101", "bPARK02501");
        assertEquals(expectedPath, path);
    }

    @Test
    public void testClosestPath() {

        Node bWALK00101 = new Node("bWALK00101", 568, 1894, "1", "Parking", "WALK", "Left Parking Lot Walkway", "Llot Walk");
        List<String> pathExp = new LinkedList();

        List<Node> category = new ArrayList<>();
        Node bPARK02301 = new Node("bPARK02301",3362,1254,"1","Parking","PARK","Right Parking Lot Spot 13","RLot13");
        Node bPARK02401 = new Node("bPARK02401",3365,1280,"1","Parking","PARK","Right Parking Lot Spot 14","RLot14");
        Node bPARK02501 = new Node("bPARK02501",3364,1307,"1","Parking","PARK","Right Parking Lot Spot 15","RLot15");

        category.add(bPARK02301);
        category.add(bPARK02401);
        category.add(bPARK02501);

        pathExp.add("bWALK00101");
        pathExp.add("bWALK00201");
        pathExp.add("bWALK00301");
        pathExp.add("bWALK00401");
        pathExp.add("bWALK00501");
        pathExp.add("bWALK00601");
        pathExp.add("bWALK00701");
        pathExp.add("bWALK00801");
        pathExp.add("bWALK01001");
        pathExp.add("bWALK01101");
        pathExp.add("bWALK01201");
        pathExp.add("bWALK01301");
        pathExp.add("bWALK01401");
        pathExp.add("bWALK01501");
        pathExp.add("bWALK01601");
        pathExp.add("bWALK02301");

        /*
        * [bWALK00101, bWALK00201, bWALK00301, bWALK00401, bWALK00501, bWALK00601, bWALK00701, bWALK00801, bWALK01001, bWALK01101, bWALK01201, bWALK01301, bWALK01401, bWALK01501, bWALK01601, bPARK02301]
        * */

        List<String> path = AStar.closestPath(bWALK00101, category);
        assertEquals(pathExp, path);


    }

}

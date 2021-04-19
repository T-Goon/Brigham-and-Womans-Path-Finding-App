package edu.wpi.teamB;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.Path;
import edu.wpi.teamB.entities.map.Edge;
import edu.wpi.teamB.entities.map.Node;
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

        db.loadNodesEdges(nodes, edges);

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

        Path path = AStar.findPath("bPARK00101", "bPARK02501");
        assertEquals(expectedPath, path.getPath());
    }

    @Test
    public void testClosestPath() {

        List<String> pathExp = new LinkedList<>();

        List<Node> category = new ArrayList<>();

        Node bPARK01501 = new Node("bPARK01501",3159,1228,"1","Parking","PARK","Right Parking Lot Spot 5","RLot5");
        Node bPARK01601 = new Node("bPARK01601",3161,1251,"1","Parking","PARK","Right Parking Lot Spot 6","RLot6");
        Node bPARK01701 = new Node("bPARK01701",3160,1278,"1","Parking","PARK","Right Parking Lot Spot 7","RLot7");

        category.add(bPARK01501);
        category.add(bPARK01601);
        category.add(bPARK01701);

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
        pathExp.add("bPARK01701");


        Path path = AStar.shortestPathToNodeInList("bWALK00101", category);

        assertEquals(pathExp, path.getPath());

    }

    @Test
    public void testGetEstimatedTime(){
        Path tempPath = AStar.findPath("bPARK01801", "bPARK00601");
        String result = AStar.getEstimatedTime(tempPath);
        String expected = "5:22 min";
        assertEquals(expected, result);

        tempPath = AStar.findPath("FSERV00201", "GEXIT00101");
        result = AStar.getEstimatedTime(tempPath);
        expected = "14:11 min";
        assertEquals(expected, result);

        tempPath = AStar.findPath("bEXIT00401", "bEXIT00501");
        result = AStar.getEstimatedTime(tempPath);
        expected = "22 sec";
        assertEquals(expected, result);
    }

}

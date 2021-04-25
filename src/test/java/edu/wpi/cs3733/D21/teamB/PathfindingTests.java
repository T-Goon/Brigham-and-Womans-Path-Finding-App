package edu.wpi.cs3733.D21.teamB;


import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.AStar;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PathfindingTests {

    @BeforeAll
    static void initDB() {
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("test.db");
        List<Node> nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBnodes.csv");
        List<Edge> edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBedges.csv");

        try {
            db.loadNodesEdges(nodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

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

    @Test
    public void testMultipleNodePathfinding(){
        Stack<String> nodeList = new Stack<>();
        nodeList.push("bPARK02501");
        nodeList.push("bEXIT00501");
        nodeList.push("bPARK00101");

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
        expectedPath.add("bWALK00901");
        expectedPath.add("bEXIT00501");
        expectedPath.add("bWALK00901");
        expectedPath.add("bWALK00801");
        expectedPath.add("bWALK01001");
        expectedPath.add("bWALK01101");
        expectedPath.add("bWALK01201");
        expectedPath.add("bWALK01301");
        expectedPath.add("bWALK01401");
        expectedPath.add("bWALK01501");
        expectedPath.add("bWALK01601");
        expectedPath.add("bPARK02501");


        Path expectedResult = new Path(expectedPath, 2484.102858858675+1954.7029936098086);

        Path returnedResult = AStar.findPath(nodeList);

        assertEquals(expectedResult, returnedResult);

        Stack<String> longNodesList = new Stack<>();
        longNodesList.push("FDEPT00301");
        longNodesList.push("FSERV00201");
        longNodesList.push("bEXIT00501");
        longNodesList.push("bPARK00101");


        LinkedList<String> expectedLongPath = new LinkedList<>();
        expectedLongPath.add("bPARK00101");
        expectedLongPath.add("bWALK00101");
        expectedLongPath.add("bWALK00201");
        expectedLongPath.add("bWALK00301");
        expectedLongPath.add("bWALK00401");
        expectedLongPath.add("bWALK00501");
        expectedLongPath.add("bWALK00601");
        expectedLongPath.add("bWALK00701");
        expectedLongPath.add("bWALK00801");
        expectedLongPath.add("bWALK00901");
        expectedLongPath.add("bEXIT00501");
        expectedLongPath.add("bEXIT00401");
        expectedLongPath.add("FEXIT00201");
        expectedLongPath.add("FHALL02801");
        expectedLongPath.add("FHALL02201");
        expectedLongPath.add("FHALL02101");
        expectedLongPath.add("FHALL01901");
        expectedLongPath.add("FHALL01601");
        expectedLongPath.add("FHALL01501");
        expectedLongPath.add("FHALL01401");
        expectedLongPath.add("FSERV00201");
        expectedLongPath.add("FHALL01401");
        expectedLongPath.add("FHALL01501");
        expectedLongPath.add("FHALL01601");
        expectedLongPath.add("FHALL03201");
        expectedLongPath.add("FHALL01801");
        expectedLongPath.add("FHALL01701");
        expectedLongPath.add("FDEPT00301");

        Path expectedLongResult = new Path(expectedLongPath, 2484.102858858675+848.2401435306502+350.0);

        Path returnedLongResult = AStar.findPath(longNodesList);

        assertEquals(expectedLongResult, returnedLongResult);


    }

}

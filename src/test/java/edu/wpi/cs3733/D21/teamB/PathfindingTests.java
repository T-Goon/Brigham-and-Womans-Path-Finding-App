package edu.wpi.cs3733.D21.teamB;


import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.AStar;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
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

        Node bPARK01501 = new Node("bPARK01501", 3159, 1228, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node bPARK01601 = new Node("bPARK01601", 3161, 1251, "1", "Parking", "PARK", "Right Parking Lot Spot 6", "RLot6");
        Node bPARK01701 = new Node("bPARK01701", 3160, 1278, "1", "Parking", "PARK", "Right Parking Lot Spot 7", "RLot7");

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
    public void testGetEstimatedTime() {
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
    public void testMultipleNodePathfinding() {
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


        Path expectedResult = new Path(expectedPath, 2484.102858858675 + 1954.7029936098086);

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

        Path expectedLongResult = new Path(expectedLongPath, 2484.102858858675 + 848.2401435306502 + 350.0);

        Path returnedLongResult = AStar.findPath(longNodesList);

        assertEquals(expectedLongResult, returnedLongResult);


    }

    @Test
    public void testing180Angles() {
        Node a = new Node("bPARK01501", 3159, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b = new Node("bPARK01501", 3159, 2, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c = new Node("bPARK01501", 3159, 3, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double angle180 = Directions.angleBetweenEdges(a, b, c);
        assertEquals(0, angle180, .5);


        Node a1 = new Node("bPARK01501", 3159, 3159, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b1 = new Node("bPARK01501", 5, 3159, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c1 = new Node("bPARK01501", 1, 3159, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        angle180 = Directions.angleBetweenEdges(a1, b1, c1);
        assertEquals(0, angle180, .5);


        Node a2 = new Node("bPARK01501", 5, 5, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b2 = new Node("bPARK01501", 8, 8, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c2 = new Node("bPARK01501", 13, 13, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        angle180 = Directions.angleBetweenEdges(a2, b2, c2);
        assertEquals(0, angle180, .5);
    }

    @Test
    public void testing0Angles() {
        Node a1 = new Node("bPARK01501", 3159, 3159, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b1 = new Node("bPARK01501", 5, 3159, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c1 = new Node("bPARK01501", 1, 3159, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double angle0 = Directions.angleBetweenEdges(c1, b1, a1);
        assertEquals(0, angle0, .5);
    }

    @Test
    public void testing90and270Angles() {

        //is 270 taking a left
        Node a1 = new Node("bPARK01501", 1, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b1 = new Node("bPARK01501", 1, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c1 = new Node("bPARK01501", 0, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double angle270 = Directions.angleBetweenEdges(a1, b1, c1);
        assertEquals(-90, angle270, 0);

        //is 270
        Node a2 = new Node("bPARK01501", 0, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b2 = new Node("bPARK01501", 0, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c2 = new Node("bPARK01501", 1, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double angle270s = Directions.angleBetweenEdges(a2, b2, c2);
        assertEquals(-90, angle270s, 0);


        //take a right
        Node a = new Node("bPARK01501", 0, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b = new Node("bPARK01501", 0, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c = new Node("bPARK01501", 1, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double angle90 = Directions.angleBetweenEdges(a, b, c);
        assertEquals(90, angle90, 0);

        //take a right 90 degrees this wrong
        Node a3 = new Node("bPARK01501", 1, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b3 = new Node("bPARK01501", 1, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c3 = new Node("bPARK01501", 0, 1, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double angle90s = Directions.angleBetweenEdges(a3, b3, c3);
        assertEquals(90, angle90s, 0);

    }

    @Test
    public void testSlightRights(){
        Node a = new Node("bPARK01501", 5, 5, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b = new Node("bPARK01501", 5, 3, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c = new Node("bPARK01501", 4, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double slightRight = Directions.angleBetweenEdges(a, b, c);
        assertEquals(-20, slightRight, 2);
    }

    @Test
    public void testSlightLeft(){
        Node a = new Node("bPARK01501", 0, 0, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node b = new Node("bPARK01501", 0, 2, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");
        Node c = new Node("bPARK01501", 1, 3, "1", "Parking", "PARK", "Right Parking Lot Spot 5", "RLot5");

        double slightRight = Directions.angleBetweenEdges(a, b, c);
        System.out.println(slightRight);
        //assertEquals(-20, slightRight, 2);
    }

    @Test
    public void testSimplePath() {
        Path path = AStar.findPath("FDEPT00101", "FSERV00201");
        List<String> idExpected = new ArrayList<>();
        idExpected.add("FDEPT00101"); //1617,825
        idExpected.add("FHALL01301"); //1627,825
        idExpected.add("FHALL01401"); //1627,1029
        idExpected.add("FSERV00201"); //1605,1029

        Path pathCheck = new Path();
        pathCheck.setPath(idExpected);
        pathCheck.setTotalPathCost(path.getTotalPathCost());
        List<String> simplePath = Directions.simplifyPath(path);
        assertEquals(idExpected, simplePath);

        Path path1 = AStar.findPath("FINFO00101", "WELEV00L01");
        List<String> idExpected1 = new ArrayList<>();
        idExpected1.add("FINFO00101");
        idExpected1.add("FHALL02901");
        idExpected1.add("FHALL02201");
        idExpected1.add("FHALL00701");
        idExpected1.add("WELEV00L01");

        Path pathCheck1 = new Path();
        pathCheck1.setPath(idExpected1);
        pathCheck1.setTotalPathCost(path1.getTotalPathCost());
        List<String> simplePath1 = Directions.simplifyPath(pathCheck1);
        assertEquals(idExpected1, simplePath1);
    }

    @Test
    public void testTextDir() {
        //Center for International Medicine
        //Tower Medical Cashier

        Path path = AStar.findPath("FDEPT00101", "FSERV00201");
        List<String> instructions = Directions.instructions(path);

        for (String inst : instructions) {
            System.out.println(inst);
        }

    }


    @Test
    public void test() {
        LinkedList<String> expectedPath = new LinkedList<>();
        Path path = AStar.findPath("FDEPT00501", "BCONF00102");
        System.out.println(path.getPath());

        //FHALL00701, WELEV00L01, WELEV00L02
        Node n = new Node("FHALL00701",1758,930,"1","Tower","HALL","Tower Elevator Entrance","Hallway F00701");
        Node n1 = new Node("WELEV00L01",1810,930,"1","Tower","ELEV","Elevator L Floor 1","Elevator L1");
        Node n2 = new Node("WELEV00L02",1805,925,"2","Tower","ELEV","Elevator L Floor 2","Elevator L2");
        System.out.println(Directions.angleBetweenEdges(n, n1, n2));
    }

}

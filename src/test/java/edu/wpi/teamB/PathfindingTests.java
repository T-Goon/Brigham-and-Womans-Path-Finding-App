package edu.wpi.teamB;

import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.pathfinding.Read;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PathfindingTests {
    @Test
    public void adjNodes() {
        //Testing only one adj node
        List<Node> adjNodes = new ArrayList<>();
        Node bWALK00501 = new Node("bWALK00501", 1872, 1965, 1, "Parking", "WALK", "Vining Street Walkway", "ViningWalk");
        adjNodes.add(bWALK00501);
        assertEquals(adjNodes, Graph.findAdjNodes("bPARK02501"));

        //testing bidirectional and multiple adj nodes
        List<Node> adjNodes3 = new ArrayList<>();

        /*
        * bWALK00301_bWALK00401,bWALK00301,bWALK00401
        bWALK00401_bEXIT00101,bWALK00401,bEXIT00101
        bWALK00401_bEXIT00201,bWALK00401,bEXIT00201
        * */

        Node bWALK00301 = new Node("bWALK00301",1874,1611,1,"Parking","WALK","Francis Street Vining Street Intersection","FrancisViningInt");
        Node bEXIT00101 = new Node("bEXIT00101",1749,1389,1,"Parking","EXIT","75 Francis Lobby Entrance","FrancisLobbyEnt");
        Node bEXIT00201 = new Node("bEXIT00201",2061,1390,1,"Parking","EXIT","Emergency Entrance","EmergencyEnt");

        adjNodes3.add(bWALK00301);
        adjNodes3.add(bEXIT00101);
        adjNodes3.add(bEXIT00201);

        assertEquals(adjNodes3, Graph.findAdjNodes("bWALK00401"));

    }

    @Test
    public void testDist(){
        Node start =  new Node("bWALK00301",3,0,1,"Parking","WALK","Francis Street Vining Street Intersection","FrancisViningInt");
        Node end = new Node("bEXIT00101",0,4,1,"Parking","EXIT","75 Francis Lobby Entrance","FrancisLobbyEnt");
        assertEquals(5.0, Graph.dist(start, end));
    }

//    @Test
//    public void populateHashmapTest(){
//
//        HashMap<String, Node> nodeMap = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBnodes.csv");
//        HashMap<String, Node> shortPathInfo = Graph.populateHashmap(nodeMap.values());
//
//        Node check = shortPathInfo.get("bWALK00301");
//        assertEquals(check.getXCoord(), 1874);
//        assertEquals(check.getYCoord(), 1611);
//        assertEquals(check.isClosed(), false);
//        assertEquals(check.getFVal(), Double.MAX_VALUE);
//        assertEquals(check.getAccumWeight(), Double.MAX_VALUE);
//        assertEquals(check.getPrevNode(), null);
//    }

//    @Test
//    public void AstrHashTest() {
//        Node bWALK00301 = new Node("bWALK00301",1874,1611,1,"Parking","WALK","Francis Street Vining Street Intersection","FrancisViningInt");
//        Node bWALK00201 = new Node("bEXIT00101",1749,1389,1,"Parking","EXIT","75 Francis Lobby Entrance","FrancisLobbyEnt");
//
//        //HashMap<String, Node> test = Graph.AStrHashTable("bWALK00301", "bWALK00201");
//
//        Node nodes = Graph.getNode("bWALK00301", "bWALK00201");
//        assertEquals(nodes, bWALK00301);
//
//    }


//    @Test
//    public void testAStar(){
//
//        List<String> path = Graph.AStr("bPARK00101", "bPARK02501");
//        System.out.println();
//
//    }

    @Test
    public void testAStar(){
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
        LinkedList<String> path = AStar.findPath("bPARK00101", "bPARK02501");
        assertEquals(expectedPath, path);
    }

}

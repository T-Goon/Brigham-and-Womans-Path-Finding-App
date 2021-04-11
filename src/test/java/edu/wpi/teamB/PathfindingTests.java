package edu.wpi.teamB;

import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.pathfinding.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathfindingTests {

    @Test
    public void hashMapTest() {
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

}

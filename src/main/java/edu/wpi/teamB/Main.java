package edu.wpi.teamB;

import edu.wpi.teamB.pathfinding.Graph;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // App.launch(App.class, args);

        //  List<String> path = Graph.AStr("bPARK01001", "bWALK00101");
        List<String> path = Graph.AStr("bPARK00101", "bPARK02501");
//       List<String> path2 = Graph.AStr("bWALK00901", "bPARK02401");
//        List<String> path = Graph.AStr("bPARK02501", "bWALK00801");


        //List<String> path = Graph.AStr("bWALK00201", "bPARK01101");

        System.out.println(path);

    }
}

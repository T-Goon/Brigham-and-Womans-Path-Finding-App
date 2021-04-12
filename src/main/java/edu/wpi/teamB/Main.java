package edu.wpi.teamB;
import edu.wpi.teamB.pathfinding.Graph;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       // App.launch(App.class, args);

     //  List<String> path = Graph.AStr("bPARK01001", "bWALK00101");
       List<String> path = Graph.AStr("bPARK02501", "bWALK00401"); //  breaking
        //List<String> path = Graph.AStr("bWALK00201", "bPARK01101");

        for(String ids: path){
            System.out.println(path);
        }

    }
}

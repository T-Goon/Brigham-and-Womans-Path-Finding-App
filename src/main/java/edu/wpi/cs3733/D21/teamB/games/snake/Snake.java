package edu.wpi.cs3733.D21.teamB.games.snake;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.Coord;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;

import java.sql.SQLException;
import java.util.List;

public class Snake {

    private static List<Node> nodes;
    private static List<Edge> edges;
    private static int snakeSize;
    private static Coord snakeHeadLoc;
    private static List<Coord> appleLocationList;

    public static void initializeMap() {
        // Load nodes and edges for game
        nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeNodes.csv");
        edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeEdges.csv");
        try {
            DatabaseHandler.getHandler().loadNodesEdges(nodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeGame() {
        // set the snake at a starting location
        Node start = nodes.get(0);
        // restart size of snake
        snakeSize = 1;
        // call placeApple()
    }

    public static void placeApple() {
        // Randomly select a node to place the apple at
        int index = (int) (Math.random() * nodes.size());
        Node appleNode = nodes.get(index);
        Coord appleCoord = new Coord(appleNode.getXCoord(), appleNode.getYCoord());
        appleLocationList.add(appleCoord);
    }

    public static void checkApple() {
        // Check if the current node location is the location of the apple
        // Add to length of snake and call placeApple()
        if(appleLocationList.contains(snakeHeadLoc)){
            //delete the apple off of the map
            appleLocationList.remove(snakeHeadLoc);
            snakeSize++;
        }
        placeApple();
    }
}

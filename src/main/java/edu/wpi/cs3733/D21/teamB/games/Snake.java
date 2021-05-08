package edu.wpi.cs3733.D21.teamB.games;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Snake {

    private static List<Node> nodes;
    private static List<Edge> edges;
    private static int snakeSize;
    private static Node appleLocation;

    public static void initializeMap(MapDrawer mapDrawer) {
        // Load nodes and edges
        nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeNodes.csv");
        edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeEdges.csv");
        try {
            DatabaseHandler.getHandler().loadNodesEdges(nodes, edges);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mapDrawer.drawAllElements();
        mapDrawer.drawEdgesOnFloor();
    }

    public static void initializeGame() {
        // set the snake at a starting location
        Node snake = nodes.get(0);
//        ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/node.fxml")));
        // restart size of snake
        snakeSize = 1;
        // call placeApple()
    }

    public static void placeApple() {
        // Randomly select a node to place the apple at
        int index = (int) (Math.random() * nodes.size());
        Node apple = nodes.get(index);
    }

    public static void checkApple() {
        // Check if the current node location is the location of the apple
        // Add to length of snake and call placeApple()
    }
}

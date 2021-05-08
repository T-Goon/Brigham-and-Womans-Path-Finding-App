package edu.wpi.cs3733.D21.teamB.games.snake;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import edu.wpi.cs3733.D21.teamB.entities.map.Coord;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Snake {

    private static List<Node> nodes;
    private static List<Edge> edges;
    private static int snakeSize;
    private static Coord snakeHeadLoc;
    private static Coord appleCoord;

    public static void initializeMap(MapDrawer mapDrawer, MapCache mapCache, AnchorPane nodeHolder) {
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
        initializeGame(mapCache, nodeHolder);
    }

    public static void initializeGame(MapCache mapCache, AnchorPane nodeHolder) {
        // set the snake at a starting location
        Node snake = nodes.get(32);
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/snake.fxml")));
            i.setLayoutX((snake.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitWidth() / 4));
            i.setLayoutY((snake.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitHeight()));
            nodeHolder.getChildren().add(i);
            mapCache.getNodePlaced().add(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // restart size of snake
        snakeSize = 1;
        // call placeApple()
    }

    public static void placeApple() {
        // Randomly select a node to place the apple at
        int index = (int) (Math.random() * nodes.size());
        Node appleNode = nodes.get(index);
        appleCoord = new Coord(appleNode.getXCoord(), appleNode.getYCoord());
    }

    public static void checkApple() {
        // Check if the current node location is the location of the apple
        // Add to length of snake and call placeApple()
        if(appleCoord.equals(snakeHeadLoc)){
            //delete the apple off of the map
            snakeSize++;
            placeApple();

        }
    }

    public static void moveSnake(){
        //
    }
}

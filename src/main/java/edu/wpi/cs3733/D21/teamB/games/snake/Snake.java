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

    private List<Node> nodes;
    private List<Edge> edges;
    private MapDrawer mapDrawer;
    private MapCache mapCache;
    private AnchorPane nodeHolder;
    private int snakeSize;
    private Coord snakeHeadLoc;
    private Coord appleCoord;

    public Snake(MapDrawer mapDrawer, MapCache mapCache, AnchorPane nodeHolder) {
        this.mapDrawer = mapDrawer;
        this.mapCache = mapCache;
        this.nodeHolder = nodeHolder;
    }

    /**
     * Display the nodes and edges for the game on the map and start the game
     */
    public void initializeMap() {
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
        initializeGame();
    }

    /**
     * Set the starting locations for the snake and apple
     */
    public void initializeGame() {
        // Set the snake at a starting location (Francis Lobby Entrance)
        Node snake = nodes.get(122);
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/snake.fxml")));
            i.setLayoutX((snake.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitWidth() / 4));
            i.setLayoutY((snake.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitHeight()));
            nodeHolder.getChildren().add(i);
            mapCache.getNodePlaced().add(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Restart size of snake
        snakeSize = 1;
        placeApple();
    }

    /**
     * Randomly select a location for the apple
     */
    public void placeApple() {
        // Randomly select a node to place the apple at
        int index = (int) (Math.random() * nodes.size());
        Node appleNode = nodes.get(index);
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/apple.fxml")));
            i.setLayoutX((appleNode.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitWidth() / 4));
            i.setLayoutY((appleNode.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitHeight()));
            nodeHolder.getChildren().add(i);
            mapCache.getNodePlaced().add(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

        appleCoord = new Coord(appleNode.getXCoord(), appleNode.getYCoord());
    }

    /**
     * Check if the current location is the location of the apple
     */
    public void checkApple() {
        // Add to length of snake and place apple
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

package edu.wpi.cs3733.D21.teamB.games.snake;

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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Snake extends JPanel implements ActionListener {

    private List<Node> nodes;
    private List<Edge> edges;
    private MapDrawer mapDrawer;
    private MapCache mapCache;
    private AnchorPane nodeHolder;
    private int snakeSize;
    private Coord snakeHeadLoc;
    private Coord appleCoord;
    boolean left;
    boolean right;
    boolean up;
    boolean down;
    private int x[] = new int[snakeSize];
    private int y[] = new int[snakeSize];
    private int DOT_SIZE = 10;

    public Snake(MapDrawer mapDrawer, MapCache mapCache, AnchorPane nodeHolder) {
        this.mapDrawer = mapDrawer;
        this.mapCache = mapCache;
        this.nodeHolder = nodeHolder;
        this.addKeyListener(new MyKeyAdapter());

    }

    public void cleanCSV(){

        List<String> idsToUse = new ArrayList<>();

        nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeNodes.csv");
        for(int i=0; i<nodes.size(); i++){
            Node node = nodes.get(i);
            if(!node.getFloor().equals("1")){
                nodes.remove(node);
                System.out.println("REMOVED " + node.getNodeID());
            }
            else{
                idsToUse.add(node.getNodeID());
            }
        }

        edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeEdges.csv");
        edges.removeIf(edge -> !idsToUse.contains(edge.getStartNodeID()) && !idsToUse.contains(edge.getEdgeID()));

        //putting the correct values into the CSV
        StringBuilder sb = new StringBuilder();
        sb.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n");
        for (Node n : nodes) {
            sb.append(n.getNodeID()).append(",")
                    .append(n.getXCoord()).append(",")
                    .append(n.getYCoord()).append(",")
                    .append(n.getFloor()).append(",")
                    .append(n.getBuilding()).append(",")
                    .append(n.getNodeType()).append(",")
                    .append(n.getLongName()).append(",")
                    .append(n.getShortName()).append("\n");
        }

        // Write to the file
        try {
            FileOutputStream out = new FileOutputStream(Paths.get("src/main/resources/edu/wpi/cs3733/D21/teamB/csvFiles/csvGames") + "/snakeNodes.csv");
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb1 = new StringBuilder();
        sb.append("edgeID,startNode,endNode\n");
        for (Edge e : edges) {
            sb1.append(e.getEdgeID()).append(",")
                    .append(e.getStartNodeID()).append(",")
                    .append(e.getEndNodeID()).append("\n");
        }

        // Write to the file
        try {
            FileOutputStream out = new FileOutputStream("src/main/resources/edu/wpi/cs3733/D21/teamB/csvFiles/csvGames" + "/snakeEdges.csv");
            out.write(sb1.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display the nodes and edges for the game on the map and start the game
     */
    public void initializeMap() {
        cleanCSV();
        // Load nodes and edges
        edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/csvGames/snakeEdges.csv");
        nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/csvGames/snakeNodes.csv");
        mapDrawer.drawAllElements();
        mapDrawer.drawEdgesOnFloor();
        initializeGame();
    }

    /**
     * Set the starting locations for the snake and apple
     */
    public void initializeGame() {
        // Set the snake at a starting location (Francis Lobby Entrance)
        Node snake = nodes. get(10);
        snakeHeadLoc = new Coord(snake.getXCoord(), snake.getYCoord());
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("")));
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

    public boolean isValid() {
        //if snake part is there it is invalid
        //if there is no adjacent edge to where you are travelling to it is invalid
        return true;
    } //stub

    @Override
    public void actionPerformed(ActionEvent e) {
        while(true) {
            move();
        }
    }

    public void move() {
        for (int z = snakeSize; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }

        if (right) {
            x[0] += DOT_SIZE;
        }

        if (up) {
            y[0] -= DOT_SIZE;
        }

        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int xVal = 0;
            int yVal = 0;


            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (isValid()) {
                        left = true;
                        right = false;
                        up = false;
                        down = false;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (isValid()) {
                        right = true;
                        left = false;
                        up = false;
                        down = false;
                    }
                    break;
                case KeyEvent.VK_W:
                    if (isValid()) {
                        left = false;
                        right = false;
                        up = true;
                        down = false;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (isValid()) {
                        left = false;
                        right = false;
                        up = false;
                        down = true;
                    }
                    break;
            }

            //this will go in a loop later because the size will grow
            try {
                ImageView i = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/snake.fxml")));
                i.setLayoutX((x[0] / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitWidth() / 4));
                i.setLayoutY((y[0] / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitHeight()));
                nodeHolder.getChildren().add(i);
                mapCache.getNodePlaced().add(i);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}

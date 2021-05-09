package edu.wpi.cs3733.D21.teamB.games.snake;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import edu.wpi.cs3733.D21.teamB.entities.map.Coord;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class Snake extends JPanel implements ActionListener {

    private List<Node> nodes;
    private List<Edge> edges;
    private MapDrawer mapDrawer;
    private MapCache mapCache;
    private AnchorPane nodeHolder;
    private Label score;
    private int snakeSize;

    @Getter
    private Coord snakeHeadLoc;

    private Coord appleCoord;
    boolean left;
    boolean right;
    boolean up;
    boolean down;
    private int x[] = new int[snakeSize];
    private int y[] = new int[snakeSize];
    private int DOT_SIZE = 10;
    private HashMap<String, List<Node>> adjMap;
    private HashMap<String, Node> idToNode;
    private Node snake;
    private Node appleNode;
    private ImageView imageView;
    private int appleCount;

    public Snake(MapDrawer mapDrawer, MapCache mapCache, AnchorPane nodeHolder, Label score) {
        this.mapDrawer = mapDrawer;
        this.mapCache = mapCache;
        this.nodeHolder = nodeHolder;
        this.score = score;
        this.addKeyListener(new MyKeyAdapter());
    }

    public void cleanCSV() {

        List<String> idsToUse = new ArrayList<>();
        List<Node> nodesToRemove = new ArrayList<>();

        nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeNodes.csv");
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (!node.getFloor().equals("1")) {
                nodesToRemove.add(node);
            } else {
                idsToUse.add(node.getNodeID());
            }
        }

        for (int i = 0; i < nodesToRemove.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (nodes.get(j).getNodeID().equals(nodesToRemove.get(i).getNodeID())) {
                    nodes.remove(nodesToRemove.get(i));
                }
            }
        }

        edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/snakeEdges.csv");
        edges.removeIf(edge -> !idsToUse.contains(edge.getStartNodeID()) || !idsToUse.contains(edge.getEndNodeID()));

        //putting the correct values into the CSV
        StringBuilder sb = new StringBuilder();
        sb.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n");
        for (Node n : nodes) {
            System.out.println("Node id " + n.getNodeID() + " with floor " + n.getFloor());
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
        sb1.append("edgeID,startNode,endNode\n");
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
    public void initializeMap(ImageView i) {
//        cleanCSV();
        // Load nodes and edges
        edges = CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/csvGames/snakeEdges.csv");
        nodes = CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/csvGames/snakeNodes.csv");
        mapDrawer.setPlayingSnake(true);
        mapDrawer.drawAllElements();

        Map<String, Node> nodeMap = new HashMap<String, Node>();
        for (Node node : nodes) {
            nodeMap.put(node.getNodeID(), node);
        }
        for (Edge edge : edges) {
            Node startNode = nodeMap.get(edge.getStartNodeID());
            Node endNode = nodeMap.get(edge.getEndNodeID());
            mapDrawer.placeEdgesForGame(startNode, endNode);
        }

        initializeGame(i);
        mapDrawer.setPlayingSnake(false);
    }

    /**
     * Set the starting locations for the snake and apple
     */
    public void initializeGame(ImageView i) {
        // Set the snake at a starting location (Francis Lobby Entrance)
        snake = nodes.get(10);
        snakeHeadLoc = new Coord(snake.getXCoord(), snake.getYCoord());

        i.setLayoutX((snake.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitWidth() / 4));
        i.setLayoutY((snake.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitHeight()));
        nodeHolder.getChildren().add(i);
        mapCache.getNodePlaced().add(i);

        // Restart size of snake
        snakeSize = 1;
        placeApple();
        appleCount = 0;
    }

    /**
     * Randomly select a location for the apple
     */
    public void placeApple() {
        // Randomly select a node to place the apple at
        int index = (int) (Math.random() * nodes.size());
        appleNode = nodes.get(index);
        try {
            imageView = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/apple.fxml")));
            imageView.setLayoutX((appleNode.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - (imageView.getFitWidth() / 4));
            imageView.setLayoutY((appleNode.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - (imageView.getFitHeight()));
            nodeHolder.getChildren().add(imageView);
            mapCache.getNodePlaced().add(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        appleCoord = new Coord(appleNode.getXCoord(), appleNode.getYCoord());
    }

    /**
     * Check if the current location is the location of the apple
     */
    public void checkApple() {
        if ((snakeHeadLoc.getX() <= (appleCoord.getX() + 40) && snakeHeadLoc.getX() >= (appleCoord.getX() - 40))
                && (snakeHeadLoc.getY() <= (appleCoord.getY() + 40) && snakeHeadLoc.getY() >= (appleCoord.getY() - 40))) {
            // Remove apple from previous location
            nodeHolder.getChildren().remove(imageView);
            mapCache.getNodePlaced().remove(imageView);
            appleCount++;
            score.setText("Score: " + appleCount);

            // Place apple in new location
            placeApple();
        }
    }

    public void LoadIDToNode(){
        for(Node node: nodes){
            idToNode.put(node.getNodeID(), node);
        }
    }

    public void loadAdjMap() {

        for (int i=0; i<edges.size(); i++) {

            if (!adjMap.containsKey(edges.get(i).getStartNodeID())) {
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(idToNode.get(edges.get(i).getEndNodeID()));
                adjMap.put(edges.get(i).getStartNodeID(), tempList);
            } else {
                adjMap.get(edges.get(i).getStartNodeID()).add(idToNode.get(edges.get(i).getEndNodeID()));
            }

            if (!adjMap.containsKey(edges.get(i).getEndNodeID())) {
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(idToNode.get(edges.get(i).getStartNodeID()));
                adjMap.put(edges.get(i).getEndNodeID(), tempList);
            } else {
                adjMap.get(edges.get(i).getEndNodeID()).add(idToNode.get(edges.get(i).getStartNodeID()));
            }
        }
    }


    public boolean isValid() {

        //did snake hit itself
//        for(int i=0; i<x.length; i++){
//            if(x[i] == snakeHeadLoc.getX() && y[i] == snakeHeadLoc.getY()){
//                return true;
//            }
//        }



        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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

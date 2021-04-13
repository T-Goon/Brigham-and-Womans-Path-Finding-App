package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class PathfindingMenuController implements Initializable {

    @FXML
    private JFXComboBox<String> startLocComboBox;

    @FXML
    private JFXComboBox<String> endLocComboBox;

    @FXML
    private AnchorPane nodeHolder;

    @FXML
    private AnchorPane mapHolder;

    @FXML
    private ImageView map;

    @FXML
    private JFXButton btnFindPath;

    @FXML
    private JFXButton btnBack;

    private static final double coordinateScale = 10 / 3.0;
    private List<Line> edgePlaced = new ArrayList<>();
    private final HashMap<String, List<Node>> floorNodes = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, Node> locations = Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db")).getNodes();
        List<String> locationNames = new ArrayList<>();

        //Adds all the destination names to locationNames and sort the nodes by floor
        for (Node n : locations.values()) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
                locationNames.add(n.getLongName());
            }

            if (floorNodes.containsKey(n.getFloor())) {
                floorNodes.get(n.getFloor()).add(n);
            } else {
                ArrayList<Node> tempList = new ArrayList<>();
                tempList.add(n);
                floorNodes.put(n.getFloor(), tempList);
            }
        }

        //Populate the Combo Boxes with valid locations (Sorted)
        Collections.sort(locationNames);
        startLocComboBox.getItems().addAll(locationNames);
        endLocComboBox.getItems().addAll(locationNames);

        drawNodesOnFloor("1");
    }

    /**
     * Draws all the nodes and intermediate nodes on a given floor
     * @param floorID the floor id for the nodes "LL", "L", "G", "1", "2", "3"
     */
    private void drawNodesOnFloor(String floorID) {
        for (Node n : floorNodes.get(floorID)) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
                placeNode(n.getXCoord(), n.getYCoord());
            } else {
                placeIntermediateNode(n.getXCoord(), n.getYCoord());
            }
        }
    }


    private Map<String, String> longNameID() {
        Map<String, Node> nodesId = Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db")).getNodes();
        Map<String, String> longName = new HashMap<>();

        for (Node node : nodesId.values()) {
            longName.put(node.getLongName(), node.getNodeID());
        }
        return longName;
    }

    private void drawPath() {
        Map<String, Node> nodesId = Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db")).getNodes();
        Map<String, String> hmLongName = longNameID();
        List<String> AstrPath = AStar.findPath(hmLongName.get(getStartLocation()), hmLongName.get(getEndLocation()));

        Node prev = null;
        Node curr;
        for (String loc : AstrPath) {

            if ((prev != null) && (loc != null)) {
                curr = nodesId.get(loc);
                placeEdge(prev.getXCoord(), prev.getYCoord(), curr.getXCoord(), curr.getYCoord());
            }
            prev = nodesId.get(loc);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":
                // Remove old path
                for (Line l : edgePlaced)
                    mapHolder.getChildren().remove(l);
                edgePlaced = new ArrayList<>();

                drawPath();
                break;
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * @param x x coordinates of node in pixels
     * @param y y coordinates of node in pixels
     */
    public void placeNode(int x, int y) {
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/node.fxml")));

            i.setLayoutX((x / PathfindingMenuController.coordinateScale) - (i.getFitWidth() / 4));
            i.setLayoutY((y / PathfindingMenuController.coordinateScale) - (i.getFitHeight()));

            nodeHolder.getChildren().add(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * @param x x coordinates of node in pixels
     * @param y y coordinates of node in pixels
     */
    public void placeIntermediateNode(int x, int y) {
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/intermediateNode.fxml")));

            c.setCenterX((x / PathfindingMenuController.coordinateScale));
            c.setCenterY((y / PathfindingMenuController.coordinateScale));

            nodeHolder.getChildren().add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws an edge between 2 points on the map.
     *
     * @param xStart x start coordinate in pixels
     * @param yStart y start coordinate in pixels
     * @param xEnd   x end coordinate in pixels
     * @param yEnd   y end coordinate in pixels
     */
    public void placeEdge(int xStart, int yStart, int xEnd, int yEnd) {
        try {
            Line l = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/edge.fxml")));
            l.setId("edge" + xStart + yStart + xEnd + yEnd);

            l.setStartX(xStart / PathfindingMenuController.coordinateScale);
            l.setStartY(yStart / PathfindingMenuController.coordinateScale);

            l.setEndX(xEnd / PathfindingMenuController.coordinateScale);
            l.setEndY(yEnd / PathfindingMenuController.coordinateScale);

            mapHolder.getChildren().add(l);
            edgePlaced.add(l);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the start location
     *
     * @return The long name of the node selected in the combobox.
     */
    public String getStartLocation() {
        return startLocComboBox.getValue();
    }

    /**
     * Gets the end location
     *
     * @return The long name of the node selected in the combobox.
     */
    public String getEndLocation() {
        return endLocComboBox.getValue();
    }
}

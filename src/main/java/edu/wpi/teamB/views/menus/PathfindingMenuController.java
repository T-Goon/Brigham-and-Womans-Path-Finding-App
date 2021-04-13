package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import com.sun.javafx.embed.HostDragStartListener;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
    private  HashMap<String, Node> locations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        locations = new HashMap<>();

        // Pulls nodes from the database to fill the nodeInfo hashmap
        try {
            Map<String, Node> nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodes();
            for (Node n : nodes.values()) {
                locations.put(n.getNodeID(), n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Place nodes on map
        for(Node n : locations.values()){
            if(!n.getNodeType().equals("WALK")){
                placeNode(n);
            } else{
                placeIntermediateNode(n);
            }
        }

        // Populate the combo boxes with locations
        try {
            List<Node> nodes = new ArrayList<>(locations.values());
            List<String> nodeNames = new ArrayList<>();
            for (Node n : nodes) {
                nodeNames.add(n.getLongName());
            }

            Collections.sort(nodeNames);
            for (String name : nodeNames) {
                startLocComboBox.getItems().add(name);
                endLocComboBox.getItems().add(name);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> longNameID(){
        Map<String, Node> nodesId =  Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db")).getNodes();
        Map<String, String> longName = new HashMap<>();

        for(Node node: nodesId.values()){
            longName.put(node.getLongName(), node.getNodeID());
        }
        return longName;
    }

   private void drawPath(){
       Map<String, Node> nodesId =  Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db")).getNodes();
       Map<String, String> hmLongName = longNameID();
       List<String> AstrPath = AStar.findPath(hmLongName.get(getStartLocation()), hmLongName.get(getEndLocation()));

       Node prev = null;
       Node curr;
       for(String loc: AstrPath){

           if((prev != null) && (loc != null)){
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
                for(Line l : edgePlaced){
                    mapHolder.getChildren().remove(l);

                }
                edgePlaced = new ArrayList<>();

                drawPath();
                break;
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
        }
    }

    private void showGraphicalSelection(ComboBox comboBox, javafx.scene.Node node, Node n){
        Button tempButton = (Button)node;

        tempButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //loop through combo box if string == name of node
                //keep track of index and pass it in

                for(int i=0; i<comboBox.getItems().size(); i++) {

                    if (n.getLongName().equals(comboBox.getItems().get(i))) {
                        comboBox.getSelectionModel().select(i);

                    }
                }


            }
        });
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     * @param n Node object to place on the map
     */
    public void placeNode(Node n) {
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/node.fxml")));

            i.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale) - (i.getFitWidth() / 4));
            i.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (i.getFitHeight()));

            i.setOnMouseClicked((MouseEvent e) ->{

                try {
                    AnchorPane locInput = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/graphicalInput.fxml")));

                    locInput.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
                    locInput.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (locInput.getHeight()));

                    for(javafx.scene.Node node: locInput.getChildren()){
                        if(node.getId().equals("BtnStart")){
                            showGraphicalSelection(startLocComboBox, node,n);
                        }

                       else{
                            showGraphicalSelection(endLocComboBox, node,n);
                       }
                    }
                    nodeHolder.getChildren().add(locInput);
                }

                catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            });

            nodeHolder.getChildren().add(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     * @param n Node object to place on the map
     */
    public void placeIntermediateNode(Node n) {
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/intermediateNode.fxml")));

            c.setCenterX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setCenterY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            c.setOnMouseClicked((MouseEvent e) ->{
               // System.out.println("Foo");

                try {
                    AnchorPane locInput = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/graphicalInput.fxml")));

                    locInput.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
                    locInput.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (locInput.getHeight()));

                    for(javafx.scene.Node node: locInput.getChildren()){
                        if(node.getId().equals("BtnStart")){
                            showGraphicalSelection(startLocComboBox, node,n);
                        }

                        else{
                            showGraphicalSelection(endLocComboBox, node,n);
                        }
                    }
                    nodeHolder.getChildren().add(locInput);
                }

                catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            });

            nodeHolder.getChildren().add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws an edge between 2 points on the map.
     * @param xStart x start coordinate in pixels
     * @param yStart y start coordinate in pixels
     * @param xEnd   x end coordinate in pixels
     * @param yEnd   y end coordinate in pixels
     */
    public void placeEdge(int xStart, int yStart, int xEnd, int yEnd) {
        try {
            Line l = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/edge.fxml")));

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

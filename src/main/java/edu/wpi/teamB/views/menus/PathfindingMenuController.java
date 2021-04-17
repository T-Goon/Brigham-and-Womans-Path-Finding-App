package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PathfindingMenuController implements Initializable {

    @FXML
    private JFXTextField txtStartLocation;

    @FXML
    private JFXTextField txtEndLocation;

    @FXML
    private AnchorPane nodeHolder;

    @FXML
    private AnchorPane intermediateNodeHolder;

    @FXML
    private AnchorPane mapHolder;

    @FXML
    private ImageView map;

    @FXML
    private JFXButton btnFindPath;

    @FXML
    private JFXButton btnBack;

    @FXML
    private Label lblError;

    @FXML
    private JFXTreeView<String> treeLocations;

    private static final double coordinateScale = 25/9.0;
    private List<Line> edgePlaced = new ArrayList<>();
    private VBox popup = null;

    private final HashMap<String, List<Node>> floorNodes = new HashMap<>();
    private Map<String, Node> locations = new HashMap<>();
    private Map<String, String> mapLongToID = new HashMap<>();
    private Map<String, String> categoryNameMap = new HashMap<>();

    private TreeItem<String> selectedLocation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        categoryNameMap.put("SERV", "Services");
        categoryNameMap.put("REST", "Restrooms");
        categoryNameMap.put("LABS", "Lab Rooms");
        categoryNameMap.put("ELEV", "Elevators");
        categoryNameMap.put("DEPT", "Departments");
        categoryNameMap.put("CONF", "Conference Rooms");
        categoryNameMap.put("INFO", "Information Locations");
        categoryNameMap.put("RETL", "Retail Locations");
        categoryNameMap.put("BATH", "Bathroom");
        categoryNameMap.put("EXIT", "Entrances");
        categoryNameMap.put("STAI", "Stairs");
        categoryNameMap.put("PARK", "Parking Spots");


        HashMap<String, List<TreeItem<String>>> catNameMap = new HashMap<>();
        locations = Graph.getGraph().getNodes();
        mapLongToID = makeLongToIDMap();


        validateFindPathButton();

        //Adds all the destination names to locationNames and sort the nodes by floor
        for (Node n : locations.values()) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
                //Populate Category map for TreeView
                if(!catNameMap.containsKey(n.getNodeType())){
                    ArrayList<TreeItem<String>> tempList = new ArrayList<>();
                    TreeItem<String>  tempItem = new TreeItem<>(n.getLongName());
                    tempList.add(tempItem);
                    catNameMap.put(n.getNodeType(), tempList);
                }else{
                    catNameMap.get(n.getNodeType()).add(new TreeItem<>(n.getLongName()));
                }

            }

            //Populate the floorNodes Map to know which nodes belong to each floor
            if (floorNodes.containsKey(n.getFloor())) {
                floorNodes.get(n.getFloor()).add(n);
            } else {
                ArrayList<Node> tempList = new ArrayList<>();
                tempList.add(n);
                floorNodes.put(n.getFloor(), tempList);
            }

        }


        //Populating TreeView
        TreeItem<String> rootNode = new TreeItem<>("Locations");
        rootNode.setExpanded(true);
        treeLocations.setRoot(rootNode);

        //Adding Categories
        for(String category : catNameMap.keySet()){
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(catNameMap.get(category));
            rootNode.getChildren().add(categoryTreeItem);
        }

        try{
            drawNodesOnFloor("1");
        } catch (NullPointerException ignored){}
    }

    /**
     * Draws all the nodes on a given floor
     *
     * @param floorID the floor id for the nodes "L2", "L1", "1", "2", "3"
     */
    private void drawNodesOnFloor(String floorID) {
        // If the floor has no nodes, return
        if (!floorNodes.containsKey(floorID)) return;

        for (Node n : floorNodes.get(floorID)) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
                placeNode(n);
            }
        }
    }

    /**
     * @return a map of long names to node IDs
     */
    private Map<String, String> makeLongToIDMap() {
        Map<String, String> longName = new HashMap<>();

        for (Node node : locations.values()) {
            longName.put(node.getLongName(), node.getNodeID());
        }
        return longName;
    }

    /**
     * Draws the path on the map
     */
    private void drawPath() {
        Map<String, Node> nodesId = Graph.getGraph().getNodes();
        List<String> AstarPath = AStar.findPath(mapLongToID.get(getStartLocation()), mapLongToID.get(getEndLocation()));

        if (AstarPath.isEmpty()) {
            lblError.setVisible(true);
        } else {
            Node prev = null;
            for (String loc : AstarPath) {
                if ((prev != null) && (loc != null)) {
                    Node curr = nodesId.get(loc);
                    placeEdge(prev.getXCoord(), prev.getYCoord(), curr.getXCoord(), curr.getYCoord());
                }
                prev = nodesId.get(loc);
            }
        }
    }

    @FXML
    public void handleLocationSelected(MouseEvent mouseEvent) {
        TreeItem<String> selectedItem = treeLocations.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            return;
        }
        if(!selectedItem.equals(selectedLocation) && selectedItem.isLeaf()){
            //Selected item is a valid location
            createGraphicalInputPopup(locations.get(mapLongToID.get(selectedItem.getValue())));
        }
        selectedLocation = selectedItem;
        validateFindPathButton();
    }

    @FXML
    private void validateFindPathButton() throws NumberFormatException {
        btnFindPath.setDisable(txtStartLocation.getText().isEmpty()|| txtEndLocation.getText().isEmpty() || txtStartLocation.getText().equals(txtEndLocation.getText()));
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":
                // Remove old path
                lblError.setVisible(false);
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
     * @param n Node object to place on the map
     */
    public void placeNode(Node n) {
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/node.fxml")));

            i.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale) - (i.getFitWidth() / 4));
            i.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (i.getFitHeight()));

            i.setId(n.getNodeID()+"Icon");

            i.setOnMouseClicked((MouseEvent e) -> createGraphicalInputPopup(n));

            nodeHolder.getChildren().add(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * @param n Node object to place on the map
     */
    public void placeIntermediateNode(Node n) {
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/intermediateNode.fxml")));

            c.setCenterX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setCenterY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            intermediateNodeHolder.getChildren().add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    public void createGraphicalInputPopup(Node n) {

        try {
            // Load fxml
            final VBox locInput = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/graphicalInput.fxml")));

            // Set coordinates of popup
            locInput.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            locInput.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (locInput.getHeight()));

            // Set up popup buttons
            for (javafx.scene.Node node : locInput.getChildren()) {
                switch (node.getId()) {
                    case "BtnStart":
                        showGraphicalSelection(txtStartLocation, node, n);
                        break;
                    case "BtnEnd":
                        showGraphicalSelection(txtEndLocation, node, n);
                        break;
                    case "BtnCancel":
                        Button cancelButton = (Button) node;
                        cancelButton.setOnAction(event -> deleteBox());
                        break;
                }
            }

            if (popup != null) {
                deleteBox();
            }

            popup = locInput;
            nodeHolder.getChildren().add(locInput);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Shows the popup for the graphical input.
     *
     * @param textField TextField to select items from
     * @param node     javafx node that will show popup when clicked
     * @param n        map node the popup is for
     */
    private void showGraphicalSelection(JFXTextField textField, javafx.scene.Node node, Node n) {
        Button tempButton = (Button) node;

        tempButton.setOnAction(event -> {
            //loop through combo box if string == name of node
            //keep track of index and pass it in
            textField.setText(n.getLongName());
            deleteBox();
            validateFindPathButton();
        });

    }

    /**
     * Removes the graphical input popup from the map.
     */
    private void deleteBox() {
        nodeHolder.getChildren().remove(popup);
        popup = null;
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
        return txtStartLocation.getText();
    }

    /**
     * Gets the end location
     *
     * @return The long name of the node selected in the combobox.
     */
    public String getEndLocation() {
        return txtEndLocation.getText();
    }

}

package edu.wpi.teamB.views.map;

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.entities.Path;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
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
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnExit;

    @FXML
    private Label lblError;

    @FXML
    private JFXButton btnEditMap;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTreeView<String> treeLocations;

    private static final double coordinateScale = 25 / 9.0;
    private List<Line> edgePlaced = new ArrayList<>();
    private List<javafx.scene.Node> nodePlaced = new ArrayList<>();
    private List<javafx.scene.Node> intermediateNodePlaced = new ArrayList<>();
    private boolean editMap = false;
    private VBox selectionBox = null;
    private VBox estimatedTimeBox = null;

    private String currentFloor = "1";
    private VBox addNodePopup;
    private VBox editNodePopup;
    private VBox delEdgePopup;
    private Map<String, List<Node>> floorNodes = new HashMap<>();
    private Map<String, String> categoryNameMap = new HashMap<>();
    private Map<String, String> mapLongToID = new HashMap<>();
    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    private TreeItem<String> selectedLocation;

    @Setter
    @Getter
    String newEdgeStart;
    @Setter
    @Getter
    Circle startNode;
    @Setter
    @Getter
    String newEdgeEnd;
    @Setter
    @Getter
    Circle endNode;

    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    // JavaFX code **************************************************************************************

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Add better category names to a hash map
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

        mapLongToID = makeLongToIDMap();

        validateFindPathButton();

        //Adds all the destination names to locationNames and sort the nodes by floor
        updateLocations();

        // Draw the nodes on the map
        try {
            drawNodesOnFloor(currentFloor);
        } catch (NullPointerException ignored) {
        }

        //test if we came from a failed covid survey
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/covidSurvey/covidFormSubmittedWithSymp.fxml")) {
            txtEndLocation.setText("Emergency Department Entrance");
            SceneSwitcher.popLastScene();
        }

        initMapForEditing();


        // Set up Load and Save buttons
        btnLoad.setOnAction(
                event -> {
                    // Get the nodes CSV file and load it
                    Stage stage = App.getPrimaryStage();
                    fileChooser.setTitle("Select Nodes CSV file:");
                    fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
                    File file = fileChooser.showOpenDialog(stage);
                    if (file == null) return;
                    List<Node> newNodes = CSVHandler.loadCSVNodesFromExternalPath(file.toPath());

                    // Get the edges CSV file and load it
                    fileChooser.setTitle("Select Edges CSV file:");
                    fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
                    file = fileChooser.showOpenDialog(stage);
                    if (file == null) return;
                    List<Edge> newEdges = CSVHandler.loadCSVEdgesFromExternalPath(file.toPath());

                    // Update the database
                    db.loadDatabase(newNodes, newEdges);
                    Graph.getGraph().updateGraph();

                    // Now delete and refresh the nodes
                    drawAllElements();
                }
        );

        btnSave.setOnAction(
                event -> {
                    // Get the CSV directory location
                    Stage stage = App.getPrimaryStage();
                    directoryChooser.setTitle("Select directory to save CSV files to:");
                    directoryChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
                    File file = directoryChooser.showDialog(stage);
                    if (file == null) return;

                    // Save the current database into that folder in CSV files
                    CSVHandler.saveCSVNodes(file.toPath(), false);
                    CSVHandler.saveCSVEdges(file.toPath(), false);
                }
        );
    }

    /**
     * Event handler for when the tree view is clicked. When clicked it checks the selected location on the tree view
     * and if its a location not a category it finds the node and used the graphical input popup on that node
     *
     * @param mouseEvent the mouse event
     */
    @FXML
    public void handleLocationSelected(MouseEvent mouseEvent) {
        TreeItem<String> selectedItem = treeLocations.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        if (!selectedItem.equals(selectedLocation) && selectedItem.isLeaf()) {
            //Selected item is a valid location

            //For now only work on nodes that are on the first floor until multi-floor pathfinding is added
            Node tempLocation = Graph.getGraph().getNodes().get(mapLongToID.get(selectedItem.getValue()));
            if (tempLocation.getFloor().equals(currentFloor)) {
                if (editMap) showEditNodePopup(tempLocation, mouseEvent, true);
                else createGraphicalInputPopup(tempLocation);
            }
        }
        selectedLocation = selectedItem;
        validateFindPathButton();
    }

    /**
     * Input validation for the pathfinding button. Button only enables when both input fields are populated and they
     * are not equal to each other.
     */
    @FXML
    private void validateFindPathButton() {
        btnFindPath.setDisable(txtStartLocation.getText().isEmpty() || txtEndLocation.getText().isEmpty() || txtStartLocation.getText().equals(txtEndLocation.getText()));
    }

    /**
     * Draw the estimated time dialog box
     *
     * @param path the path to draw the box on
     */
    private void drawEstimatedTimeBox(Path path) {

        // No path
        if (path.getPath().size() == 0) return;

        String estimatedTime = AStar.getEstimatedTime(path);
        estimatedTimeBox = new VBox();
        try {
            estimatedTimeBox = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/showEstimatedTime.fxml")));
        } catch (IOException e) {
            System.err.println("[drawEstimatedTimeBox] FXMLLoader load failed");
        }

        estimatedTimeBox.setId("estimatedTimeDialog");

        List<javafx.scene.Node> child = estimatedTimeBox.getChildren();
        Text textBox = (Text) child.get(0);
        textBox.setText(estimatedTime);

        Graph graph = Graph.getGraph();
        Node endNode = graph.getNodes().get(path.getPath().get(path.getPath().size() - 1));

        estimatedTimeBox.setLayoutX((endNode.getXCoord() / PathfindingMenuController.coordinateScale));
        estimatedTimeBox.setLayoutY((endNode.getYCoord() / PathfindingMenuController.coordinateScale) - (estimatedTimeBox.getHeight()));
        nodeHolder.getChildren().add(estimatedTimeBox);
    }


    /**
     * Button handler for the scene
     *
     * @param e the action event being handled
     */
    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":
                removeOldPaths();
                drawPath();
                break;
            case "btnEditMap":
                ImageView graphic = (ImageView) btnEditMap.getChildrenUnmodifiable().get(1);

                if (!editMap) {
                    graphic.setImage(new Image("edu/wpi/teamB/images/menus/directionsIcon.png"));
                    editMap = true;
                } else {
                    graphic.setImage(new Image("edu/wpi/teamB/images/menus/wrench.png"));

                    // Remove the add node popup if it is on the map
                    if (addNodePopup != null) {
                        nodeHolder.getChildren().remove(addNodePopup);
                        addNodePopup = null;
                    }

                    // Remove the edit node popup if it is on the map
                    if (editNodePopup != null) {
                        nodeHolder.getChildren().remove(editNodePopup);
                        editNodePopup = null;
                    }

                    editMap = false;
                }
                selectedLocation = null;
                drawAllElements();

                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/map/pathfindingMenu.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    // Code for graphical map editor *********************************************************************

    /**
     * Shows the add node popup when double clicking on the map.
     */
    private void initMapForEditing() {

        map.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Show popup on double clicks
                if (event.getClickCount() < 2) return;

                // Coordinates on the map
                double x = event.getX();
                double y = event.getY();

                // if in editing mode
                if (editMap) {

                    // Only one window open at a time;
                    removeAllPopups();

                    App.getPrimaryStage().setUserData(new GraphicalEditorNodeData(null,
                            x * PathfindingMenuController.coordinateScale,
                            y * PathfindingMenuController.coordinateScale,
                            currentFloor,
                            null,
                            null,
                            null,
                            null,
                            null,
                            nodeHolder,
                            PathfindingMenuController.this,
                            null));

                    try {
                        addNodePopup = FXMLLoader.load(Objects.requireNonNull(
                                getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/addNodePopup.fxml")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    assert addNodePopup != null;

                    // Keep popup on the map
                    placePopupOnMap(addNodePopup, x, y);

                    nodeHolder.getChildren().add(addNodePopup);
                }

            }
        });
    }

    /**
     * Shows the edit node popup filled in with the information from n.
     *
     * @param n Node that is to be edited.
     */
    private void showEditNodePopup(Node n, MouseEvent event, boolean fromTreeView) {

        Circle c;
        if (fromTreeView) c = null;
        else c = (Circle) event.getSource();

        // Make sure there is only one editNodePopup at one time
        removeAllPopups();

        // Data to pass to popup
        App.getPrimaryStage().setUserData(new GraphicalEditorNodeData(
                n.getNodeID(),
                n.getXCoord(),
                n.getYCoord(),
                currentFloor,
                n.getBuilding(),
                n.getNodeType(),
                n.getLongName(),
                n.getShortName(),
                null,
                nodeHolder,
                PathfindingMenuController.this,
                c));

        // Load popup
        try {
            editNodePopup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/nodePopupWindow.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set location on map
        double x = n.getXCoord() / PathfindingMenuController.coordinateScale;
        double y = n.getYCoord() / PathfindingMenuController.coordinateScale;

        placePopupOnMap(editNodePopup, x, y);

        // Add to map
        nodeHolder.getChildren().add(editNodePopup);
    }

    private void showDelEdgePopup(Node start, Node end) {
        // Make sure there is only one editNodePopup at one time
        removeAllPopups();

        // Pass window data
        App.getPrimaryStage().setUserData(new GraphicalEditorEdgeData(start, end, nodeHolder, this));

        try {
            delEdgePopup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set popup location on map
        double startX = (start.getXCoord() / PathfindingMenuController.coordinateScale);
        double endX = (end.getXCoord() / PathfindingMenuController.coordinateScale);

        double startY = (start.getYCoord() / PathfindingMenuController.coordinateScale);
        double endY = (end.getYCoord() / PathfindingMenuController.coordinateScale);

        placePopupOnMap(delEdgePopup, (startX + endX) / 2, (startY + endY) / 2);

        nodeHolder.getChildren().add(delEdgePopup);
    }

    private void removeAllPopups() {
        if (addNodePopup != null || editNodePopup != null || delEdgePopup != null || selectionBox != null || estimatedTimeBox != null) {
            deleteBox(selectionBox);
            deleteBox(estimatedTimeBox);
            deleteBox(editNodePopup);
            deleteBox(delEdgePopup);
            deleteBox(addNodePopup);
        }
    }

    private void placePopupOnMap(VBox node, double x, double y) {

        if (nodeHolder.getWidth() < node.getPrefWidth() + x) {
            node.setLayoutX(nodeHolder.getWidth() - node.getPrefWidth());
        } else {
            node.setLayoutX(x);
        }

        if (nodeHolder.getHeight() < node.getPrefHeight() + y) {
            node.setLayoutY(nodeHolder.getHeight() - node.getPrefHeight());
        } else {
            node.setLayoutY(y);
        }
    }

    // Code for graphical input to pathfinding ***********************************************************

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    public void createGraphicalInputPopup(Node n) {

        try {
            // Load fxml
            final VBox locInput = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/graphicalInput.fxml")));

            // Set coordinates of popup
            locInput.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            locInput.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (locInput.getHeight()));

            // Set up popup buttons
            for (javafx.scene.Node node : ((VBox) locInput.getChildren().get(0)).getChildren()) {
                JFXButton btn = (JFXButton) ((HBox) node).getChildren().get(0);
                switch (btn.getId()) {
                    case "btnStart":
                        showGraphicalSelection(txtStartLocation, btn, n);
                        break;
                    case "btnEnd":
                        showGraphicalSelection(txtEndLocation, btn, n);
                        break;
                    case "btnCancel":
                        btn.setOnAction(event -> deleteBox(selectionBox));
                        break;
                }
            }

            if (selectionBox != null) {
                deleteBox(selectionBox);
            }

            selectionBox = locInput;
            nodeHolder.getChildren().add(locInput);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Shows the popup for the graphical input.
     *
     * @param textField TextField to set text for
     * @param node      javafx node that will show popup when clicked
     * @param n         map node the popup is for
     */
    private void showGraphicalSelection(JFXTextField textField, javafx.scene.Node node, Node n) {
        JFXButton tempButton = (JFXButton) node;

        tempButton.setOnAction(event -> {
            textField.setText(n.getLongName());
            deleteBox(selectionBox);
            validateFindPathButton();
        });

    }

    /**
     * Removes the graphical input popup from the map.
     *
     * @param box the VBox to be deleted
     */
    private void deleteBox(VBox box) {
        nodeHolder.getChildren().remove(box);
        box = null;
    }

    // Code for displaying content on the map ***********************************************************

    /**
     * Draws all the nodes on a given floor with the default graphic
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
     * Draws all the elements of the map base on direction or map edit mode.
     */
    private void drawAllElements() {
        removeAllPopups();
        if (editMap) {
            removeOldPaths();
            removeNodes();
            drawEdgesOnFloor(currentFloor);
            drawAltNodesOnFloor(currentFloor);
            drawIntermediateNodesOnFloor(currentFloor);
        } else {
            updateLocations();
            removeOldPaths();
            removeIntermediateNodes();
            removeNodes();
            drawNodesOnFloor(currentFloor);
        }
    }

    /**
     * Refresh the nodes on the map.
     * <p>
     * FOR MAP EDITOR MODE ONLY!!!
     * <p>
     */
    public void refreshEditor() {
        updateLocations();
        removeOldPaths();
        removeIntermediateNodes();
        removeNodes();
        drawEdgesOnFloor(currentFloor);
        drawAltNodesOnFloor(currentFloor);
        drawIntermediateNodesOnFloor(currentFloor);
    }

    /**
     * Draws all the nodes on a given floor with the alternate graphic
     *
     * @param floorID the floor id for the nodes "L2", "L1", "1", "2", "3"
     */
    private void drawAltNodesOnFloor(String floorID) {

        Map<String, Node> nodes = db.getNodes();

        if (nodes.isEmpty()) return;

        for (Node n : nodes.values()) {
            if ((!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) &&
                    n.getFloor().equals(floorID)) {
                placeAltNode(n);
            }
        }
    }

    /**
     * Draws all the intermediate nodes on a floor
     *
     * @param floorID the floor id for the nodes "L2", "L1", "1", "2", "3"
     */
    private void drawIntermediateNodesOnFloor(String floorID) {
        Map<String, Node> nodes = db.getNodes();

        if (nodes.isEmpty()) return;

        for (Node n : nodes.values()) {
            if (((n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) &&
                    n.getFloor().equals(floorID)) {
                placeIntermediateNode(n);
            }
        }
    }

    /**
     * Draws all edges on a floor
     *
     * @param floor number of floor as a string
     */
    private void drawEdgesOnFloor(String floor) {
        Map<String, Edge> edges = Graph.getGraph().getEdges();
        for (Edge e : edges.values()) {
            Node start = db.getNodeById(e.getStartNodeID());
            Node end = db.getNodeById(e.getEndNodeID());

            if (start.getFloor().equals(floor) && end.getFloor().equals(floor)) {
                placeEdge(start, end);
            }
        }
    }

    /**
     * Draws the path on the map
     */
    private void drawPath() {
        if (estimatedTimeBox != null)
            deleteBox(estimatedTimeBox);

        Map<String, Node> nodesId = Graph.getGraph().getNodes();
        Map<String, String> hmLongName = makeLongToIDMap();
        Path aStarPath = AStar.findPath(hmLongName.get(getStartLocation()), hmLongName.get(getEndLocation()));

        List<String> AstarPath = aStarPath.getPath();

        if (AstarPath.isEmpty()) {
            lblError.setVisible(true);
        } else {
            Node prev = null;
            for (String loc : AstarPath) {
                if ((prev != null) && (loc != null)) {
                    Node curr = nodesId.get(loc);
                    placeEdge(prev, curr);
                }
                prev = nodesId.get(loc);
            }
        }

        drawEstimatedTimeBox(aStarPath);
    }

    /**
     * Removes any edges drawn on the map
     */
    private void removeOldPaths() {
        lblError.setVisible(false);
        for (Line l : edgePlaced)
            mapHolder.getChildren().remove(l);
        edgePlaced = new ArrayList<>();
    }

    /**
     * Removes all nodes from the map
     */
    private void removeNodes() {
        for (javafx.scene.Node n : nodePlaced)
            nodeHolder.getChildren().remove(n);
        nodePlaced = new ArrayList<>();
    }

    /**
     * Removes all intermediate nodes from the map
     */
    private void removeIntermediateNodes() {
        for (javafx.scene.Node n : intermediateNodePlaced)
            intermediateNodeHolder.getChildren().remove(n);
        intermediateNodePlaced = new ArrayList<>();
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * @param n Node object to place on the map
     */
    private void placeNode(Node n) {
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/node.fxml")));

            i.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale) - (i.getFitWidth() / 4));
            i.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (i.getFitHeight()));

            i.setId(n.getNodeID() + "Icon");

            // Show graphical input for pathfinding when clicked
            i.setOnMouseClicked((MouseEvent e) -> createGraphicalInputPopup(n));

            nodeHolder.getChildren().add(i);
            nodePlaced.add(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Place alternate node type on the map
     *
     * @param n the Node object to place
     */
    private void placeAltNode(Node n) {
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/nodeAlt.fxml")));

            c.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            c.setId(n.getNodeID() + "Icon");

            c.setOnMouseClicked((MouseEvent e) -> showEditNodePopup(n, e, false));

            nodeHolder.getChildren().add(c);
            nodePlaced.add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws an edge between 2 points on the map.
     *
     * @param start start node
     * @param end   end node
     */
    public void placeEdge(Node start, Node end) {
        try {
            Line l = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/edge.fxml")));

            l.setStartX(start.getXCoord() / PathfindingMenuController.coordinateScale);
            l.setStartY(start.getYCoord() / PathfindingMenuController.coordinateScale);

            l.setEndX(end.getXCoord() / PathfindingMenuController.coordinateScale);
            l.setEndY(end.getYCoord() / PathfindingMenuController.coordinateScale);

            l.setOnMouseClicked(e -> showDelEdgePopup(start, end));

            l.setId(start.getNodeID() + "_" + end.getNodeID() + "Icon");

            mapHolder.getChildren().add(l);
            edgePlaced.add(l);

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
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/intermediateNode.fxml")));

            c.setCenterX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setCenterY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            c.setOnMouseClicked(event -> showEditNodePopup(n, event, false));

            c.setId(n.getNodeID() + "IntIcon");

            intermediateNodeHolder.getChildren().add(c);
            intermediateNodePlaced.add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that updates everything involved with the different locations on the map
     * - Tree View
     * - Floor Nodes
     */
    private void updateLocations() {
        mapLongToID = makeLongToIDMap();
        Map<String, List<TreeItem<String>>> catNameMap = new HashMap<>();
        floorNodes.remove(currentFloor);
        for (Node n : Graph.getGraph().getNodes().values()) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
                //Populate Category map for TreeView
                if (!catNameMap.containsKey(n.getNodeType())) {
                    ArrayList<TreeItem<String>> tempList = new ArrayList<>();
                    TreeItem<String> tempItem = new TreeItem<>(n.getLongName());
                    tempList.add(tempItem);
                    catNameMap.put(n.getNodeType(), tempList);
                } else {
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
        for (String category : catNameMap.keySet()) {
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(catNameMap.get(category));
            rootNode.getChildren().add(categoryTreeItem);
        }

    }

    // Misc utility methods ****************************************************************************

    /**
     * @return a map of long names to node IDs
     */
    private Map<String, String> makeLongToIDMap() {
        Map<String, Node> nodesId = Graph.getGraph().getNodes();
        Map<String, String> longName = new HashMap<>();

        for (Node node : nodesId.values()) {
            longName.put(node.getLongName(), node.getNodeID());
        }
        return longName;
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

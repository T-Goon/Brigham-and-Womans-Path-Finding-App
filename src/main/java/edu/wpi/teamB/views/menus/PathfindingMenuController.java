package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.GraphicalEditorData;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    private JFXComboBox<String> startLocComboBox;

    @FXML
    private JFXComboBox<String> endLocComboBox;

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
    private JFXButton btnEditMap;

    private static final double coordinateScale = 25/9.0;
    private List<Line> edgePlaced = new ArrayList<>();
    private List<javafx.scene.Node> nodePlaced = new ArrayList<>();
    private List<javafx.scene.Node> intermediateNodePlaced = new ArrayList<>();
    private boolean editMap = false;
    private VBox popup = null;
    private int currentFloor = 1;
    private VBox addNodePopup;
    private final HashMap<String, List<Node>> floorNodes = new HashMap<>();

    // JavaFx code **************************************************************************************

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, Node> locations = Graph.getGraph().getNodes();
        List<String> locationNames = new ArrayList<>();

        validateFindPathButton();

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

        // Draw the nodes on the map
        try{
            drawNodesOnFloor(Integer.toString(currentFloor));
        } catch (NullPointerException ignored){}

        // Set up map to be edited
        initMapForEditing();
    }

    /**
     * Enables go button only when input is valid.
     */
    @FXML
    private void validateFindPathButton() throws NumberFormatException {
        btnFindPath.setDisable(startLocComboBox.getValue() == null || endLocComboBox.getValue() == null || startLocComboBox.getValue().equals(endLocComboBox.getValue()));
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":

                removeOldPaths();
                drawPath();

                break;
            case "btnEditMap":
                ImageView graphic = (ImageView) btnEditMap.getChildrenUnmodifiable().get(1);

                if(!editMap){
                    graphic.setImage(new Image("edu/wpi/teamB/images/menus/directionsIcon.png"));
                    editMap = true;
                } else if(editMap){
                    graphic.setImage(new Image("edu/wpi/teamB/images/menus/wrench.png"));

                    // Remove the add node popup if it is on the map
                    if(addNodePopup != null){
                        nodeHolder.getChildren().remove(addNodePopup);
                        addNodePopup = null;
                    }

                    editMap = false;
                }

                drawAllElements();

                break;
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
        }
    }

    // Code for graphical map editor *********************************************************************

    private void initMapForEditing(){
        map.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boolean dragged = false;

                // Don't show menu when mouse dragged
                if(event.getEventType() == MouseEvent.MOUSE_DRAGGED || event.getEventType() == MouseEvent.DRAG_DETECTED)
                    dragged = true;

                if(dragged) return;

                // Coordinates on the map
                double x = event.getX();
                double y = event.getY();

                // if in editing mode
                if(editMap){

                    if(addNodePopup != null){
                        nodeHolder.getChildren().remove(addNodePopup);
                    }

                    App.getPrimaryStage().setUserData(new GraphicalEditorData(x*PathfindingMenuController.coordinateScale,
                            y*PathfindingMenuController.coordinateScale,
                            currentFloor,
                            nodeHolder,
                            PathfindingMenuController.this));

                    try{
                        addNodePopup = FXMLLoader.load(Objects.requireNonNull(
                                getClass().getClassLoader().getResource("edu/wpi/teamB/views/mapEditor/addNodePopup.fxml")));
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    assert addNodePopup != null;

                    addNodePopup.setLayoutX(x);
                    addNodePopup.setLayoutY(y);

                    nodeHolder.getChildren().add(addNodePopup);
                }
                // clicks on any nodes now show an editing popup
                // clicks on the map show popup for creating new node
                // popup also has items to add edges

            }
        });
    }

    // Code for graphical input to pathfinding ***********************************************************

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    private void createGraphicalInputPopup(Node n) {

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
                        showGraphicalSelection(startLocComboBox, node, n);
                        break;
                    case "BtnEnd":
                        showGraphicalSelection(endLocComboBox, node, n);
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
     * @param comboBox Combobox to select items from
     * @param node     javafx node that will show popup when clicked
     * @param n        map node the popup is for
     */
    private void showGraphicalSelection(ComboBox comboBox, javafx.scene.Node node, Node n) {
        Button tempButton = (Button) node;

        tempButton.setOnAction(event -> {
            //loop through combo box if string == name of node
            //keep track of index and pass it in

            for (int i = 0; i < comboBox.getItems().size(); i++) {

                if (n.getLongName().equals(comboBox.getItems().get(i))) {
                    comboBox.getSelectionModel().select(i);

                }
            }

            deleteBox();
        });
    }

    /**
     * Removes the graphical input popup from the map.
     */
    private void deleteBox() {
        nodeHolder.getChildren().remove(popup);
        popup = null;
    }

    // Code for displaying the path on the map ***********************************************************

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

    private void drawAllElements(){
        if(editMap){
            removeOldPaths();
            removeNodes();
            drawEdgesOnFloor(Integer.toString(currentFloor));
            drawAltNodesOnFloor(Integer.toString(currentFloor));
            drawIntermediateNodesOnFloor(Integer.toString(currentFloor));
        } else{
            removeOldPaths();
            removeIntermediateNodes();
            removeNodes();
            drawNodesOnFloor(Integer.toString(currentFloor));
        }
    }

    public void refreshEditor(){
        removeOldPaths();
        removeIntermediateNodes();
        removeNodes();
        drawEdgesOnFloor(Integer.toString(currentFloor));
        drawAltNodesOnFloor(Integer.toString(currentFloor));
        drawIntermediateNodesOnFloor(Integer.toString(currentFloor));
    }

    /**
     * Draws all the nodes on a given floor with the alternate graphic
     *
     * @param floorID the floor id for the nodes "L2", "L1", "1", "2", "3"
     */
    private void drawAltNodesOnFloor(String floorID) {

        Map<String, Node> nodes =  DatabaseHandler.getDatabaseHandler("main.db").getNodes();

        if(nodes.isEmpty()) return;

        for (Node n : nodes.values()) {
            if ((!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) &&
                n.getFloor().equals(floorID)) {
                placeAltNode(n);
            }
        }
    }

    /**
     * Draws all the intermediate nodes on a floor
     * @param floorID
     */
    private void drawIntermediateNodesOnFloor(String floorID){
        Map<String, Node> nodes =  DatabaseHandler.getDatabaseHandler("main.db").getNodes();

        if(nodes.isEmpty()) return;

        for (Node n : nodes.values()) {
            if (((n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) &&
                    n.getFloor().equals(floorID)) {
                placeIntermediateNode(n);
            }
        }
    }

    /**
     * Draws all edges on a floor
     * @param floor number of floor as a string
     */
    private void drawEdgesOnFloor(String floor){
        Map<String, Edge> edges = Graph.getGraph().getEdges();
        DatabaseHandler db  = DatabaseHandler.getDatabaseHandler("main.db");

        for(Edge e : edges.values()){
            Node start = db.getNodeById( e.getStartNodeID() );
            Node end = db.getNodeById( e.getEndNodeID() );

            if(start.getFloor().equals(floor) && end.getFloor().equals(floor)){
                placeEdge(
                        start.getXCoord(), start.getYCoord(),
                        end.getXCoord(), end.getYCoord());
            }
        }
    }

    /**
     * Draws the path on the map
     */
    private void drawPath() {
        Map<String, Node> nodesId = Graph.getGraph().getNodes();
        Map<String, String> hmLongName = makeLongToIDMap();
        List<String> AstarPath = AStar.findPath(hmLongName.get(getStartLocation()), hmLongName.get(getEndLocation()));

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

    /**
     * Removes any edges drawn on the map
     */
    private void removeOldPaths(){
        lblError.setVisible(false);
        for (Line l : edgePlaced)
            mapHolder.getChildren().remove(l);
        edgePlaced = new ArrayList<>();
    }

    /**
     * Removes all nodes from the map
     */
    private void removeNodes(){
        for (javafx.scene.Node n : nodePlaced)
            nodeHolder.getChildren().remove(n);
        nodePlaced = new ArrayList<>();
    }

    /**
     * Removes all intermediate nodes from the map
     */
    private void removeIntermediateNodes(){
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
            ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource( "/edu/wpi/teamB/views/misc/node.fxml")));

            i.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale) - (i.getFitWidth() / 4));
            i.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (i.getFitHeight()));

            i.setId(n.getNodeID()+"Icon");

            i.setOnMouseClicked((MouseEvent e) -> createGraphicalInputPopup(n));

            nodeHolder.getChildren().add(i);
            nodePlaced.add(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void placeAltNode(Node n){
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource( "/edu/wpi/teamB/views/misc/nodeAlt.fxml")));

            c.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            c.setId(n.getNodeID()+"Icon");

            c.setOnMouseClicked((MouseEvent e) -> createGraphicalInputPopup(n));

            nodeHolder.getChildren().add(c);
            nodePlaced.add(c);

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
            intermediateNodePlaced.add(c);

        } catch (IOException e) {
            e.printStackTrace();
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

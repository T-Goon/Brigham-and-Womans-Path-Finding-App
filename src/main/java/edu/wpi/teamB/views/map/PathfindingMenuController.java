package edu.wpi.teamB.views.map;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.map.MapCache;
import edu.wpi.teamB.entities.map.MapDrawer;
import edu.wpi.teamB.entities.map.MapEditorPopupManager;
import edu.wpi.teamB.entities.map.MapPathPopupManager;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.*;
import edu.wpi.teamB.views.BasePageController;
import edu.wpi.teamB.util.CSVHandler;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.sql.SQLException;
import java.util.*;

public class PathfindingMenuController extends BasePageController implements Initializable {

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
    private Label lblError;

    @FXML
    private JFXButton btnEditMap;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTreeView<String> treeLocations;

    @FXML
    private StackPane mapStack;

    @FXML
    private GesturePane gpane;

    @FXML
    private StackPane stackContainer;

    public static final double coordinateScale = 25 / 9.0;

    private final Map<String, String> categoryNameMap = new HashMap<>();

    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    private TreeItem<String> selectedLocation;

    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    private final MapCache mc = new MapCache();;
    private MapDrawer md;
    private MapEditorPopupManager mepm;
    private MapPathPopupManager mppm;

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

        validateFindPathButton();

        //Adds all the destination names to locationNames and sort the nodes by floor
        mc.updateLocations();
        populateTreeView();

        md = new MapDrawer(mc, nodeHolder, mapHolder, intermediateNodeHolder, lblError, mapStack, gpane);

        mepm = new MapEditorPopupManager(md, mc, gpane, mapStack);
        md.setMepm(mepm);

        mppm = new MapPathPopupManager(md, txtStartLocation, txtEndLocation, mapStack, gpane, this, nodeHolder);
        md.setMppm(mppm);

        // Draw the nodes on the map
        md.drawNodesOnFloor();


        //test if we came from a failed covid survey
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/covidSurvey/covidFormSubmittedWithSymp.fxml")) {
            txtEndLocation.setText("Emergency Department Entrance");
            SceneSwitcher.popLastScene();
        }

        initMapForEditing();

        // Set up Load and Save buttons
        btnLoad.setOnAction( event -> loadCSV());

        btnSave.setOnAction( event -> saveCSV());

        // Disable editing if the user is not an admin
        checkPermissions();
    }

    /**
     * Loads node and edges data from csv
     */
    private void loadCSV(){
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
        try {
            db.loadNodesEdges(newNodes, newEdges);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();

        // Now delete and refresh the nodes
        md.drawAllElements();
    }

    /**
     * Saves node and edges data to a csv
     */
    private void saveCSV(){
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

    /**
     * Only show edit map buttons if the user has the correct permissions.
     */
    private void checkPermissions() {
        btnEditMap.setVisible(db.getAuthenticationUser().isAtLeast(User.AuthenticationLevel.ADMIN));
        btnLoad.setVisible(db.getAuthenticationUser().isAtLeast(User.AuthenticationLevel.ADMIN));
        btnSave.setVisible(db.getAuthenticationUser().isAtLeast(User.AuthenticationLevel.ADMIN));
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
            Node tempLocation = Graph.getGraph().getNodes().get(
                    mc.makeLongToIDMap().get(
                            selectedItem.getValue()));

            if (tempLocation.getFloor().equals(mc.getCurrentFloor())) {

                md.removeAllPopups();
                if (md.isEditing())
                    mepm.showEditNodePopup(tempLocation, mouseEvent, true);
                else
                    mppm.createGraphicalInputPopup(tempLocation);

            }
        }

        selectedLocation = selectedItem;
        validateFindPathButton();
    }

    /**
     * Input validation for the pathfinding button. Button only enables when both input fields are populated and they
     * are not equal to each other.
     */
    public void validateFindPathButton() {
        btnFindPath.setDisable(txtStartLocation.getText().isEmpty() || txtEndLocation.getText().isEmpty() || txtStartLocation.getText().equals(txtEndLocation.getText()));
    }

    /**
     * Button handler for the scene
     *
     * @param e the action event being handled
     */
    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/teamB/views/map/pathfindingMenu.fxml";
        super.handleButtonAction(e);
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":
                md.removeOldPaths();
                md.drawPath(txtStartLocation.getText(), txtEndLocation.getText());
                break;
            case "btnEditMap":

//                ImageView graphic = (ImageView) btnEditMap.getChildrenUnmodifiable().get(0);

                md.removeAllPopups();
                mppm.removeETAPopup();
                //                    graphic.setImage(new Image("edu/wpi/teamB/images/menus/directionsIcon.png"));
                //                    graphic.setImage(new Image("edu/wpi/teamB/images/menus/wrench.png"));
                md.setEditing(!md.isEditing());

                selectedLocation = null;
                md.drawAllElements();
                break;
            case "btnHelp":
                loadHelpDialog();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
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
                            mapStack,
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
                    placePopupOnMap(addNodePopup);

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
                mapStack,
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

        placePopupOnMap(editNodePopup);

    }

    private void showDelEdgePopup(Node start, Node end) {
        // Make sure there is only one editNodePopup at one time
        removeAllPopups();

        // Pass window data
        App.getPrimaryStage().setUserData(new GraphicalEditorEdgeData(start, end, mapStack, this));

        try {
            delEdgePopup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgePopup.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        placePopupOnMap(delEdgePopup);
    }

    private void removeAllPopups() {
        if (addNodePopup != null || editNodePopup != null || delEdgePopup != null || selectionBox != null) {
            deleteBox(selectionBox);
            deleteBox(editNodePopup);
            deleteBox(delEdgePopup);
            deleteBox(addNodePopup);
        }

        if (estimatedTimeBox != null) {
            nodeHolder.getChildren().remove(estimatedTimeBox);
        }

        gpane.setGestureEnabled(true);

    }

    /**
     * Place a popup over the map.
     *
     * @param node The popup
     */
    private void placePopupOnMap(VBox node) {
        gpane.setGestureEnabled(false);
        mapStack.getChildren().add(node);
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

            // Set up popup buttons
            for (javafx.scene.Node node : ((VBox) locInput.getChildren().get(0)).getChildren()) {
                javafx.scene.Node child = ((HBox) node).getChildren().get(0);
                switch (child.getId()) {
                    case "nodeName":
                        ((Text) child).setText(n.getLongName());
                        break;
                    case "btnStart":
                        showGraphicalSelection(txtStartLocation, child, n);
                        break;
                    case "btnEnd":
                        showGraphicalSelection(txtEndLocation, child, n);
                        break;
                    case "btnCancel":
                        ((JFXButton) child).setOnAction(event -> removeAllPopups());
                        break;
                }
            }

            if (selectionBox != null) {
                removeAllPopups();
            }

            selectionBox = locInput;
//            nodeHolder.getChildren().add(locInput);
            placePopupOnMap(locInput);

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
            removeAllPopups();
            validateFindPathButton();
        });

    }

    /**
     * Removes the graphical input popup from the map.
     *
     * @param box the VBox to be deleted
     */
    private void deleteBox(VBox box) {
        mapStack.getChildren().remove(box);
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
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL") || n.getBuilding().equals("BTM") || n.getBuilding().equals("Shapiro"))) {
                placeNode(n);
            }
        }
    }

    /**
     * Populates the tree view with nodes and categories
     */
    private void populateTreeView(){
        //Populating TreeView
        TreeItem<String> rootNode = new TreeItem<>("Locations");
        rootNode.setExpanded(true);
        treeLocations.setRoot(rootNode);

        //Adding Categories
        for (String category : mc.getCatNameMap().keySet()) {
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(mc.getCatNameMap().get(category));
            rootNode.getChildren().add(categoryTreeItem);
        }
    }

    /**
     * Shows the add node popup when double clicking on the map.
     */
    private void initMapForEditing() {

        map.setOnMouseClicked(event -> {
            // Show popup on double clicks
            if (event.getClickCount() < 2) return;

            // if in editing mode
            if (md.isEditing()) {

                // Only one window open at a time;
                md.removeAllPopups();

                mepm.showAddNodePopup(event);
            }
        });

    }

    /**
     * Shows the help dialog box.
     */
    private void loadHelpDialog() {
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText;
        if(!md.isEditing()){
            helpText = new Text("Enter your start and end location graphically or using our menu selector. To use the graphical selection,\nsimply click on the node and click on the set button. To enter a location using the menu. Click on the appropriate\ndrop down and choose your location. The node you selected will show up on your map where you can either\nset it to your start or end location. Once both the start and end nodes are filled in you can press \"Go\" to generate your path");
        else
            helpText = new Text("Double click to add a node. Click on a node or an edge to edit or remove them. To add a new edge click on\none of the nodes, then add edge, and then start node. Go to the next node in the edge then, add edge, end node,\nand finally add node.");
        helpText.setFont(new Font("MS Reference Sans Serif", 14));

        Label headerLabel = new Label("Help");
        headerLabel.setFont(new Font("MS Reference Sans Serif", 18));

        helpLayout.setHeading(headerLabel);
        helpLayout.setBody(helpText);
        JFXDialog helpWindow = new JFXDialog(stackContainer, helpLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> helpWindow.close());
        helpLayout.setActions(button);

        helpWindow.show();
    }
}

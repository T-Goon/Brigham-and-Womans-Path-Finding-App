package edu.wpi.cs3733.D21.teamB.views.map;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.FloorSwitcher;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.map.MapCache;
import edu.wpi.cs3733.D21.teamB.entities.map.MapDrawer;
import edu.wpi.cs3733.D21.teamB.entities.map.MapEditorPopupManager;
import edu.wpi.cs3733.D21.teamB.entities.map.MapPathPopupManager;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.AStar;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
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
    private JFXButton btnEmergency;

    @FXML
    private Label lblError;

    @FXML
    private JFXButton btnEditMap;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private JFXButton btnSave;

    @FXML
    @Getter
    private JFXComboBox<String> comboPathingType;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXCheckBox btnMobility;

    @FXML
    private JFXTreeView<String> treeLocations;

    @FXML
    private StackPane mapStack;

    @FXML
    private GesturePane gpane;

    @FXML
    private StackPane stackContainer;

    @FXML
    private JFXButton btnAddToFavorites;

    @FXML
    private JFXButton btnRemoveFromFavorites;

    @FXML
    private JFXButton btnF3;

    @FXML
    private JFXButton btnF2;

    @FXML
    private JFXButton btnF1;

    @FXML
    private JFXButton btnFL1;

    @FXML
    private JFXButton btnFL2;

    @FXML
    private StackPane textDirectionsHolder;

    @FXML
    private JFXButton btnTxtDir;

    @FXML
    private JFXTextArea txtAreaStops;

    @FXML
    private JFXButton btnRemoveStop;

    @FXML
    private Circle pathHead;

    public static final double coordinateScale = 25 / 9.0;

    private final Map<String, String> categoryNameMap = new HashMap<>();

    private final HashMap<String, Color> colors = new HashMap<>();

    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    private final MapCache mapCache = new MapCache();
    private MapDrawer mapDrawer;
    private MapEditorPopupManager mapEditorPopupManager;
    private MapPathPopupManager mapPathPopupManager;
    private FloorSwitcher floorSwitcher;

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
        mapCache.updateLocations();
        try {
            populateTreeView();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapDrawer = new MapDrawer(this, mapCache, nodeHolder, mapHolder, intermediateNodeHolder, lblError, mapStack, gpane);

        mapEditorPopupManager = new MapEditorPopupManager(mapDrawer, mapCache, gpane, mapStack);
        mapDrawer.setMapEditorPopupManager(mapEditorPopupManager);

        mapPathPopupManager = new MapPathPopupManager(mapDrawer, mapCache, txtStartLocation, txtEndLocation, btnRemoveStop, mapStack, gpane, this, nodeHolder, textDirectionsHolder);
        mapDrawer.setMapPathPopupManager(mapPathPopupManager);

        // Set up floor switching
        floorSwitcher = new FloorSwitcher(mapDrawer, mapCache, map, btnF3, btnF2, btnF1, btnFL1, btnFL2);
        floorSwitcher.switchFloor(FloorSwitcher.floor1ID);

        //test if we came from a failed covid survey
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormSubmittedWithSymp.fxml")) {
            txtEndLocation.setText("Emergency Department Entrance");
            SceneSwitcher.popLastScene();
        }

        //test if we came from a not failed covid survey
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormSubmittedNoSymp.fxml")) {
            txtEndLocation.setText("75 Francis Lobby Entrance");
            SceneSwitcher.popLastScene();
        }

        initMapForEditing();

        // Set up Load and Save buttons
        btnLoad.setOnAction(event -> loadCSV());

        btnSave.setOnAction(event -> saveCSV());

        // Set up mobility button
        btnMobility.setOnAction(event -> mapDrawer.setMobility(btnMobility.isSelected()));

        // Disable editing if the user is not an admin
        checkPermissions();

        // Makes sure no illegal characters can't be written in the field
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(" a-zA-Z0-9\\-"))
                txtSearch.setText(newValue.replaceAll("[^ a-zA-Z0-9\\-]", ""));
        });

        // Set up the pathing type combo box
        comboPathingType.getItems().add("A*");
        comboPathingType.getItems().add("DFS");
        comboPathingType.getItems().add("BFS");
        comboPathingType.getSelectionModel().select(Graph.getGraph().getPathingTypeIndex());
        comboPathingType.setOnAction(e -> Graph.getGraph().setPathingTypeIndex(comboPathingType.getSelectionModel().getSelectedIndex()));
    }

    /**
     * Loads node and edges data from csv
     */
    private void loadCSV() {
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
        mapDrawer.drawAllElements();
    }

    /**
     * Saves node and edges data to a csv
     */
    private void saveCSV() {
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
        comboPathingType.setVisible(db.getAuthenticationUser().isAtLeast(User.AuthenticationLevel.ADMIN));
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

        if (!selectedItem.getValue().equals("Favorites") && selectedItem.isLeaf()) {
            //Selected item is a valid location

            //For now only work on nodes that are on the first floor until multi-floor pathfinding is added
            Node tempLocation = Graph.getGraph().getNodes().get(
                    mapCache.makeLongToIDMap().get(
                            selectedItem.getValue()));

            if (tempLocation.getFloor().equals(mapCache.getCurrentFloor())) {

                mapDrawer.removeAllPopups();
                if (mapDrawer.isEditing())
                    mapEditorPopupManager.showEditNodePopup(tempLocation, mouseEvent, true);
                else
                    mapPathPopupManager.createGraphicalInputPopup(tempLocation);

            }
        } else if (!selectedItem.isLeaf()) {
            mapDrawer.removeAllPopups();
        }

        if (!selectedItem.isLeaf() && !selectedItem.getValue().equals("Locations") && !selectedItem.getValue().equals("Favorites")) {
            String category = selectedItem.getValue();
            NodeType nt = NodeType.deprettify(category);
            HashMap<String, List<Node>> floorNodes = (HashMap<String, List<Node>>) mapCache.getFloorNodes();

            // Change node color back to original color
            if (!colors.isEmpty()) {
                for (Node node : floorNodes.get(mapCache.getCurrentFloor())) {
                    if (colors.containsKey(node.getNodeID())) {
                        node.setColor(colors.get(node.getNodeID()));
                    }
                }
                colors.clear();
            }

            // Change node color to gray
            for (Node node : floorNodes.get(mapCache.getCurrentFloor())) {
                if (!node.getNodeType().equals(nt.toString())) {
                    Color color = Color.web("#9A9999");
                    if (node.getColor() == null) {
                        colors.put(node.getNodeID(), Color.web("#012D5A"));
                    } else {
                        colors.put(node.getNodeID(), node.getColor());
                    }
                    node.setColor(color);
                }
            }

            mapDrawer.redrawNodes();
        }

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
        super.handleButtonAction(e);
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":
                mapDrawer.removeAllEdges();
                mapDrawer.drawPath(txtStartLocation.getText(), txtEndLocation.getText());

                if (btnTxtDir.isDisable()) {
                    btnTxtDir.setDisable(false);
                }

                break;
            case "btnEditMap":

                mapDrawer.removeAllPopups();
                mapDrawer.removeAllEdges();

                mapDrawer.setEditing(!mapDrawer.isEditing());

                mapDrawer.drawAllElements();
                break;
            case "btnF3":
                mapDrawer.removeAllEdges();
                floorSwitcher.switchFloor(FloorSwitcher.floor3ID);
                break;
            case "btnF2":
                mapDrawer.removeAllEdges();
                floorSwitcher.switchFloor(FloorSwitcher.floor2ID);
                break;
            case "btnF1":
                mapDrawer.removeAllEdges();
                floorSwitcher.switchFloor(FloorSwitcher.floor1ID);
                break;
            case "btnFL1":
                mapDrawer.removeAllEdges();
                floorSwitcher.switchFloor(FloorSwitcher.floorL1ID);
                break;
            case "btnFL2":
                mapDrawer.removeAllEdges();
                floorSwitcher.switchFloor(FloorSwitcher.floorL2ID);
                break;
            case "btnRemoveStop":
                mapCache.getStopsList().remove(mapCache.getStopsList().size() - 1);
                displayStops(mapCache.getStopsList());

                // Validate button
                btnRemoveStop.setDisable(mapCache.getStopsList().isEmpty());

                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnHelp":
                loadHelpDialog();
                break;
            case "btnSearch":
                handleItemSearched();
                break;
            case "btnTxtDir":
                mapDrawer.removeAllPopups();
                if (mapCache.getFinalPath() != null) {
                    mapPathPopupManager.createTxtDirPopup(mapCache.getFinalPath());
                }
                break;
        }
    }

    /**
     * Display the stops in the textArea
     *
     * @param stopsList List of node longnames
     */
    public void displayStops(List<String> stopsList) {
        StringBuilder stops = new StringBuilder();

        for (String s : stopsList) {
            stops.append(s).append("\n");
        }

        txtAreaStops.setText(stops.toString());
    }

    /**
     * Populates the tree view with nodes and categories
     */
    private void populateTreeView() throws IOException {
        //Populating TreeView
        btnAddToFavorites = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/addBtn.fxml")));
        TreeItem<String> rootNode = new TreeItem<>("Root");
        treeLocations.setRoot(rootNode);
        treeLocations.setShowRoot(false);

        // Favorites drop down
        TreeItem<String> favorites = new TreeItem<>("Favorites");
        if (DatabaseHandler.getDatabaseHandler("main.db").getAuthenticationUser().isAtLeast(User.AuthenticationLevel.PATIENT)) {
            favorites.setExpanded(true);
            rootNode.getChildren().add(favorites);
            favorites.setGraphic(btnAddToFavorites);
        }

        // Locations drop down
        TreeItem<String> locations = new TreeItem<>("Locations");
        locations.setExpanded(true);
        rootNode.getChildren().add(locations);

        //Adding Categories
        for (String category : mapCache.getCatNameMap().keySet()) {
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(mapCache.getCatNameMap().get(category));
            locations.getChildren().add(categoryTreeItem);
        }

        // Adding to Favorites
        btnAddToFavorites.setOnAction(addEvent -> {
            try {
                btnRemoveFromFavorites = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/removeBtn.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            TreeItem<String> equivalent = treeLocations.getSelectionModel().getSelectedItem();
            if (equivalent == null) {
                return;
            }
            String text = equivalent.getValue();
            TreeItem<String> itemToAdd = new TreeItem<>(text);

            boolean contains = false;

            for (TreeItem<String> item : favorites.getChildren()) {
                if (item.getValue().equals(equivalent.getValue())) {
                    contains = true;
                    break;
                }
            }

            if (!contains && equivalent.isLeaf() && !text.equals("Favorites")) {
                itemToAdd.setGraphic(btnRemoveFromFavorites);
                favorites.getChildren().add(itemToAdd);
                try {
                    DatabaseHandler.getDatabaseHandler("main.db").addFavoriteLocation(itemToAdd.getValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Removing from Favorites
            btnRemoveFromFavorites.setOnAction(removeEvent -> {
                JFXButton itemToRemove = (JFXButton) removeEvent.getSource();
                TreeCell<String> treeCell = (TreeCell<String>) itemToRemove.getParent();
                favorites.getChildren().remove(treeCell.getTreeItem());
                try {
                    DatabaseHandler.getDatabaseHandler("main.db").removeFavoriteLocation(treeCell.getTreeItem().getValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });

        // Populate Favorites with locations from database
        try {
            ArrayList<String> savedFavorites = (ArrayList<String>) DatabaseHandler.getDatabaseHandler("main.db").getFavorites();
            for (String favorite : savedFavorites) {
                TreeItem<String> item = new TreeItem<>(favorite);
                try {
                    btnRemoveFromFavorites = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/removeBtn.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Removing from Favorites
                btnRemoveFromFavorites.setOnAction(removeEvent -> {
                    JFXButton itemToRemove = (JFXButton) removeEvent.getSource();
                    TreeCell<String> treeCell = (TreeCell<String>) itemToRemove.getParent();
                    favorites.getChildren().remove(treeCell.getTreeItem());
                    try {
                        DatabaseHandler.getDatabaseHandler("main.db").removeFavoriteLocation(treeCell.getTreeItem().getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                item.setGraphic(btnRemoveFromFavorites);
                favorites.getChildren().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            if (mapDrawer.isEditing()) {

                // Only one window open at a time;
                mapDrawer.removeAllPopups();

                mapEditorPopupManager.showAddNodePopup(event);
            }
        });

    }

    /**
     * Shows the help dialog box.
     */
    private void loadHelpDialog() {
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText;
        if (!mapDrawer.isEditing())
            helpText = new Text("Enter your start and end location and any stops graphically or using our menu selector. " +
                    "To use the graphical selection,\nsimply click on the node and click on the set button. " +
                    "To enter a location using the menu, click on the appropriate\ndrop down and choose your location. " +
                    "The node you selected will show up on your map where you can either\nset it to your start or end location or a stop. " +
                    "Once both the start and end nodes are filled in you can press \"Go\" to generate\nyour path. " +
                    "If you want to remove your stops, click on the \"Remove Stop\" button. " +
                    "Favorites can be chosen by clicking\non them in the menu selector and pressing the add button, and removed by pressing the minus button on the node.\n"
            );
        else
            helpText = new Text("Double click to add a node. Click on a node or an edge to edit or remove them. To add a new edge click on\none of the nodes, then \"Add Edge\". Click on another node and click \"Yes\" to add the new edge or \"No\" to cancel it.");

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

    @FXML
    private void handleKeysPressedSearchBar(KeyEvent e) throws IOException {
        String regex = "[ a-zA-Z0-9\\-]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(e.getText());
        if (matcher.matches()) handleItemSearched();

        // Check for backspace
        if (e.getCode() == KeyCode.BACK_SPACE) {
            handleItemSearched();
            if (txtSearch.getText().isEmpty() || txtSearch.getText().length() == 1) populateTreeView();
        }
    }

    /**
     * Shows only the nodes similar to what is being searched for
     */
    @FXML
    private void handleItemSearched() {
        //when an item is searched for, have only objects with that phrase populate the treeview
        String searchBar = txtSearch.getText();
        //Populating TreeView
        TreeItem<String> newRoot = new TreeItem<>("Locations");
        newRoot.setExpanded(true);
        treeLocations.setRoot(newRoot);

        //Adding the nodes
        for (String category : mapCache.getCatNameMap().keySet()) {
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(mapCache.getCatNameMap().get(category));
            List<TreeItem<String>> treeItems = categoryTreeItem.getChildren();
            for (TreeItem<String> c : treeItems) {
                if (c.getValue().toLowerCase().contains(searchBar.toLowerCase())) {
                    newRoot.getChildren().add(c);
                }
            }
        }

        // If nothing is found, say "None"
        if (newRoot.getChildren().isEmpty()) newRoot.setValue("Not found!");
    }
}

package edu.wpi.teamB.views.map;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.map.*;
import edu.wpi.teamB.entities.map.data.Edge;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.CSVHandler;
import edu.wpi.teamB.util.RequestWrapper;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

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

    public static final double coordinateScale = 25 / 9.0;

    private final Map<String, String> categoryNameMap = new HashMap<>();

    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    private final MapCache mc = new MapCache();
    ;
    private MapDrawer md;
    private MapEditorPopupManager mepm;
    private MapPathPopupManager mppm;

    // JavaFX code **************************************************************************************

    @SneakyThrows
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

        //test if we came from a not failed covid survey
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/covidSurvey/covidFormSubmittedNoSymp.fxml")) {
            txtEndLocation.setText("75 Francis Lobby Entrance");
            SceneSwitcher.popLastScene();
        }


        // Set up Load and Save buttons
        btnLoad.setOnAction(event -> loadCSV());

        btnSave.setOnAction(event -> saveCSV());

        // Disable editing if the user is not an admin
        checkPermissions();

        // Makes sure no illegal characters can't be written in the field
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(" a-zA-Z0-9\\-"))
                txtSearch.setText(newValue.replaceAll("[^ a-zA-Z0-9\\-]", ""));
        });
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
        md.drawAllElements();
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

        if (selectedItem.isLeaf()) {
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
    private void handleButtonAction(ActionEvent e) {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":

                md.removeOldPaths();
                md.drawPath(txtStartLocation.getText(), txtEndLocation.getText());
                break;
            case "btnEditMap":

                md.removeAllPopups();
                mppm.removeETAPopup();

                md.setEditing(!md.isEditing());

                md.drawAllElements();
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
            case "btnHelp":
                loadHelpDialog();
            case "btnSearch":
                handleItemSearched();
                break;
        }
    }

    /**
     * Populates the tree view with nodes and categories
     */
    private void populateTreeView() throws IOException {
        //Populating TreeView
        btnAddToFavorites = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/addBtn.fxml")));
        TreeItem<String> rootNode = new TreeItem<>("Root");
        treeLocations.setRoot(rootNode);
        treeLocations.setShowRoot(false);

        TreeItem<String> favorites = new TreeItem<>("Favorites");
        favorites.setExpanded(true);
        rootNode.getChildren().add(favorites);

        favorites.setGraphic(btnAddToFavorites);

        TreeItem<String> locations = new TreeItem<>("Locations");
        locations.setExpanded(true);
        rootNode.getChildren().add(locations);

        //Adding Categories
        for (String category : mc.getCatNameMap().keySet()) {
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(mc.getCatNameMap().get(category));
            locations.getChildren().add(categoryTreeItem);
        }

        // Adding to Favorites
        btnAddToFavorites.setOnAction(event -> {
            try {
                btnRemoveFromFavorites = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/misc/removeBtn.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            TreeItem<String> itemToAdd = treeLocations.getSelectionModel().getSelectedItem();

            if (!favorites.getChildren().contains(itemToAdd) && itemToAdd.isLeaf()) {
                itemToAdd.setGraphic(btnRemoveFromFavorites);
                favorites.getChildren().add(itemToAdd);
            }

            // Removing from Favorites
            btnRemoveFromFavorites.setOnAction(e -> {
                JFXButton itemToRemove = (JFXButton) e.getSource();
                TreeCell<String> treeCell = (TreeCell<String>) itemToRemove.getParent();
                favorites.getChildren().remove(treeCell.getTreeItem());
            });
        });
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
        if (!md.isEditing())
            helpText = new Text("Enter your start and end location graphically or using our menu selector. To use the graphical selection,\nsimply click on the node and click on the set button. To enter a location using the menu. Click on the appropriate\ndrop down and choose your location. The node you selected will show up on your map where you can either\nset it to your start or end location. Once both the start and end nodes are filled in you can press \"Go\" to generate your path");
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
        for (String category : mc.getCatNameMap().keySet()) {
            TreeItem<String> categoryTreeItem = new TreeItem<>(categoryNameMap.get(category));
            categoryTreeItem.getChildren().addAll(mc.getCatNameMap().get(category));
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

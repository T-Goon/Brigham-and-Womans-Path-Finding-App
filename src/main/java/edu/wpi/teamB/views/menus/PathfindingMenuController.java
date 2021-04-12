package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import edu.wpi.teamB.pathfinding.Node;
import edu.wpi.teamB.pathfinding.Read;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PathfindingMenuController implements Initializable {

    @FXML
    private JFXComboBox<String> startLocComboBox;

    @FXML
    private JFXComboBox<String> endLocComboBox;

    @FXML
    private AnchorPane mapHolder;

    @FXML
    private ImageView map;

    @FXML
    private JFXButton btnFindPath;

    @FXML
    private JFXButton btnZoomIn;

    @FXML
    private JFXButton btnZoomOut;

    @FXML
    private JFXButton btnPlaceDot;// TODO This is just and example. Remove it later.

    @FXML
    private JFXButton btnBack;

    private static final double zoomAmount = .2;
    private static final double zoomMin = .1;
    private static final double zoomMax = 10;
    private static final double coordinateScale = 3.33333333333333333333333333333333333333333;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        HashMap<String, Node> locations = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBnodes.csv");

        // Populate the combo boxes with locations
        try{
            for (Node n : locations.values()){
                startLocComboBox.getItems().add(n.getLongName());
                endLocComboBox.getItems().add(n.getLongName());
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()){
            case "btnFindPath":
                // TODO pathfinding stuff
                break;
            case "btnZoomIn":
                zoomMap(true);
                break;
            case "btnZoomOut":
                zoomMap(false);
                break;
            case "btnPlaceDot":
                // TODO This is just and example. Remove it later.

                placeEdge(2077, 2031, 56, 2078);

                placeNode(2077, 2031);

                placeNode(56, 2078);

                break;
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
        }
    }

    /**
     * Zooms the map byt the value.
     * @param value Value to zoom in or out.
     */
    private void zoomMap(boolean value){
        if(mapHolder.getScaleX()*(1+zoomAmount) < PathfindingMenuController.zoomMax &&
                mapHolder.getScaleY()*(1+zoomAmount) < PathfindingMenuController.zoomMax &&
                value){
            mapHolder.setScaleX(mapHolder.getScaleX()*(1+zoomAmount));
            mapHolder.setScaleY(mapHolder.getScaleX()*(1+zoomAmount));
        }

        if(mapHolder.getScaleX()*(1-zoomAmount) > PathfindingMenuController.zoomMin &&
                mapHolder.getScaleY()*(1-zoomAmount) > PathfindingMenuController.zoomMin &&
                !value){
            mapHolder.setScaleX(mapHolder.getScaleX()*(1-zoomAmount));
            mapHolder.setScaleY(mapHolder.getScaleX()*(1-zoomAmount));
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * IMPORTANT!!!: ALWAYS PLACE EDGES BEFORE NODES.
     *
     * @param x x coordinates of node in pixels
     * @param y y coordinates of node in pixels
     */
    public void placeNode(int x, int y) {
        try {
            Circle c = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/misc/node.fxml"));

            c.setCenterX(x/PathfindingMenuController.coordinateScale);
            c.setCenterY(y/PathfindingMenuController.coordinateScale);

            mapHolder.getChildren().add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws an edge between 2 points on the map.
     *
     * IMPORTANT!!!: ALWAYS PLACE EDGES BEFORE NODES.
     *
     * @param xStart x start coordinate in pixels
     * @param yStart y start coordinate in pixels
     * @param xEnd x end coordinate in pixels
     * @param yEnd y end coordinate in pixels
     */
    public void placeEdge(int xStart, int yStart, int xEnd, int yEnd){
        try {
            Line l = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/misc/edge.fxml"));

            l.setStartX(xStart/PathfindingMenuController.coordinateScale);
            l.setStartY(yStart/PathfindingMenuController.coordinateScale);

            l.setEndX(xEnd/PathfindingMenuController.coordinateScale);
            l.setEndY(yEnd/PathfindingMenuController.coordinateScale);

            mapHolder.getChildren().add(l);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the start location
     * @return The long name of the node selected in the combobox.
     */
    public String getStartLocation(){
        return startLocComboBox.getValue();
    }

    /**
     * Gets the end location
     * @return The long name of the node selected in the combobox.
     */
    public String getEndLocation(){
        return endLocComboBox.getValue();
    }
}

package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PathfindingMenuController implements Initializable {

    @FXML
    private JFXComboBox<Label> startLocComboBox;

    @FXML
    private JFXComboBox<Label> endLocComboBox;

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

    private static final double zoomAmount = 0.2;
    private static final double zoomMin = 0.1;
    private static final double zoomMax = 10;
    private static final double coordinateScale = 10 / 3.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Get node info from database
        List<Node> nodes = null;
        try {
            nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodeInformation();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Populate the combo boxes with locations

        assert nodes != null;
        for (Node n : nodes) {
            Label l = new Label();
            String s = n.getLongName();
            l.setText(s);

            // Add new label to combobox
            startLocComboBox.getItems().add(l);
            endLocComboBox.getItems().add(l);

            // Add nodes to map
            placeNode(n.getXCoord(), n.getYCoord());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnFindPath":
                // TODO pathfinding stuff
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
        return startLocComboBox.getValue().getText();
    }

    /**
     * Gets the end location
     *
     * @return The long name of the node selected in the combobox.
     */
    public String getEndLocation() {
        return endLocComboBox.getValue().getText();
    }

    public void runAstr() {
        List<String> path = Graph.AStr(getStartLocation(), getEndLocation());
    }
}

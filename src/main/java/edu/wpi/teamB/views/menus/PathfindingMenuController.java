package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.pannable.PannablePane;
import edu.wpi.teamB.pathfinding.Node;
import edu.wpi.teamB.pathfinding.Read;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

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
    private ImageView map;

    @FXML
    private JFXButton findPathBtn;

    @FXML
    private JFXButton zoomInBtn;

    @FXML
    private JFXButton zoomOutBtn;

    @FXML
    private JFXButton backBtn;

    private static final double zoomAmount = 400;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        HashMap<String, Node> locations = Read.parseCSVNodes();

        for (Node n : locations.values()){
            startLocComboBox.getItems().add(n.getLongName());
            endLocComboBox.getItems().add(n.getLongName());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()){
            case "findPathBtn":
                // TODO pathfinding stuff
                break;
            case "zoomInBtn":
                zoomMap(PathfindingMenuController.zoomAmount);
                break;
            case "zoomOutBtn":
                zoomMap(-PathfindingMenuController.zoomAmount);
                break;
            case "backBtn":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
        }
    }

    @FXML
    private void zoom(ZoomEvent e){
        zoomMap(PathfindingMenuController.zoomAmount);
    }

    /**
     * Zooms the map byt the value.
     * @param value Value to zoom in or out.
     */
    private void zoomMap(double value){
        int w = map.fitWidthProperty().intValue();
        int h = map.fitHeightProperty().intValue();

        map.setFitWidth(w+value);
        map.setFitHeight(h+value);
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

package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamB.pathfinding.Node;
import edu.wpi.teamB.pathfinding.Read;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    private JFXButton findPathBtn;

    @FXML
    private JFXButton backBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        HashMap<String, Node> locations = Read.parseCSVNodes();

        System.out.println(locations.values());

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
            case "backBtn":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
            break;
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

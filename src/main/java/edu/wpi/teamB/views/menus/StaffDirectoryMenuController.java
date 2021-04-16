package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StaffDirectoryMenuController {

    @FXML
    private JFXButton btnDatabase;
    @FXML
    private JFXButton btnEmergency;
    @FXML
    private JFXButton btnDirections;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnServiceRequests;


    @FXML
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        JFXButton btn = (JFXButton) actionEvent.getSource();
        SceneSwitcher.pushScene("/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml");

        switch (btn.getId()) {
            case "btnServiceRequests":
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/pathfindingMenu.fxml");
                break;

            case "btnDatabase"://fix file path
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/serviceRequestDatabase.fxml");
                break;
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/loginPages/staffLogin.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;

        }

    }
}

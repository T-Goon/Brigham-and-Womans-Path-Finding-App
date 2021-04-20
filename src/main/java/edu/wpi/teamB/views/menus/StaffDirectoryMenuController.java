package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnServiceRequests":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnDatabase":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml");
                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}

package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
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
    private JFXButton btnUsers;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnServiceRequests;

    @FXML
    private JFXButton btnBack;




    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnServiceRequests":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestMenu.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnDatabase":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml");
                break;
            case "btnUsers":
                SceneSwitcher.switchScene(getClass(),"/edu/wpi/cs3733/D21/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/menus/userInformationDatabase.fxml");
                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/staffDirectoryMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}

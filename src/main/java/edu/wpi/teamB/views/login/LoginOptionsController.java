package edu.wpi.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginOptionsController {
    @FXML
    private JFXButton btnStaff;

    @FXML
    private JFXButton btnGuest;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnExit;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnStaff":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/login/loginPage.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnCovid":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/covidSurvey/covidSurvey.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}

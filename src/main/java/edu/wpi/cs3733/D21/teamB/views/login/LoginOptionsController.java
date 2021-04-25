package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginOptionsController extends BasePageController {

    @FXML
    private JFXButton btnStaff;

    @FXML
    private JFXButton btnCovid;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/loginOptions.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnStaff":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnCovid":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

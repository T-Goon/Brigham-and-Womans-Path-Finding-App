package edu.wpi.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import edu.wpi.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginOptionsController extends BasePageController {

    @FXML
    private JFXButton btnStaff;

    @FXML
    private JFXButton btnGuest;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/teamB/views/login/loginOptions.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnStaff":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/login/loginPage.fxml");
                break;
            case "btnGuest":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/menus/userDirectoryMenu.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

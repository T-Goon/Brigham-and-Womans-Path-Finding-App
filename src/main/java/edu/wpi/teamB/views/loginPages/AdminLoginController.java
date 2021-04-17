package edu.wpi.teamB.views.loginPages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminLoginController {
    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnLogin":
                SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/menus/adminDirectoryMenu.fxml");
                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass());
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/loginPages/adminLogin.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

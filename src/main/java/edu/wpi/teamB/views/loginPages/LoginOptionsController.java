package edu.wpi.teamB.views.loginPages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class LoginOptionsController {
    @FXML
    private JFXButton btnAdmin;

    @FXML
    private JFXButton btnStaff;

    @FXML
    private JFXButton btnGuest;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnEXit;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnAdmin":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/loginPages/adminLogin.fxml");
                break;
            case "btnStaff":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/loginPages/staffLogin.fxml");
                break;
            case "btnGuest":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
            case "btnEmergency":
                // Not implemented
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}

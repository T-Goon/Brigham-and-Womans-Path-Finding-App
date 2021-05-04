package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SuccessfulRegistrationController {

    @FXML
    public JFXButton btnEmergency;

    @FXML
    public JFXButton btnExit;

    @FXML
    public JFXButton btnReturn;

    @FXML
    private StackPane stackPane;

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnReturn":
                SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}

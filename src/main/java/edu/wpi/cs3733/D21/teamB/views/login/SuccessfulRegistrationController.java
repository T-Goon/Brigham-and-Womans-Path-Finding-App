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
    private JFXButton btnHome;

    @FXML
    private void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml";
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnReturn":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnHome":
                SceneSwitcher.switchScene(currentPath,  "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                break;
        }
    }
}

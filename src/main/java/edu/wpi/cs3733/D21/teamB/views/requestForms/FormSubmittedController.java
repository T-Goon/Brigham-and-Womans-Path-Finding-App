package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FormSubmittedController {

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnExit;

    @FXML
    private void handleButtonAction(ActionEvent e) {

        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnReturn":
                SceneSwitcher.goBack(2);
                break;
            case "btnEmergency":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}
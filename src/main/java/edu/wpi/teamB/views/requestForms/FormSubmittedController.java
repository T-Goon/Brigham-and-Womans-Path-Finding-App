package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FormSubmittedController {

    @FXML
    private JFXButton btnReturn;

    @FXML
    private void handleButtonAction(ActionEvent e) {

        JFXButton btn = (JFXButton) e.getSource();

        if (btn.getId().equals("btnReturn")) {
            SceneSwitcher.goBack(getClass(), 2);
        } else if (btn.getId().equals("btnEmergency")) {
            SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
        } else if(btn.getId().equals("btnExit")){
            Platform.exit();
        }
    }
}
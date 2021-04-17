package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class FormSubmittedController {

    @FXML
    private JFXButton btnReturn;

    @FXML
    private void handleButtonAction(ActionEvent e) {

        JFXButton btn = (JFXButton) e.getSource();

        if (btn.getId().equals("btnReturn")) {
            SceneSwitcher.goBack(getClass());
        } else if (btn.getId().equals("btnEmergency")) {
            SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
        }
    }
}
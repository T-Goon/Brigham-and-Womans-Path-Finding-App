package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class ServiceRequestDatabaseController {
    @FXML
    private JFXButton btnBack;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(getClass());
                break;
        }
    }
}

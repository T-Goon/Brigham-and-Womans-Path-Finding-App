package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class ServiceRequestDatabaseController {
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnEmergency;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch(btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), SceneSwitcher.popScene());
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

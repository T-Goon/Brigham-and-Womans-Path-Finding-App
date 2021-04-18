package edu.wpi.teamB.views.requestForms;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;


public class ServiceRequestDatabaseController implements Initializable {
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnEmergency;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml");
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

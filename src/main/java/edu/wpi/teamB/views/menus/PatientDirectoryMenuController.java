package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class PatientDirectoryMenuController {

    @FXML private JFXButton btnCovid;
    @FXML private JFXButton btnEmergency;
    @FXML private JFXButton btnDirections;
    @FXML private JFXButton btnExit;
    @FXML private JFXButton btnServiceRequests;


    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnServiceRequests":
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnCovid":
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/secondaryForms/covidSurvey.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
        }

    }
}

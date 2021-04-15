package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class PatientDirectoryMenuController {

    @FXML
    private JFXButton btnCovid;
    @FXML
    private JFXButton btnEmergency;
    @FXML
    private JFXButton btnDirections;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnServiceRequests;
    @FXML
    private JFXButton btnMapEditor;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
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
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/pathfindingMenu.fxml");
                break;
            case "btnMapEditor":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/editorIntermediateMenu.fxml");
                break;
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/loginPages/loginOptions.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnCovid":
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/covidSurvey/covidSurvey.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
        }

    }
}

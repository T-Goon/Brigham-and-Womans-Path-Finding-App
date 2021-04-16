package edu.wpi.teamB.views.covidSurvey;

// Java program to create a popup and
// add it to the stage
import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CovidPopupWithSympController implements Initializable {
    @FXML
    private JFXButton btnDirections;

    @FXML
    private JFXButton btnBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
            case "btnDirections":
                //SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/editorIntermediateMenu.fxml");
                break;
        }

    }
}
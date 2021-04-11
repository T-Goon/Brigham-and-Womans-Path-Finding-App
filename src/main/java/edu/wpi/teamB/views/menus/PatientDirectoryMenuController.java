package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class PatientDirectoryMenuController {

    @FXML
    private JFXButton covidBtn;

    @FXML
    private JFXButton emergencyBtn;

    @FXML
    private JFXButton directionsBtn;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton serviceBtn;


    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "serviceBtn":
                try {
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
        }

    }
}

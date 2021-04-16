package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class EmergencyFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private JFXTextField wing;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXTextArea description;

    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        JFXButton btn = (JFXButton) actionEvent.getSource();
//        SceneSwitcher.pushScene("/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");

        switch (btn.getId()) {
            case "btnSubmit":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/formSubmitted.fxml");
                break;
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), SceneSwitcher.popScene());
                break;
            case "btnHelp":
                break;
        }
    }

}

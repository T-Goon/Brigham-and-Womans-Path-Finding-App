package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class EmergencyFormController implements Initializable {

    @FXML
    private JFXTextField wing;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXCheckBox medicalEmergency;

    @FXML
    private JFXCheckBox securityEmergency;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnHelp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSubmit.setDisable(true);
    }

    @FXML
    private void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();
        switch (btn.getId()) {
            case "btnSubmit":

            case "btnCancel":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnHelp":
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                wing.getText().isEmpty() || roomNum.getText().isEmpty() ||
                !(medicalEmergency.isSelected() || securityEmergency.isSelected()) ||
                description.getText().isEmpty()
        );
    }
}

package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MedDeliveryRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextArea reason;


    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                name.getText().isEmpty() || roomNum.getText().isEmpty() || medName.getText().isEmpty() ||
                    reason.getText().isEmpty()
        );
    }
}
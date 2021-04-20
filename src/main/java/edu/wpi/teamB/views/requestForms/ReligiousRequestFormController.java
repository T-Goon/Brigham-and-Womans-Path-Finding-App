package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;

public class ReligiousRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXTextField faith;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox infectious;

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
            name.getText().isEmpty() || roomNum.getText().isEmpty() || date.getValue() == null ||
            startTime.getValue() == null || endTime.getValue() == null || faith.getText().isEmpty() ||
            description.getText().isEmpty()
        );
    }
}
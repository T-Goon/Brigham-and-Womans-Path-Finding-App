package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class MedDeliveryRequestFormController extends DefaultServiceRequestFormController {
    @FXML
    private JFXTextField patName;

    @FXML
    private JFXTextField rmNum;

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextArea reason;
}
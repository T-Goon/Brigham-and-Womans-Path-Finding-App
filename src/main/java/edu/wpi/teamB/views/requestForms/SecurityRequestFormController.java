package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecurityRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField location;

    @FXML
    private JFXComboBox<Label> comboSecurityType;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXTextField detailOne;

    @FXML
    private JFXTextField detailTwo;

    @FXML
    private JFXTextField detailThree;
}


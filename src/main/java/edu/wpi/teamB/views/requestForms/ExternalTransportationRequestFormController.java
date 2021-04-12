package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExternalTransportationRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXComboBox<Label> transpType;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXTextArea allergies;

    @FXML
    private JFXCheckBox unconscious;

    @FXML
    private JFXCheckBox infectious;

    @FXML
    private JFXCheckBox outNetwork;
}

package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ExternalTransportationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXComboBox<Label> comboTranspType;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboTranspType.getItems().add(new Label("Bus"));
        comboTranspType.getItems().add(new Label("Ambulance"));
        comboTranspType.getItems().add(new Label("Helicopter"));
    }
}

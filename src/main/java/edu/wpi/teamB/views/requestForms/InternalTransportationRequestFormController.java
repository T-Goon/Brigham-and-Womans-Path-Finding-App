package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class InternalTransportationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXComboBox<Label> comboTranspType;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox unconscious;

    @FXML
    private JFXCheckBox infectious;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboTranspType.getItems().add(new Label("Wheelchair"));
        comboTranspType.getItems().add(new Label("Stretcher"));
        comboTranspType.getItems().add(new Label("Gurney"));
    }
}

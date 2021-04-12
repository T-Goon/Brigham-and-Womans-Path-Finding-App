package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class InternalTransportationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXComboBox<Label> comboTransportType = new JFXComboBox<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboTransportType.getItems().add(new Label("Wheelchair"));
        comboTransportType.getItems().add(new Label("Stretcher"));
        comboTransportType.getItems().add(new Label("Gurney"));
    }
}

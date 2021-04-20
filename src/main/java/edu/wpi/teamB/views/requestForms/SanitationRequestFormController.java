package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SanitationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField loc;

    @FXML
    private JFXComboBox<Label> comboTypeService;

    @FXML
    private JFXComboBox<Label> comboSizeService;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox safetyHazard;

    @FXML
    private JFXCheckBox biologicalSubstance;

    @FXML
    private JFXCheckBox roomOccupied;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        comboTypeService.getItems().add(new Label("Wet"));
        comboTypeService.getItems().add(new Label("Dry"));
        comboTypeService.getItems().add(new Label("Glass"));

        comboSizeService.getItems().add(new Label("Small"));
        comboSizeService.getItems().add(new Label("Medium"));
        comboSizeService.getItems().add(new Label("Large"));

    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
            loc.getText().isEmpty() || comboTypeService.getValue() == null || comboSizeService.getValue() == null ||
            description.getText().isEmpty()
        );
    }
}

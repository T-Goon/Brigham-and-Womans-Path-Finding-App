package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LaundryRequestFormController extends DefaultServiceRequestFormController implements Initializable {
    @FXML
    private JFXComboBox<Label> comboTypeService = new JFXComboBox<>();

    @FXML
    private JFXComboBox<Label> comboSizeService = new JFXComboBox<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboTypeService.getItems().add(new Label("Regular Cycle"));
        comboTypeService.getItems().add(new Label("Delicate Cycle"));
        comboTypeService.getItems().add(new Label("Permanent Press"));

        comboSizeService.getItems().add(new Label("Small"));
        comboSizeService.getItems().add(new Label("Medium"));
        comboSizeService.getItems().add(new Label("Large"));
    }
}

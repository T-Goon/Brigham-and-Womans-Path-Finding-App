package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SecurityRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField assignedTo;

    @FXML
    private JFXTextField loc;

    @FXML
    private JFXComboBox<Label> comboUrgency;

    @FXML
    private JFXTextArea description;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        for (int i = 1; i <= 10; i++) {
            comboUrgency.getItems().add(new Label(Integer.toString(i)));
        }

    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                assignedTo.getText().isEmpty() || loc.getText().isEmpty() ||
                comboUrgency.getValue() == null || description.getText().isEmpty()
        );
    }
}


package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class FloralDeliveryRequestFormController {

    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXTextField roomNumber;

    @FXML
    private JFXDatePicker deliveryDate;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXButton btnCancel;

    @FXML
    void handleButtonAction(ActionEvent e) throws IOException {
        Button btn = (Button) e.getSource();

        if (btn.getId().equals("btnCancel")) {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        }
    }
}
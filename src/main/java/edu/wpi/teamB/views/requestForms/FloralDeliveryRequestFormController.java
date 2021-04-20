package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.FloralRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FloralDeliveryRequestFormController extends DefaultServiceRequestFormController {

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
    private JFXTextArea message;

    @FXML
    private Label totalPrice;

    @FXML
    private JFXCheckBox roses;

    @FXML
    private JFXCheckBox tulips;

    @FXML
    private JFXCheckBox daisies;

    @FXML
    private JFXCheckBox lilies;

    @FXML
    private JFXCheckBox sunflowers;

    @FXML
    private JFXCheckBox carnations;

    @FXML
    private JFXCheckBox orchids;

    @FXML
    private void updatePrice() {
        validateButton();
        double currentPrice = 0;
        if (roses.isSelected()) currentPrice += 2.99;
        if (tulips.isSelected()) currentPrice += 2.99;
        if (daisies.isSelected()) currentPrice += 2.99;
        if (lilies.isSelected()) currentPrice += 2.99;
        if (sunflowers.isSelected()) currentPrice += 2.99;
        if (carnations.isSelected()) currentPrice += 2.99;
        if (orchids.isSelected()) currentPrice += 2.99;

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        totalPrice.setText("Total Price: $" + nf.format(currentPrice));
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenPatientName = patientName.getText();
            String givenDeliveryDate = deliveryDate.getValue().toString();
            String givenStartTime = startTime.getValue().toString();
            String givenEndTime = endTime.getValue().toString();
            String wantsRoses = roses.isSelected() ? "T" : "F";
            String wantsTulips = tulips.isSelected() ? "T" : "F";
            String wantsDaisies = daisies.isSelected() ? "T" : "F";
            String wantsLilies = lilies.isSelected() ? "T" : "F";
            String wantsSunflowers = sunflowers.isSelected() ? "T" : "F";
            String wantsCarnations = carnations.isSelected() ? "T" : "F";
            String wantsOrchids = orchids.isSelected() ? "T" : "F";

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String location = roomNumber.getText();
            String givenDescription = message.getText();

            FloralRequest request = new FloralRequest(givenPatientName, givenDeliveryDate, givenStartTime, givenEndTime,
                    wantsRoses, wantsTulips, wantsDaisies, wantsLilies, wantsSunflowers, wantsCarnations, wantsOrchids,
                    requestID, time, date, complete, employeeName, location, givenDescription);
            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || roomNumber.getText().isEmpty() ||
                        deliveryDate.getValue() == null || startTime.getValue() == null ||
                        endTime.getValue() == null || message.getText().isEmpty() ||
                        !(roses.isSelected() || tulips.isSelected() || daisies.isSelected() || lilies.isSelected() || sunflowers.isSelected() || carnations.isSelected() || orchids.isSelected())
        );
    }
}
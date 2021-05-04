package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.GiftRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;

import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class GiftRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField patientName;

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
    private JFXCheckBox balloons;

    @FXML
    private JFXCheckBox teddyBear;

    @FXML
    private JFXCheckBox chocolate;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {

            this.id = (String) App.getPrimaryStage().getUserData();
            GiftRequest giftRequest;

            try {
                giftRequest = (GiftRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.GIFT);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            assert giftRequest != null;
            patientName.setText(giftRequest.getPatientName());
            getLocationIndex(giftRequest.getLocation());
            String date = giftRequest.getDeliveryDate();
            LocalDate ld = LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)));
            deliveryDate.setValue(ld);
            String sTime = giftRequest.getStartTime();
            LocalTime slt = LocalTime.of(Integer.parseInt(sTime.substring(0, 2)), Integer.parseInt(sTime.substring(3, 5)));
            startTime.setValue(slt);
            String eTime = giftRequest.getEndTime();
            LocalTime elt = LocalTime.of(Integer.parseInt(eTime.substring(0, 2)), Integer.parseInt(eTime.substring(3, 5)));
            endTime.setValue(elt);
            message.setText(giftRequest.getDescription());
            balloons.setSelected(giftRequest.getWantsBalloons().equals("T"));
            teddyBear.setSelected(giftRequest.getWantsTeddyBear().equals("T"));
            chocolate.setSelected(giftRequest.getWantsChocolate().equals("T"));

            ArrayList<JFXCheckBox> checkBoxes = new ArrayList<>();
            checkBoxes.add(balloons);
            checkBoxes.add(teddyBear);
            checkBoxes.add(chocolate);

            double price = 0;
            for (JFXCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    price += 2.99;
                }
            }
            totalPrice.setText("Total Price: $" + price);
        }
        validateButton();

        // inputting an error message to ensure that all fields are filled in or an error message pops up
        //patient name text field
        RequiredFieldValidator validatorName = new RequiredFieldValidator();
        patientName.getValidators().add(validatorName);
        validatorName.setMessage("Please input the patient's name!");
        patientName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                patientName.validate();
            }
        });

        //location combo box
        RequiredFieldValidator validatorLocation = new RequiredFieldValidator();
        loc.getValidators().add(validatorLocation);
        validatorLocation.setMessage("Please select a location for delivery!");
        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //date picker
        RequiredFieldValidator validatorDate = new RequiredFieldValidator();
        deliveryDate.getValidators().add(validatorDate);
        validatorDate.setMessage("Please select a valid date!");

        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DATE, -1);
        Calendar selectedDate = Calendar.getInstance();

        deliveryDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    selectedDate.setTime(Date.from(deliveryDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    if (selectedDate.compareTo(currentDate) < 0) {
                        deliveryDate.setValue(null);
                    }
                } catch (Exception e) {

                }
                deliveryDate.validate();
            }
        });

        //start time picker
        RequiredFieldValidator validatorStart = new RequiredFieldValidator();
        startTime.getValidators().add(validatorStart);
        validatorStart.setMessage("Please select a valid start time!");
        startTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    if (startTime.getValue().getHour() > endTime.getValue().getHour()) {
                        startTime.setValue(null);
                    }
                } catch (Exception e) {

                }
                startTime.validate();
            }
        });

        //end time picker
        RequiredFieldValidator validatorEnd = new RequiredFieldValidator();
        endTime.getValidators().add(validatorEnd);
        validatorEnd.setMessage("Please select a valid end time!");
        endTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    if (endTime.getValue().getHour() < startTime.getValue().getHour()) {
                        endTime.setValue(null);
                    }
                } catch (Exception e) {

                }
                endTime.validate();
            }
        });

        //message text area
        RequiredFieldValidator validatorMessage = new RequiredFieldValidator();
        message.getValidators().add(validatorMessage);
        validatorMessage.setMessage("Please input a message!");
        message.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                message.validate();
            }
        });
    }

    @FXML
    private void updatePrice() {
        validateButton();
        double currentPrice = 0;
        if (balloons.isSelected()) currentPrice += 0.5;
        if (teddyBear.isSelected()) currentPrice += 2;
        if (chocolate.isSelected()) currentPrice += 1;


        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        totalPrice.setText("Total Price: $" + nf.format(currentPrice));
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenPatientName = patientName.getText();
            String givenDeliveryDate = deliveryDate.getValue().toString();
            String givenStartTime = startTime.getValue().toString();
            String givenEndTime = endTime.getValue().toString();
            String wantsBalloons = balloons.isSelected() ? "T" : "F";
            String wantsTeddyBear = teddyBear.isSelected() ? "T" : "F";
            String wantsChocolate = chocolate.isSelected() ? "T" : "F";

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml"))
                requestID = this.id;
            else requestID = UUID.randomUUID().toString();

            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String givenDescription = message.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = DatabaseHandler.getHandler().getSpecificRequestById(this.id, Request.RequestType.GIFT).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            GiftRequest request = new GiftRequest(givenPatientName, givenDeliveryDate, givenStartTime, givenEndTime,
                    wantsBalloons, wantsTeddyBear, wantsChocolate,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml"))
                    DatabaseHandler.getHandler().updateRequest(request);
                else DatabaseHandler.getHandler().addRequest(request);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || loc.getValue() == null ||
                        deliveryDate.getValue() == null || startTime.getValue() == null ||
                        endTime.getValue() == null || message.getText().isEmpty() ||
                        !(balloons.isSelected() || teddyBear.isSelected() || chocolate.isSelected())
        );
    }

}

package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.FloralRequest;
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

public class FloralDeliveryRequestFormController extends DefaultServiceRequestFormController implements Initializable {

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

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            FloralRequest floralRequest;
            try {
                floralRequest = (FloralRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.FLORAL);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            patientName.setText(floralRequest.getPatientName());
            getLocationIndex(floralRequest.getLocation());
            String date = floralRequest.getDeliveryDate();
            LocalDate ld = LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)));
            deliveryDate.setValue(ld);
            String sTime = floralRequest.getStartTime();
            LocalTime slt = LocalTime.of(Integer.parseInt(sTime.substring(0, 2)), Integer.parseInt(sTime.substring(3, 5)));
            startTime.setValue(slt);
            String eTime = floralRequest.getEndTime();
            LocalTime elt = LocalTime.of(Integer.parseInt(eTime.substring(0, 2)), Integer.parseInt(eTime.substring(3, 5)));
            endTime.setValue(elt);
            message.setText(floralRequest.getDescription());
            roses.setSelected(floralRequest.getWantsRoses().equals("T"));
            tulips.setSelected(floralRequest.getWantsTulips().equals("T"));
            daisies.setSelected(floralRequest.getWantsDaisies().equals("T"));
            lilies.setSelected(floralRequest.getWantsLilies().equals("T"));
            sunflowers.setSelected(floralRequest.getWantsSunflowers().equals("T"));
            carnations.setSelected(floralRequest.getWantsCarnations().equals("T"));
            orchids.setSelected(floralRequest.getWantsOrchids().equals("T"));

            ArrayList<JFXCheckBox> checkBoxes = new ArrayList<>();
            checkBoxes.add(roses);
            checkBoxes.add(tulips);
            checkBoxes.add(daisies);
            checkBoxes.add(lilies);
            checkBoxes.add(sunflowers);
            checkBoxes.add(carnations);
            checkBoxes.add(orchids);

            double price = 0;
            for (JFXCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    price += 2.99;
                }
            }
            totalPrice.setText("Total Price: $" + price);
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //name text field
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
        validatorLocation.setMessage("Please select the location for delivery!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //date picker
        RequiredFieldValidator validatorDate = new RequiredFieldValidator();

        deliveryDate.getValidators().add(validatorDate);
        validatorDate.setMessage("Please select a valid date for delivery!");

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
        RequiredFieldValidator validatorStartTime = new RequiredFieldValidator();

        startTime.getValidators().add(validatorStartTime);
        validatorStartTime.setMessage("Please select a valid start time!");

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
        RequiredFieldValidator validatorEndTime = new RequiredFieldValidator();

        endTime.getValidators().add(validatorEndTime);
        validatorEndTime.setMessage("Please select a valid end time!");

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

        //message text field
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

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
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
                    employeeName = DatabaseHandler.getHandler().getSpecificRequestById(this.id, Request.RequestType.FLORAL).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            FloralRequest request = new FloralRequest(givenPatientName, givenDeliveryDate, givenStartTime, givenEndTime,
                    wantsRoses, wantsTulips, wantsDaisies, wantsLilies, wantsSunflowers, wantsCarnations, wantsOrchids,
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
                        !(roses.isSelected() || tulips.isSelected() || daisies.isSelected() || lilies.isSelected() ||
                                sunflowers.isSelected() || carnations.isSelected() || orchids.isSelected()
                                || super.validateCommon())
        );
    }
}
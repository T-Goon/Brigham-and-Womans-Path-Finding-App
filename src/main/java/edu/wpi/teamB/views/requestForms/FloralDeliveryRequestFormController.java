package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.FloralRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

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
        super.initialize(location,resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            FloralRequest floralRequest = (FloralRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.FLORAL);
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

            ArrayList<JFXCheckBox> checkBoxes = new ArrayList<JFXCheckBox>();
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
            totalPrice.setText("Total Price: $" + Double.toString(price));
        }
        validateButton();
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

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                requestID = this.id;
            } else {
                requestID = UUID.randomUUID().toString();
            }

            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String givenDescription = message.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.FLORAL).getEmployeeName();
            } else {
                employeeName = null;
            }

            FloralRequest request = new FloralRequest(givenPatientName, givenDeliveryDate, givenStartTime, givenEndTime,
                    wantsRoses, wantsTulips, wantsDaisies, wantsLilies, wantsSunflowers, wantsCarnations, wantsOrchids,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
            } else {
                DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
            }
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || loc.getValue() == null ||
                        deliveryDate.getValue() == null || startTime.getValue() == null ||
                        endTime.getValue() == null || message.getText().isEmpty() ||
                        !(roses.isSelected() || tulips.isSelected() || daisies.isSelected() || lilies.isSelected() || sunflowers.isSelected() || carnations.isSelected() || orchids.isSelected())
        );
    }
}
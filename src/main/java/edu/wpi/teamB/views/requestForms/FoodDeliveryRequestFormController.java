package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.FoodRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FoodDeliveryRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXTextField mealChoice;

    @FXML
    private JFXTimePicker arrivalTime;

    @FXML
    private JFXTextArea description;

    public void handleButtonAction(ActionEvent actionEvent) {
        String givenPatientName = name.getText();
        String givenArrivalTime = arrivalTime.getText();
        String givenMealChoice = mealChoice.getText();

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date dateInfo = new Date();

        String requestID = UUID.randomUUID().toString();
        String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
        String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
        String complete = "F";
        String employeeName = null; // fix
        String location = roomNum.getText();
        String givenDescription = description.getText();

        FoodRequest request = new FoodRequest(givenPatientName, givenArrivalTime, givenMealChoice,
                requestID, time, date, complete, employeeName, location, givenDescription);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {
            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }
        super.handleButtonAction(actionEvent);
    }
}
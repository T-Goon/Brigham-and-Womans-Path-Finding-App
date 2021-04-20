package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.CaseManagerRequest;
import edu.wpi.teamB.entities.requests.FoodRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class FoodDeliveryRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField mealChoice;

    @FXML
    private JFXTimePicker arrivalTime;

    @FXML
    private JFXTextArea description;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            String id = (String) App.getPrimaryStage().getUserData();
            FoodRequest foodRequest = (FoodRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.FOOD);
            name.setText(foodRequest.getPatientName());
            roomNum.setText(foodRequest.getLocation());
            mealChoice.setText(foodRequest.getMealChoice());
            String time = foodRequest.getArrivalTime();
            LocalTime lt = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));
            arrivalTime.setValue(lt);
            description.setText(foodRequest.getDescription());
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenPatientName = name.getText();
            String givenArrivalTime = arrivalTime.getValue().toString();
            String givenMealChoice = mealChoice.getText();

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String givenDescription = description.getText();

            FoodRequest request = new FoodRequest(givenPatientName, givenArrivalTime, givenMealChoice,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
            name.getText().isEmpty() || loc.getValue() == null || mealChoice.getText().isEmpty() ||
            arrivalTime.getValue() == null || description.getText().isEmpty()
        );
    }
}
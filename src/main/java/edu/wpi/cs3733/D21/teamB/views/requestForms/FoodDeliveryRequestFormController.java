package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.FoodRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class FoodDeliveryRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField mealChoice;

    @FXML
    private JFXTimePicker arrivalTime;

    @FXML
    private JFXTextArea description;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            FoodRequest foodRequest;
            try {
                foodRequest = (FoodRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.FOOD);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            name.setText(foodRequest.getPatientName());
            getLocationIndex(foodRequest.getLocation());
            mealChoice.setText(foodRequest.getMealChoice());
            String time = foodRequest.getArrivalTime();
            LocalTime lt = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));
            arrivalTime.setValue(lt);
            description.setText(foodRequest.getDescription());
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //patient name text field
        RequiredFieldValidator validatorName = new RequiredFieldValidator();

        name.getValidators().add(validatorName);
        validatorName.setMessage("Please input the patient's name!");

        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                name.validate();
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

        //meal choice text field
        RequiredFieldValidator validatorMealChoice = new RequiredFieldValidator();

        mealChoice.getValidators().add(validatorMealChoice);
        validatorMealChoice.setMessage("Please input your meal choice!");

        mealChoice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                mealChoice.validate();
            }
        });

        //arrival time picker
        RequiredFieldValidator validatorArrival = new RequiredFieldValidator();

        arrivalTime.getValidators().add(validatorArrival);
        validatorArrival.setMessage("Please select your desired arrival time!");

        arrivalTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                arrivalTime.validate();
            }
        });

        //extra information
        RequiredFieldValidator validatorExtra = new RequiredFieldValidator();

        description.getValidators().add(validatorExtra);
        validatorExtra.setMessage("Please input any additional information or type 'none'!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });
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

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                requestID = this.id;
            } else {
                requestID = UUID.randomUUID().toString();
            }

            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String givenDescription = description.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.FOOD).getEmployeeName();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            FoodRequest request = new FoodRequest(givenPatientName, givenArrivalTime, givenMealChoice,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml"))
                    DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
                else DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                name.getText().isEmpty() || loc.getValue() == null || mealChoice.getText().isEmpty() ||
                        arrivalTime.getValue() == null || description.getText().isEmpty()
        );
    }
}
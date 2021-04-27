package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.entities.requests.SocialWorkerRequest;

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

public class SocialWorkerRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXTimePicker timeForArrival;

    @FXML
    private JFXTextArea messageForSocialWorker;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            SocialWorkerRequest socialWorkerRequest;
            try {
                socialWorkerRequest = (SocialWorkerRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.SOCIAL_WORKER);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            patientName.setText(socialWorkerRequest.getPatientName());
            getLocationIndex(socialWorkerRequest.getLocation());
            String time = socialWorkerRequest.getTimeForArrival();
            LocalTime lt = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));
            timeForArrival.setValue(lt);
            messageForSocialWorker.setText(socialWorkerRequest.getDescription());
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
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
        validatorLocation.setMessage("Please select the location!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //arrival time picker
        RequiredFieldValidator validatorArrivalTime = new RequiredFieldValidator();

        timeForArrival.getValidators().add(validatorArrivalTime);
        validatorArrivalTime.setMessage("Please select an arrival time!");

        timeForArrival.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                timeForArrival.validate();
            }
        });

        //message text field
        RequiredFieldValidator validatorMessage = new RequiredFieldValidator();

        messageForSocialWorker.getValidators().add(validatorMessage);
        validatorMessage.setMessage("Please input a message or type 'none'!");

        messageForSocialWorker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                messageForSocialWorker.validate();
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenPatientName = patientName.getText();
            String givenTimeForArrival = timeForArrival.getValue().toString();

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
            String givenDescription = messageForSocialWorker.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.SOCIAL_WORKER).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            SocialWorkerRequest request = new SocialWorkerRequest(givenPatientName, givenTimeForArrival,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                    DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
                } else {
                    DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
                }
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || loc.getValue() == null || timeForArrival.getValue() == null ||
                messageForSocialWorker.getText().isEmpty()
        );
    }
}

package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.CaseManagerRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
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

public class CaseManagerRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXTimePicker timeForArrival;

    @FXML
    private JFXTextArea messageForCaseManager;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            CaseManagerRequest caseManagerRequest = null;
            try {
                caseManagerRequest = (CaseManagerRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.CASE_MANAGER);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            patientName.setText(caseManagerRequest.getPatientName());
            getLocationIndex(caseManagerRequest.getLocation());
            String time = caseManagerRequest.getTimeForArrival();
            LocalTime lt = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));
            timeForArrival.setValue(lt);
            messageForCaseManager.setText(caseManagerRequest.getDescription());
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //patientName text field
        RequiredFieldValidator validatorPatientName = new RequiredFieldValidator();

        patientName.getValidators().add(validatorPatientName);
        validatorPatientName.setMessage("Please input the patient's name!");

        patientName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    patientName.validate();
                }
            }
        });

        //location combo box
        RequiredFieldValidator validatorLocation = new RequiredFieldValidator();

        loc.getValidators().add(validatorLocation);
        validatorLocation.setMessage("Please select the location!");

        loc.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    loc.validate();
                }
            }
        });

        //arrival time picker
        RequiredFieldValidator validatorArrivalTime = new RequiredFieldValidator();

        timeForArrival.getValidators().add(validatorArrivalTime);
        validatorArrivalTime.setMessage("Please select the time for arrival!");

        timeForArrival.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    timeForArrival.validate();
                }
            }
        });

        //message text field
        RequiredFieldValidator validatorMessage = new RequiredFieldValidator();

        messageForCaseManager.getValidators().add(validatorMessage);
        validatorMessage.setMessage("Please input a message for the case manager or type 'none'!");

        messageForCaseManager.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    messageForCaseManager.validate();
                }
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
            String givenDescription = messageForCaseManager.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.CASE_MANAGER).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            CaseManagerRequest request = new CaseManagerRequest(givenPatientName, givenTimeForArrival,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml"))
                    DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
                else DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || loc.getValue() == null || timeForArrival.getValue() == null ||
                        messageForCaseManager.getText().isEmpty()
        );
    }
}

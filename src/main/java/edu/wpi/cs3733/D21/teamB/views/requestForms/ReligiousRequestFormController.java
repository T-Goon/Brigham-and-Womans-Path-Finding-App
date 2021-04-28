package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.ReligiousRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class ReligiousRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXTextField faith;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox infectious;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            ReligiousRequest religiousRequest;
            try {
                religiousRequest = (ReligiousRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.RELIGIOUS);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            name.setText(religiousRequest.getPatientName());
            getLocationIndex(religiousRequest.getLocation());
            String d = religiousRequest.getReligiousDate();
            LocalDate ld = LocalDate.of(Integer.parseInt(d.substring(0, 4)), Integer.parseInt(d.substring(5, 7)), Integer.parseInt(d.substring(8, 10)));
            date.setValue(ld);
            String sTime = religiousRequest.getStartTime();
            LocalTime slt = LocalTime.of(Integer.parseInt(sTime.substring(0, 2)), Integer.parseInt(sTime.substring(3, 5)));
            startTime.setValue(slt);
            String eTime = religiousRequest.getEndTime();
            LocalTime elt = LocalTime.of(Integer.parseInt(eTime.substring(0, 2)), Integer.parseInt(eTime.substring(3, 5)));
            endTime.setValue(elt);
            faith.setText(religiousRequest.getFaith());
            description.setText(religiousRequest.getDescription());
            infectious.setSelected(religiousRequest.getInfectious().equals("T"));
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
        validatorLocation.setMessage("Please select the location for the request!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //date picker
        RequiredFieldValidator validatorDate = new RequiredFieldValidator();

        date.getValidators().add(validatorDate);
        validatorDate.setMessage("Please select the date!");

        date.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                date.validate();
            }
        });

        //start time picker
        RequiredFieldValidator validatorStartTime = new RequiredFieldValidator();

        startTime.getValidators().add(validatorStartTime);
        validatorStartTime.setMessage("Please select the start time!");

        startTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                startTime.validate();
            }
        });

        //end time picker
        RequiredFieldValidator validatorEndTime = new RequiredFieldValidator();

        endTime.getValidators().add(validatorEndTime);
        validatorEndTime.setMessage("Please select the end time!");

        endTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                endTime.validate();
            }
        });

        //faith/denomination text field
        RequiredFieldValidator validatorFaith = new RequiredFieldValidator();

        faith.getValidators().add(validatorFaith);
        validatorFaith.setMessage("Please input the faith/denomination for the request!");

        faith.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                faith.validate();
            }
        });

        //description
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please input any relevant information or type 'none'!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenPatientName = name.getText();
            String givenReligiousDate = date.getValue().toString();
            String givenStartTime = startTime.getValue().toString();
            String givenEndTime = endTime.getValue().toString();
            String givenFaith = faith.getText();
            String givenInfectious = infectious.isSelected() ? "T" : "F";

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
                    employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.RELIGIOUS).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            ReligiousRequest request = new ReligiousRequest(givenPatientName, givenReligiousDate, givenStartTime, givenEndTime, givenFaith, givenInfectious,
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
            name.getText().isEmpty() || loc.getValue() == null || date.getValue() == null ||
            startTime.getValue() == null || endTime.getValue() == null || faith.getText().isEmpty() ||
            description.getText().isEmpty()
        );
    }
}
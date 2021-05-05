package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.MedicineRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class MedDeliveryRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextArea reason;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            MedicineRequest medicineRequest;
            try {
                medicineRequest = (MedicineRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.MEDICINE);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            name.setText(medicineRequest.getPatientName());
            getLocationIndex(medicineRequest.getLocation());
            medName.setText(medicineRequest.getMedicine());
            reason.setText(medicineRequest.getDescription());
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

        //patient name text field
        RequiredFieldValidator validatorMedName = new RequiredFieldValidator();

        medName.getValidators().add(validatorMedName);
        validatorMedName.setMessage("Please input the medicine name!");

        medName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                medName.validate();
            }
        });

        //reason text field
        RequiredFieldValidator validatorReason = new RequiredFieldValidator();

        reason.getValidators().add(validatorReason);
        validatorReason.setMessage("Please input the reason for the request!");

        reason.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                reason.validate();
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenPatientName = name.getText();
            String givenMedicine = medName.getText();

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
            String givenDescription = reason.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = DatabaseHandler.getHandler().getSpecificRequestById(this.id, Request.RequestType.MEDICINE).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            MedicineRequest request = new MedicineRequest(givenPatientName, givenMedicine,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                    DatabaseHandler.getHandler().updateRequest(request);
                } else {
                    DatabaseHandler.getHandler().addRequest(request);
                }
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }


    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                name.getText().isEmpty() || loc.getValue() == null || medName.getText().isEmpty() ||
                    reason.getText().isEmpty() || super.validateCommon()
        );
    }
}
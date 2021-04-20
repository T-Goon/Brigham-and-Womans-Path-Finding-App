package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.CaseManagerRequest;
import edu.wpi.teamB.entities.requests.MedicineRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
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
    private JFXTextField roomNumber;

    @FXML
    private JFXTimePicker timeForArrival;

    @FXML
    private JFXTextArea messageForCaseManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            String id = (String) App.getPrimaryStage().getUserData();
            CaseManagerRequest caseManagerRequest = (CaseManagerRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.CASE_MANAGER);
            patientName.setText(caseManagerRequest.getPatientName());
            String time = caseManagerRequest.getTime();
            LocalTime lt = LocalTime.of(Integer.parseInt(time.substring(0, 1)), Integer.parseInt(time.substring(3, 4)));
            timeForArrival.setValue(lt);
            roomNumber.setText(caseManagerRequest.getLocation());
            messageForCaseManager.setText(caseManagerRequest.getDescription());
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenPatientName = patientName.getText();
            String givenTimeForArrival = timeForArrival.getValue().toString();

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String location = roomNumber.getText();
            String givenDescription = messageForCaseManager.getText();

            CaseManagerRequest request = new CaseManagerRequest(givenPatientName, givenTimeForArrival,
                    requestID, time, date, complete, employeeName, location, givenDescription);

            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || roomNumber.getText().isEmpty() || timeForArrival.getValue() == null ||
                        messageForCaseManager.getText().isEmpty()
        );
    }
}

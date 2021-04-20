package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.SocialWorkerRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SocialWorkerRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXTextField roomNumber;

    @FXML
    private JFXTimePicker timeForArrival;

    @FXML
    private JFXTextArea messageForSocialWorker;

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
            String givenDescription = messageForSocialWorker.getText();

            SocialWorkerRequest request = new SocialWorkerRequest(givenPatientName, givenTimeForArrival,
                    requestID, time, date, complete, employeeName, location, givenDescription);
            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || roomNumber.getText().isEmpty() || timeForArrival.getValue() == null ||
                messageForSocialWorker.getText().isEmpty()
        );
    }
}

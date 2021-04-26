package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.EmergencyRequest;
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


public class EmergencyFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXComboBox<String> loc;

    @FXML
    private JFXCheckBox medicalEmergency;

    @FXML
    private JFXCheckBox securityEmergency;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnHelp;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            EmergencyRequest emergencyRequest = null;
            try {
                emergencyRequest = (EmergencyRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.EMERGENCY);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            if (!SceneSwitcher.isEmergencyBtn) {
                getLocationIndex(emergencyRequest.getLocation());
                medicalEmergency.setSelected(emergencyRequest.getMedicalEmergency().equals("T"));
                securityEmergency.setSelected(emergencyRequest.getSecurityEmergency().equals("T"));
                description.setText(emergencyRequest.getDescription());
            }
        }
        validateButton();
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencySubmitted.fxml");

            String givenMedicalEmergency = medicalEmergency.isSelected() ? "T" : "F";
            String givenSecurityEmergency = securityEmergency.isSelected() ? "T" : "F";

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml") && !SceneSwitcher.isEmergencyBtn) {
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
                    if (!SceneSwitcher.isEmergencyBtn) {
                        employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.EMERGENCY).getEmployeeName();
                    } else {
                        employeeName = "Nobody";
                    }
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            EmergencyRequest request = new EmergencyRequest(givenMedicalEmergency, givenSecurityEmergency,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml") && !SceneSwitcher.isEmergencyBtn)
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
                loc.getValue() == null || !(medicalEmergency.isSelected() || securityEmergency.isSelected()) || description.getText().isEmpty()
        );
    }
}

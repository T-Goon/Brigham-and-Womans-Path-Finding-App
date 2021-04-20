package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.CaseManagerRequest;
import edu.wpi.teamB.entities.requests.ReligiousRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            String id = (String) App.getPrimaryStage().getUserData();
            ReligiousRequest religiousRequest = (ReligiousRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.RELIGIOUS);
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
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
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

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String givenDescription = description.getText();

            ReligiousRequest request = new ReligiousRequest(givenPatientName, givenReligiousDate, givenStartTime, givenEndTime, givenFaith, givenInfectious,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
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
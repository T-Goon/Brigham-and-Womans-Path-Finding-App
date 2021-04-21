package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.requests.ReligiousRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
import edu.wpi.teamB.views.requestForms.DefaultServiceRequestFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;


public class EditUserController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXComboBox authenticationLevel;

    @FXML
    private JFXComboBox job;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnHelp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //should probably not extend: kinda a hac
        super.initialize(location, resources);

        for (User.AuthenticationLevel level : User.AuthenticationLevel.values()) {
            authenticationLevel.getItems().add(new Label(level.toString()));
        }
        for (Request.RequestType r : Request.RequestType.values()) {
            job.getItems().add(new Label(Request.RequestType.prettify(r)));
        }

        User u = (User) App.getPrimaryStage().getUserData();
        username.setText(u.getUsername());
        firstName.setText(u.getFirstName());
        lastName.setText(u.getLastName());
        int i = 0;
        for(Object option : authenticationLevel.getItems()){
            if((String) option == u.getAuthenticationLevel().toString()){
               authenticationLevel.getSelectionModel().select(i);
            }
            i++;
        }
        i = 0;
        for(Object option : job.getItems()){
            if((String) option == u.getAuthenticationLevel().toString()){
                authenticationLevel.getSelectionModel().select(i);
            }
            i++;
        }

        validateButton();

    }

//    public void handleButtonAction(ActionEvent actionEvent) {
//        super.handleButtonAction(actionEvent);
//
//        JFXButton btn = (JFXButton) actionEvent.getSource();
//        if (btn.getId().equals("btnSubmit")) {
//
//            String givenPatientName = name.getText();
//            String givenReligiousDate = date.getValue().toString();
//            String givenStartTime = startTime.getValue().toString();
//            String givenEndTime = endTime.getValue().toString();
//            String givenFaith = faith.getText();
//            String givenInfectious = infectious.isSelected() ? "T" : "F";
//
//            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date dateInfo = new Date();
//
//            String requestID;
//            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
//                requestID = this.id;
//            } else {
//                requestID = UUID.randomUUID().toString();
//            }
//
//            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
//            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
//            String complete = "F";
//            String givenDescription = description.getText();
//
//            String employeeName;
//            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
//                employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.RELIGIOUS).getEmployeeName();
//            } else {
//                employeeName = null;
//            }
//
//            ReligiousRequest request = new ReligiousRequest(givenPatientName, givenReligiousDate, givenStartTime, givenEndTime, givenFaith, givenInfectious,
//                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);
//
//            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
//                DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
//            } else {
//                DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
//            }
//        }
//    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                username.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty() ||
                        authenticationLevel.getValue() == null || job.getValue() == null
        );
    }
}

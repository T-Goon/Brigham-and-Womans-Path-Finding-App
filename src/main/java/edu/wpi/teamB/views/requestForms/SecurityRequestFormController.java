package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.entities.requests.SecurityRequest;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class SecurityRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField assignedTo;

    @FXML
    private JFXComboBox<Label> comboUrgency;

    @FXML
    private JFXTextArea description;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        for (int i = 1; i <= 10; i++) {
            comboUrgency.getItems().add(new Label(Integer.toString(i)));
        }

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            SecurityRequest securityRequest = null;
            try {
                securityRequest = (SecurityRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.SECURITY);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            assignedTo.setText(securityRequest.getEmployeeName());
            getLocationIndex(securityRequest.getLocation());
            int index = securityRequest.getUrgency() - 1;
            comboUrgency.getSelectionModel().select(index);
            description.setText(securityRequest.getDescription());
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //assigned to text field
        RequiredFieldValidator validatorAssignedTo = new RequiredFieldValidator();

        assignedTo.getValidators().add(validatorAssignedTo);
        validatorAssignedTo.setMessage("Please assign the request!");

        assignedTo.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    assignedTo.validate();
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

        //urgency combo box
        RequiredFieldValidator validatorUrgency = new RequiredFieldValidator();

        comboUrgency.getValidators().add(validatorUrgency);
        validatorUrgency.setMessage("Please select the urgency!");

        comboUrgency.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    comboUrgency.validate();
                }
            }
        });

        //description text field
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please provide relevant information or type 'none'!");

        description.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    description.validate();
                }
            }
        });
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                assignedTo.getText().isEmpty() || loc.getValue() == null ||
                        comboUrgency.getValue() == null || description.getText().isEmpty()
        );
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            int givenUrgency = Integer.parseInt(comboUrgency.getValue().getText());

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                requestID = this.id;
            } else {
                requestID = UUID.randomUUID().toString();
            }

            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = assignedTo.getText();
            String givenDescription = description.getText();

            SecurityRequest request = new SecurityRequest(givenUrgency, requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                    DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
                } else {
                    DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
                }
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }

    }
}


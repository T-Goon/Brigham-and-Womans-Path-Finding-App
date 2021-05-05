package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.entities.requests.SecurityRequest;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.AutoCompleteComboBoxListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SecurityRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXComboBox<String> comboAssignedTo;

    @FXML
    private JFXComboBox<String> comboUrgency;

    @FXML
    private JFXTextArea description;

    private String id;
    private final List<User> listStaff = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        for (int i = 1; i <= 10; i++) {
            comboUrgency.getItems().add(Integer.toString(i));
        }

        for (User user : DatabaseHandler.getHandler().getUsersByAuthenticationLevel(User.AuthenticationLevel.STAFF)) {
            comboAssignedTo.getItems().add(user.getFirstName() + " " + user.getLastName());
            listStaff.add(user);
        }

        String employeeName = null;
        int index = -1;
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            SecurityRequest securityRequest;
            try {
                securityRequest = (SecurityRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.SECURITY);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            index = securityRequest.getUrgency() - 1;
            getLocationIndex(securityRequest.getLocation());
            employeeName = securityRequest.getEmployeeName();
            description.setText(securityRequest.getDescription());
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //assigned to text field
        RequiredFieldValidator validatorAssignedTo = new RequiredFieldValidator();

        comboAssignedTo.getValidators().add(validatorAssignedTo);
        validatorAssignedTo.setMessage("Please assign the request!");

        comboAssignedTo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboAssignedTo.validate();
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

        //urgency combo box
        RequiredFieldValidator validatorUrgency = new RequiredFieldValidator();

        comboUrgency.getValidators().add(validatorUrgency);
        validatorUrgency.setMessage("Please select the urgency!");

        comboUrgency.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboUrgency.validate();
            }
        });

        //description text field
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please provide relevant information or type 'none'!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });

        //add searchable combo boxes
        new AutoCompleteComboBoxListener<>(comboUrgency);
        if (index != -1) comboUrgency.getSelectionModel().select(index);

        comboAssignedTo.setVisibleRowCount(3);
        new AutoCompleteComboBoxListener<>(comboAssignedTo);

        if (employeeName != null) setPersonIndex(employeeName);
    }

    /**
     * Sets position of combo box according to employee name
     *
     * @param employeeName the employee name
     */
    protected void setPersonIndex(String employeeName) {
        for (int i = 0; i < listStaff.size(); i++) {
            if ((listStaff.get(i).getFirstName() + " " + listStaff.get(i).getLastName()).equals(employeeName)) {
                comboAssignedTo.getSelectionModel().select(i);
                return;
            }
        }
    }


    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                comboAssignedTo.getSelectionModel().isEmpty() || loc.getValue() == null ||
                        comboUrgency.getValue() == null || description.getText().isEmpty() || super.validateCommon() ||
                        !comboAssignedTo.getItems().contains(comboAssignedTo.getValue()) ||
                        !comboUrgency.getItems().contains(comboUrgency.getValue())
        );
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            int givenUrgency = Integer.parseInt(comboUrgency.getValue());

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
            String employeeName = comboAssignedTo.getSelectionModel().getSelectedItem();
            String givenDescription = description.getText();

            SecurityRequest request = new SecurityRequest(givenUrgency, requestID, time, date, complete, employeeName, getLocation(), givenDescription);

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
}


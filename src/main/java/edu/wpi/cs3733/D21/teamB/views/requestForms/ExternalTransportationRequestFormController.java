package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.ExternalTransportRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
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

public class ExternalTransportationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXComboBox<Label> comboTranspType;

    @FXML
    private JFXTextField destination;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXTextArea allergies;

    @FXML
    private JFXCheckBox unconscious;

    @FXML
    private JFXCheckBox infectious;

    @FXML
    private JFXCheckBox outNetwork;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        comboTranspType.getItems().add(new Label("Bus"));
        comboTranspType.getItems().add(new Label("Ambulance"));
        comboTranspType.getItems().add(new Label("Helicopter"));

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            ExternalTransportRequest externalTransportRequest;
            try {
                externalTransportRequest = (ExternalTransportRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.EXTERNAL_TRANSPORT);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            name.setText(externalTransportRequest.getPatientName());
            getLocationIndex(externalTransportRequest.getLocation());
            int index = -1;
            switch (externalTransportRequest.getTransportType()) {
                case "Bus":
                    index = 0;
                    break;
                case "Ambulance":
                    index = 1;
                    break;
                case "Helicopter":
                    index = 2;
                    break;
            }
            comboTranspType.getSelectionModel().select(index);
            destination.setText(externalTransportRequest.getDestination());
            description.setText(externalTransportRequest.getDescription());
            allergies.setText(externalTransportRequest.getPatientAllergies());
            unconscious.setSelected(externalTransportRequest.getUnconscious().equals("T"));
            infectious.setSelected(externalTransportRequest.getInfectious().equals("T"));
            outNetwork.setSelected(externalTransportRequest.getOutNetwork().equals("T"));
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //name text field
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
        validatorLocation.setMessage("Please input the current location!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //destination text field
        RequiredFieldValidator validatorDestination = new RequiredFieldValidator();

        destination.getValidators().add(validatorDestination);
        validatorDestination.setMessage("Please input the destination address!");

        destination.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                destination.validate();
            }
        });

        //transportation type combo box
        RequiredFieldValidator validatorType = new RequiredFieldValidator();

        comboTranspType.getValidators().add(validatorType);
        validatorType.setMessage("Please select the transportation type!");

        comboTranspType.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboTranspType.validate();
            }
        });

        //transportation details text field
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please input transportation details!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });

        //allergies text field
        RequiredFieldValidator validatorAllergies = new RequiredFieldValidator();

        allergies.getValidators().add(validatorAllergies);
        validatorAllergies.setMessage("Please input any allergies or input 'none'!");

        allergies.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                allergies.validate();
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenPatientName = name.getText();
            String givenTransportType = comboTranspType.getValue().getText();
            String givenDestination = destination.getText();
            String givenPatientAllergies = allergies.getText();
            String givenOutNetwork = outNetwork.isSelected() ? "T" : "F";
            String givenInfectious = infectious.isSelected() ? "T" : "F";
            String givenUnconscious = unconscious.isSelected() ? "T" : "F";

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml"))
                requestID = this.id;
            else requestID = UUID.randomUUID().toString();

            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String givenDescription = description.getText();

            DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = db.getSpecificRequestById(this.id, Request.RequestType.EXTERNAL_TRANSPORT).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            ExternalTransportRequest request = new ExternalTransportRequest(givenPatientName, givenTransportType, givenDestination, givenPatientAllergies, givenOutNetwork, givenInfectious, givenUnconscious,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            try {
                if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml"))
                    db.updateRequest(request);
                else db.addRequest(request);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                name.getText().isEmpty() || loc.getValue() == null || comboTranspType.getValue() == null ||
                        description.getText().isEmpty() || allergies.getText().isEmpty() || destination.getText().isEmpty()
        );
    }
}

package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.entities.requests.SanitationRequest;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
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

public class SanitationRequestFormController extends DefaultServiceRequestFormController implements Initializable {


    @FXML
    private JFXComboBox<Label> comboTypeService;

    @FXML
    private JFXComboBox<Label> comboSizeService;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox safetyHazard;

    @FXML
    private JFXCheckBox biologicalSubstance;

    @FXML
    private JFXCheckBox roomOccupied;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        comboTypeService.getItems().add(new Label("Wet"));
        comboTypeService.getItems().add(new Label("Dry"));
        comboTypeService.getItems().add(new Label("Glass"));

        comboSizeService.getItems().add(new Label("Small"));
        comboSizeService.getItems().add(new Label("Medium"));
        comboSizeService.getItems().add(new Label("Large"));

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            SanitationRequest sanitationRequest;
            try {
                sanitationRequest = (SanitationRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.SANITATION);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            getLocationIndex(sanitationRequest.getLocation());
            int indexType = -1;
            switch (sanitationRequest.getSanitationType()) {
                case "Wet":
                    indexType = 0;
                    break;
                case "Dry":
                    indexType = 1;
                    break;
                case "Glass":
                    indexType = 2;
                    break;
            }
            comboTypeService.getSelectionModel().select(indexType);
            int indexSize = -1;
            switch (sanitationRequest.getSanitationSize()) {
                case "Small":
                    indexSize = 0;
                    break;
                case "Medium":
                    indexSize = 1;
                    break;
                case "Large":
                    indexSize = 2;
                    break;
            }
            comboSizeService.getSelectionModel().select(indexSize);
            description.setText(sanitationRequest.getDescription());
            safetyHazard.setSelected(sanitationRequest.getHazardous().equals("T"));
            biologicalSubstance.setSelected(sanitationRequest.getBiologicalSubstance().equals("T"));
            roomOccupied.setSelected(sanitationRequest.getOccupied().equals("T"));
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //location combo box
        RequiredFieldValidator validatorLocation = new RequiredFieldValidator();

        loc.getValidators().add(validatorLocation);
        validatorLocation.setMessage("Please select the location!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //patient name text field
        RequiredFieldValidator validatorTypeOfService = new RequiredFieldValidator();

        comboTypeService.getValidators().add(validatorTypeOfService);
        validatorTypeOfService.setMessage("Please select the type of service!");

        comboTypeService.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboTypeService.validate();
            }
        });

        //size of service combo box
        RequiredFieldValidator validatorSizeOfService = new RequiredFieldValidator();

        comboSizeService.getValidators().add(validatorSizeOfService);
        validatorSizeOfService.setMessage("Please select the size of service!");

        comboSizeService.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboSizeService.validate();
            }
        });

        //description text field
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please describe the situation!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                loc.getValue() == null || comboTypeService.getValue() == null || comboSizeService.getValue() == null ||
                        description.getText().isEmpty()
        );
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenSanitationType = comboTypeService.getValue().getText();
            String givenSanitationSize = comboSizeService.getValue().getText();
            String givenHazardous = safetyHazard.isSelected() ? "T" : "F";
            String givenBiologicalSubstance = biologicalSubstance.isSelected() ? "T" : "F";
            String givenOccupied = roomOccupied.isSelected() ? "T" : "F";

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
                    employeeName = DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(this.id, Request.RequestType.SANITATION).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            SanitationRequest request = new SanitationRequest(givenSanitationType, givenSanitationSize, givenHazardous, givenBiologicalSubstance, givenOccupied,
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
}

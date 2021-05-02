package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.InternalTransportRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;


public class InternalTransportationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXComboBox<String> comboTranspType;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox unconscious;

    @FXML
    private JFXCheckBox infectious;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        comboTranspType.getItems().add("Wheelchair");
        comboTranspType.getItems().add("Stretcher");
        comboTranspType.getItems().add("Gurney");

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            InternalTransportRequest internalTransportRequest;
            try {
                internalTransportRequest = (InternalTransportRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.INTERNAL_TRANSPORT);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            name.setText(internalTransportRequest.getPatientName());
            getLocationIndex(internalTransportRequest.getLocation());
            int index = -1;
            switch (internalTransportRequest.getTransportType()) {
                case "Wheelchair":
                    index = 0;
                    break;
                case "Stretcher":
                    index = 1;
                    break;
                case "Gurney":
                    index = 2;
                    break;
            }
            comboTranspType.getSelectionModel().select(index);
            description.setText(internalTransportRequest.getDescription());
            unconscious.setSelected(internalTransportRequest.getUnconscious().equals("T"));
            infectious.setSelected(internalTransportRequest.getInfectious().equals("T"));
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
        validatorLocation.setMessage("Please select the transportation location!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //transportation type combo box
        RequiredFieldValidator validatorTransportationType = new RequiredFieldValidator();

        comboTranspType.getValidators().add(validatorTransportationType);
        validatorTransportationType.setMessage("Please select the transportation type!");

        comboTranspType.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboTranspType.validate();
            }
        });

        //description text field
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please provide any additional details or type 'none'!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });

        //add searchable combo box
        comboTranspType.setEditable(true);

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(comboTranspType.getItems());

        //SelectionHandler sets the value of the comboBox
        autoCompletePopup.setSelectionHandler(event -> {
            comboTranspType.setValue(event.getObject());
        });

        TextField editor = comboTranspType.getEditor();
        editor.addEventHandler(KeyEvent.ANY, event -> {
            //The filter method uses the Predicate to filter the Suggestions defined above
            //I choose to use the contains method while ignoring cases
            if(!event.getCode().isNavigationKey()) {
                autoCompletePopup.filter(item -> item.toLowerCase().contains(editor.getText().toLowerCase()));
                //Hide the autocomplete popup if the filtered suggestions is empty or when the box's original popup is open
                if (autoCompletePopup.getFilteredSuggestions().isEmpty()) {
                    autoCompletePopup.hide();
                } else {
                    autoCompletePopup.show(editor);
                }
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenPatientName = name.getText();
            String givenTransportType = comboTranspType.getValue();
            String givenUnconscious = unconscious.isSelected() ? "T" : "F";
            String givenInfectious = infectious.isSelected() ? "T" : "F";

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
                    employeeName = DatabaseHandler.getHandler().getSpecificRequestById(this.id, Request.RequestType.INTERNAL_TRANSPORT).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            InternalTransportRequest request = new InternalTransportRequest(givenPatientName, givenTransportType, givenUnconscious, givenInfectious,
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
                name.getText().isEmpty() || loc.getValue() == null ||
                comboTranspType.getValue() == null || description.getText().isEmpty()
        );
    }
}

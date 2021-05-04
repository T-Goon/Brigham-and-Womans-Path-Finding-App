package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.LaundryRequest;
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

public class LaundryRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXComboBox<String> comboTypeService;

    @FXML
    private JFXComboBox<String> comboSizeService;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox darks;

    @FXML
    private JFXCheckBox lights;

    @FXML
    private JFXCheckBox roomOccupied;

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        comboTypeService.getItems().add("Regular Cycle");
        comboTypeService.getItems().add("Delicate Cycle");
        comboTypeService.getItems().add("Permanent Press");

        comboSizeService.getItems().add("Small");
        comboSizeService.getItems().add("Medium");
        comboSizeService.getItems().add("Large");

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            LaundryRequest laundryRequest;
            try {
                laundryRequest = (LaundryRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.LAUNDRY);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            getLocationIndex(laundryRequest.getLocation());
            int indexType = -1;
            switch (laundryRequest.getServiceType()) {
                case "Regular Cycle":
                    indexType = 0;
                    break;
                case "Delicate Cycle":
                    indexType = 1;
                    break;
                case "Permanent Press":
                    indexType = 2;
                    break;
            }
            comboTypeService.getSelectionModel().select(indexType);
            int indexSize = -1;
            switch (laundryRequest.getServiceSize()) {
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
            description.setText(laundryRequest.getDescription());
            darks.setSelected(laundryRequest.getDark().equals("T"));
            lights.setSelected(laundryRequest.getLight().equals("T"));
            roomOccupied.setSelected(laundryRequest.getOccupied().equals("T"));
        }
        validateButton();

        //creating a pop-up error message when a text field is left empty
        //location combo box
        RequiredFieldValidator validatorLocation = new RequiredFieldValidator();

        loc.getValidators().add(validatorLocation);
        validatorLocation.setMessage("Please select a location!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        //type of service combo box
        RequiredFieldValidator validatorTypeOfService = new RequiredFieldValidator();

        comboTypeService.getValidators().add(validatorTypeOfService);
        validatorTypeOfService.setMessage("Please select a type of service!");

        comboTypeService.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboTypeService.validate();
            }
        });

        //size of service combo box
        RequiredFieldValidator validatorSizeOfService = new RequiredFieldValidator();

        comboSizeService.getValidators().add(validatorSizeOfService);
        validatorSizeOfService.setMessage("Please select the size of the service!");

        comboSizeService.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comboSizeService.validate();
            }
        });

        //description text field
        RequiredFieldValidator validatorDescription = new RequiredFieldValidator();

        description.getValidators().add(validatorDescription);
        validatorDescription.setMessage("Please input a description or type 'none'!");

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                description.validate();
            }
        });

        //searchable combo boxes
        comboTypeService.setEditable(true);

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(comboTypeService.getItems());

        //SelectionHandler sets the value of the comboBox
        autoCompletePopup.setSelectionHandler(event -> {
            comboTypeService.setValue(event.getObject());
        });

        TextField editor = comboTypeService.getEditor();
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

        comboSizeService.setEditable(true);

        JFXAutoCompletePopup<String> autoCompletePopup2 = new JFXAutoCompletePopup<>();
        autoCompletePopup2.getSuggestions().addAll(comboSizeService.getItems());

        //SelectionHandler sets the value of the comboBox
        autoCompletePopup2.setSelectionHandler(event -> {
            comboSizeService.setValue(event.getObject());
        });

        TextField editor2 = comboSizeService.getEditor();
        editor2.addEventHandler(KeyEvent.ANY, event -> {
            //The filter method uses the Predicate to filter the Suggestions defined above
            //I choose to use the contains method while ignoring cases
            if(!event.getCode().isNavigationKey()) {
                autoCompletePopup2.filter(item -> item.toLowerCase().contains(editor2.getText().toLowerCase()));
                //Hide the autocomplete popup if the filtered suggestions is empty or when the box's original popup is open
                if (autoCompletePopup2.getFilteredSuggestions().isEmpty()) {
                    autoCompletePopup2.hide();
                } else {
                    autoCompletePopup2.show(editor2);
                }
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenServiceType = comboTypeService.getValue();
            String givenServiceSize = comboSizeService.getValue();
            String givenDark = darks.isSelected() ? "T" : "F";
            String givenLight = lights.isSelected() ? "T" : "F";
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
                    employeeName = DatabaseHandler.getHandler().getSpecificRequestById(this.id, Request.RequestType.LAUNDRY).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            LaundryRequest request = new LaundryRequest(givenServiceType, givenServiceSize, givenDark, givenLight, givenOccupied,
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
    private void validateButton() {
        btnSubmit.setDisable(
                loc.getValue() == null || comboSizeService.getValue() == null || comboTypeService.getValue() == null || description.getText().isEmpty()
        );
    }
}

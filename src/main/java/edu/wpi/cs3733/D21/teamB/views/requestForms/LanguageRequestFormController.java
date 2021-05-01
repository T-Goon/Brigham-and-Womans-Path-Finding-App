package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.LanguageRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXComboBox<Label> loc;

    @FXML
    private JFXComboBox<String> language;

    @FXML
    private JFXTimePicker timeForArrival;

    @FXML
    private JFXTextArea message;

    private String id;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        language.setEditable(true);

        language.getItems().add("Chinese");
        language.getItems().add("French");
        language.getItems().add("Russian");
        language.getItems().add("Spanish");
        language.getItems().add("Vietnamese");

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(language.getItems());

        //SelectionHandler sets the value of the comboBox
        autoCompletePopup.setSelectionHandler(event -> {
            language.setValue(event.getObject());
        });

        TextField editor = language.getEditor();
        editor.addEventHandler(KeyEvent.ANY,event -> {
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

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            LanguageRequest languageRequest;
            try {
                languageRequest = (LanguageRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.LANGUAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            patientName.setText(languageRequest.getPatientName());
            getLocationIndex(languageRequest.getLocation());
            String time = languageRequest.getTimeForArrival();
            LocalTime lt = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));
            timeForArrival.setValue(lt);
            message.setText(languageRequest.getDescription());

            getLocationIndex(languageRequest.getLocation());
            int indexType = -1;

            switch (languageRequest.getLanguage()) {
                case "Chinese":
                    indexType = 0;
                    break;
                case "French":
                    indexType = 1;
                    break;
                case "Russian":
                    indexType = 2;
                    break;
                case "Spanish":
                    indexType = 3;
                    break;
                case "Vietnamese":
                    indexType = 4;
                    break;
            }
            language.getSelectionModel().select(indexType);
        }
        validateButton();

        RequiredFieldValidator validatorName = new RequiredFieldValidator();

        patientName.getValidators().add(validatorName);
        validatorName.setMessage("Please input the patient's name!");

        patientName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                patientName.validate();
            }
        });

        RequiredFieldValidator validatorLocation = new RequiredFieldValidator();

        loc.getValidators().add(validatorLocation);
        validatorLocation.setMessage("Please select the location!");

        loc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loc.validate();
            }
        });

        RequiredFieldValidator validatorLanguage = new RequiredFieldValidator();

        language.getValidators().add(validatorLanguage);
        validatorLanguage.setMessage("Please select a language!");

        language.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                language.validate();
            }
        });

        RequiredFieldValidator validatorTimeForArrival = new RequiredFieldValidator();

        timeForArrival.getValidators().add(validatorTimeForArrival);
        validatorTimeForArrival.setMessage("Please select the time of arrival!");

        timeForArrival.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                timeForArrival.validate();
            }
        });


        RequiredFieldValidator validatorMessage = new RequiredFieldValidator();

        message.getValidators().add(validatorMessage);
        validatorMessage.setMessage("Please input any additional details or type 'none' !");

        message.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                message.validate();
            }
        });
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);

        JFXButton btn = (JFXButton) e.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenPatientName = patientName.getText();
            String languageChosen = language.getValue();
            String givenTimeForArrival = timeForArrival.getValue().toString();

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
            String givenDescription = message.getText();

            String employeeName;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
                try {
                    employeeName = DatabaseHandler.getHandler().getSpecificRequestById(this.id, Request.RequestType.LANGUAGE).getEmployeeName();
                } catch (SQLException err) {
                    err.printStackTrace();
                    return;
                }
            } else {
                employeeName = null;
            }

            LanguageRequest request = new LanguageRequest(languageChosen, givenPatientName, givenTimeForArrival,
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
                patientName.getText().isEmpty() || loc.getValue() == null || timeForArrival.getValue() == null ||
                        message.getText().isEmpty()
        );
    }
}
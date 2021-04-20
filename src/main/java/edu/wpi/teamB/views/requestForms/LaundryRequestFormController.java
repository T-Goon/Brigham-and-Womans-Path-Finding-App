package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.LaundryRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class LaundryRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField loc;

    @FXML
    private JFXComboBox<Label> comboTypeService;

    @FXML
    private JFXComboBox<Label> comboSizeService;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox darks;

    @FXML
    private JFXCheckBox lights;

    @FXML
    private JFXCheckBox roomOccupied;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        comboTypeService.getItems().add(new Label("Regular Cycle"));
        comboTypeService.getItems().add(new Label("Delicate Cycle"));
        comboTypeService.getItems().add(new Label("Permanent Press"));

        comboSizeService.getItems().add(new Label("Small"));
        comboSizeService.getItems().add(new Label("Medium"));
        comboSizeService.getItems().add(new Label("Large"));
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String givenServiceType = comboTypeService.getValue().getText();
            String givenServiceSize = comboSizeService.getValue().getText();
            String givenDark = darks.isSelected() ? "T" : "F";
            String givenLight = lights.isSelected() ? "T" : "F";
            String givenOccupied = roomOccupied.isSelected() ? "T" : "F";

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String location = loc.getText();
            String givenDescription = description.getText();

            LaundryRequest request = new LaundryRequest(givenServiceType, givenServiceSize, givenDark, givenLight, givenOccupied,
                    requestID, time, date, complete, employeeName, location, givenDescription);

            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
            loc.getText().isEmpty() || comboSizeService.getValue() == null || comboTypeService.getValue() == null ||
            description.getText().isEmpty()
        );
    }
}

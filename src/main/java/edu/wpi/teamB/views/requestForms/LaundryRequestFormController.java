package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.LaundryRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
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

    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        comboTypeService.getItems().add(new Label("Regular Cycle"));
        comboTypeService.getItems().add(new Label("Delicate Cycle"));
        comboTypeService.getItems().add(new Label("Permanent Press"));

        comboSizeService.getItems().add(new Label("Small"));
        comboSizeService.getItems().add(new Label("Medium"));
        comboSizeService.getItems().add(new Label("Large"));

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            LaundryRequest laundryRequest = (LaundryRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.LAUNDRY);
            getLocationIndex(laundryRequest.getLocation());
            int indexType = -1;
            if (laundryRequest.getServiceType().equals("Regular Cycle")) {
                indexType = 0;
            } else if (laundryRequest.getServiceType().equals("Delicate Cycle")) {
                indexType = 1;
            } else if (laundryRequest.getServiceType().equals("Permanent Press")) {
                indexType = 2;
            }
            comboTypeService.getSelectionModel().select(indexType);
            int indexSize = -1;
            if (laundryRequest.getServiceSize().equals("Small")) {
                indexSize = 0;
            } else if (laundryRequest.getServiceSize().equals("Medium")) {
                indexSize = 1;
            } else if (laundryRequest.getServiceSize().equals("Large")) {
                indexSize = 2;
            }
            comboSizeService.getSelectionModel().select(indexSize);
            description.setText(laundryRequest.getDescription());
            darks.setSelected(laundryRequest.getDark().equals("T"));
            lights.setSelected(laundryRequest.getLight().equals("T"));
            roomOccupied.setSelected(laundryRequest.getOccupied().equals("T"));
        }
        validateButton();
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

            String requestID;
            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                requestID = this.id;
            } else {
                requestID = UUID.randomUUID().toString();
            }

            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String givenDescription = description.getText();

            LaundryRequest request = new LaundryRequest(givenServiceType, givenServiceSize, givenDark, givenLight, givenOccupied,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
                DatabaseHandler.getDatabaseHandler("main.db").updateRequest(request);
            } else {
                DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
            }
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
            loc.getValue() == null || comboSizeService.getValue() == null || comboTypeService.getValue() == null ||
            description.getText().isEmpty()
        );
    }
}

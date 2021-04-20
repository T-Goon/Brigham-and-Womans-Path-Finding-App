package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.ExternalTransportRequest;
import edu.wpi.teamB.entities.requests.InternalTransportRequest;
import edu.wpi.teamB.entities.requests.Request;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;


public class InternalTransportationRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXComboBox<Label> comboTranspType;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXCheckBox unconscious;

    @FXML
    private JFXCheckBox infectious;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        comboTranspType.getItems().add(new Label("Wheelchair"));
        comboTranspType.getItems().add(new Label("Stretcher"));
        comboTranspType.getItems().add(new Label("Gurney"));

        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml")) {
            String id = (String) App.getPrimaryStage().getUserData();
            InternalTransportRequest internalTransportRequest = (InternalTransportRequest) DatabaseHandler.getDatabaseHandler("main.db").getSpecificRequestById(id, Request.RequestType.INTERNAL_TRANSPORT);
            name.setText(internalTransportRequest.getPatientName());
            roomNum.setText(internalTransportRequest.getLocation());
            int index = -1;
            if (internalTransportRequest.getTransportType().equals("Wheelchair")) {
                index = 0;
            } else if (internalTransportRequest.getTransportType().equals("Stretcher")) {
                index = 1;
            } else if (internalTransportRequest.getTransportType().equals("Gurney")) {
                index = 2;
            }
            comboTranspType.getSelectionModel().select(index);
            description.setText(internalTransportRequest.getDescription());
            unconscious.setSelected(internalTransportRequest.getUnconscious().equals("T"));
            infectious.setSelected(internalTransportRequest.getInfectious().equals("T"));
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {
            String givenPatientName = name.getText();
            String givenTransportType = comboTranspType.getValue().getText();
            String givenUnconscious = unconscious.isSelected() ? "T" : "F";
            String givenInfectious = infectious.isSelected() ? "T" : "F";

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = null; // fix
            String givenDescription = description.getText();

            InternalTransportRequest request = new InternalTransportRequest(givenPatientName, givenTransportType, givenUnconscious, givenInfectious,
                    requestID, time, date, complete, employeeName, getLocation(), givenDescription);

            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
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

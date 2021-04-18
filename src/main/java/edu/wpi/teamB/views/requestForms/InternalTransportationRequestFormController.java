package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.InternalTransportRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import java.io.IOException;
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
    private JFXTextField roomNum;

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
        comboTranspType.getItems().add(new Label("Wheelchair"));
        comboTranspType.getItems().add(new Label("Stretcher"));
        comboTranspType.getItems().add(new Label("Gurney"));
    }

    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        super.handleButtonAction(actionEvent);

        String givenPatientName = name.getText();
        String givenTransportType = comboTranspType.getValue().toString();
        String givenUnconscious = unconscious.isSelected() ? "T" : "F";
        String givenInfectious = infectious.isSelected() ? "T" : "F";

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date dateInfo = new Date();
        String formattedTime = timeFormat.format(dateInfo);
        String formattedDate = dateFormat.format(dateInfo);

        String requestID = UUID.randomUUID().toString();
        String time = formattedTime; // Stored as HH:MM (24 hour time)
        String date = formattedDate; // Stored as YYYY-MM-DD
        String complete = "F";
        String employeeName = null; // fix
        String location = roomNum.getText();
        String givenDescription = description.getText();

        InternalTransportRequest request = new InternalTransportRequest(givenPatientName, givenTransportType, givenUnconscious, givenInfectious,
                requestID, time, date, complete, employeeName, location, givenDescription);
        DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
    }
}

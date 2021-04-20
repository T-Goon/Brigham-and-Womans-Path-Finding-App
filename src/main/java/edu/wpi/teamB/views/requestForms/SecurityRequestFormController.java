package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.requests.SecurityRequest;
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

public class SecurityRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXTextField assignedTo;

    @FXML
    private JFXTextField loc;

    @FXML
    private JFXComboBox<Label> comboUrgency;

    @FXML
    private JFXTextArea description;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        for (int i = 1; i <= 10; i++) {
            comboUrgency.getItems().add(new Label(Integer.toString(i)));
        }

    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                assignedTo.getText().isEmpty() || loc.getText().isEmpty() ||
                comboUrgency.getValue() == null || description.getText().isEmpty()
        );
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {
            int givenUrgency = Integer.parseInt(comboUrgency.getValue().getText());

            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInfo = new Date();

            String requestID = UUID.randomUUID().toString();
            String time = timeFormat.format(dateInfo); // Stored as HH:MM (24 hour time)
            String date = dateFormat.format(dateInfo); // Stored as YYYY-MM-DD
            String complete = "F";
            String employeeName = assignedTo.getText();
            String location = loc.getText();
            String givenDescription = description.getText();

            SecurityRequest request = new SecurityRequest(givenUrgency, requestID, time, date, complete, employeeName, location, givenDescription);
            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        }

    }
}


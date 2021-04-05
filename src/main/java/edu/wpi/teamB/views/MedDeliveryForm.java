package edu.wpi.teamB.views;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MedDeliveryForm {
    @FXML
    private Button cancelBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private Button helpBtn;

    @FXML
    private TextField patName;

    @FXML
    private TextField rmNum;

    @FXML
    private TextField medName;

    @FXML
    private TextArea reason;

    public void handleButtonAction(ActionEvent e) {
        Button bnt = (Button) e.getSource();

        if (bnt.getId().equals("cancelBtn")) {
            // Go back to the service request menu
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (bnt.getId().equals("submitBtn")) {
            //Show the confirmation page
        }

        if (bnt.getId().equals("helpBtn")) {
            //Show the help page
        }

    }
}
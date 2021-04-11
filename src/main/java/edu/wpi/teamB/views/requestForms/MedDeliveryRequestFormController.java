package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MedDeliveryRequestFormController {

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton emergencyBtn;

    @FXML
    private JFXButton submitBtn;

    @FXML
    private JFXButton helpBtn;

    @FXML
    private JFXTextField patName;

    @FXML
    private JFXTextField rmNum;

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextArea reason;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        Button bnt = (Button) e.getSource();

        if (bnt.getId().equals("cancelBtn")) {
            // Go back to the service request menu
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (bnt.getId().equals("submitBtn")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/requestForms/formSubmitted.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
                }
        }

        if (bnt.getId().equals("helpBtn")) {
            //Show the help page
        }

        if (bnt.getId().equals("emergencyBtn")) {
            //Show the help page
        }

    }
}
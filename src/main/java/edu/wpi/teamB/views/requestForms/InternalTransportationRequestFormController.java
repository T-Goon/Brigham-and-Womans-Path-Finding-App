package edu.wpi.teamB.views.requestForms;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class InternalTransportationRequestFormController {

    @FXML
    private Button btnCancel;
    @FXML
    private Button SubmitB;
    @FXML
    private Button HelpB;
    @FXML
    private TextField NAME;
    @FXML
    private TextField ROOMNUM;
    @FXML
    private TextField TRTYPE;
    @FXML
    private TextArea REASON;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        Button bnt = (Button) e.getSource();

        if (bnt.getId().equals("btnCancel")) {
            // Go back to the service request menu
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (bnt.getId().equals("SubmitB")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/requestForms/formSubmitted.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);

            } catch (IOException ex) {
                ex.printStackTrace();
                }
        }

        if (bnt.getId().equals("HelpB")) {
            //Show the help page
        }

    }


}

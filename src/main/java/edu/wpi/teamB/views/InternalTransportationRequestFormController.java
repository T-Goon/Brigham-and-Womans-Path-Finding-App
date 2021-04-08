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
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/serviceRequestMenu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (bnt.getId().equals("SubmitB")) {
            //Show the confirmation page
        }

        if (bnt.getId().equals("HelpB")) {
            //Show the help page
        }

    }


}

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

public class InternalDeliveryReqController {

    @FXML
    private Button CancelB;
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
    private void handleButtonAction(ActionEvent e){
        Button bnt = (Button)e.getSource();
        if(bnt.getId().equals("CancelB")){
            // Ends request and returns to splash page without submitting
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(bnt.getId().equals("SubmitB")){
            // Ends request and returns to splash page while also submitting a request
            //Maybe read the text here and then send it to the system
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
                //Add method to send request into system
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(bnt.getId().equals("HelpB")){
            // Opens the help screen
            try {
                //Replace this path with the path for the help screen
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

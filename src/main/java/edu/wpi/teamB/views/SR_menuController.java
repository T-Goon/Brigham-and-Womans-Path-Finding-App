package edu.wpi.teamB.views;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class SR_menuController {
    @FXML
    private Button blankFormBtn;
    @FXML
    private Button SSFormBtn;

    @FXML
    private void handleButtonAction(ActionEvent e){

        Button bnt = (Button)e.getSource();

        if(bnt.getId().equals("blankFormBtn")){
            // Open blank service request form
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/blankForm.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(bnt.getId().equals("SSFormBtn")){
            // Open security service request form
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/securityRequestForm.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}

package edu.wpi.teamB.views;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class blankFormController {

    @FXML
    private Button backBtn;

    @FXML
    private void handleButtonAction(ActionEvent e){

        Button bnt = (Button)e.getSource();

        if(bnt.getId().equals("backBtn")){
            // Open blank service request form
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

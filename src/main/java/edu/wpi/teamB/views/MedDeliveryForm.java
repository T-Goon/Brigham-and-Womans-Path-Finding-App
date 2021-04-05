package edu.wpi.teamB.views;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MedDeliveryForm {
    public void handleButtonAction(ActionEvent e) {
        Button bnt = (Button)e.getSource();

        if(bnt.getId().equals("cancelBtn")){
            // Go back to the service request menu
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }
    }
}

package edu.wpi.teamB.views.requestForms;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class SecurityRequestFormController {

    @FXML
    private Button backBtn;

    @FXML
    void handleBackButton(ActionEvent e) throws IOException {
        Button btn = (Button) e.getSource();

        if (btn.getId().equals("backBtn")) {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        }
    }
}

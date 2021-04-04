package edu.wpi.teamB.views;

import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecurityRequestController implements Initializable {
    @FXML
    private ChoiceBox<String> typeCB;
    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        // Choice box items
        typeCB.getItems().add("Type");
        typeCB.setValue("Type");
    }

    @FXML
    void handleBackButton(ActionEvent e) throws IOException {
        Button btn = (Button)e.getSource();

        if(btn.getId().equals("backBtn")){
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/SR_menu.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        }
    }
}

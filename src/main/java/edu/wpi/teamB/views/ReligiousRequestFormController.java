package edu.wpi.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ReligiousRequestFormController {

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch(btn.getId()) {
            case "btnCancel":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/serviceRequestMenu.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            case "btnHelp":
                // handle help button
                break;
            case "btnSubmit":
                // handle submit button
                break;
            case "btnEmergency":
                // handle emergency button
                break;
        }
    }
}
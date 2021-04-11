package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

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
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "cancelBtn":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            case "helpBtn":
                // handle help button
                break;
            case "submitBtn":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/requestForms/formSubmitted.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }

        }
    }
}
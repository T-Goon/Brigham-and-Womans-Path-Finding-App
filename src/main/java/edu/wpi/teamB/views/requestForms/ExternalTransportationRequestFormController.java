package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ExternalTransportationRequestFormController {

    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton SubmitB;
    @FXML
    private JFXButton HelpB;
    @FXML
    private JFXTextField NAME;
    @FXML
    private JFXTextField ROOMNUM;
    @FXML
    private JFXComboBox TRTYPE;
    @FXML
    private JFXTextArea DESCRIPTION;
    @FXML
    private JFXTextArea ALLERGIES;
    @FXML
    private JFXCheckBox UNCONSCIOUS;
    @FXML
    private JFXCheckBox INFECTIOUS;
    @FXML
    private JFXCheckBox OUTNETWORK;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        JFXButton bnt = (JFXButton) e.getSource();

        if (bnt.getId().equals("btnCancel")) {
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

    }


}

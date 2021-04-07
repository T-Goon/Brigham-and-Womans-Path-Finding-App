package edu.wpi.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class ServiceRequestMenuController {

    @FXML
    private JFXButton btnMedicine;

    @FXML
    private JFXButton btnSanitation;

    @FXML
    private JFXButton btnIntTransp;

    @FXML
    private JFXButton btnExtTransp;

    @FXML
    private JFXButton btnSecurity;

    @FXML
    private JFXButton btnFloralDelivery;

    @FXML
    private JFXButton btnReligiousRequest;

    @FXML
    private JFXButton btnLaundryRequest;

    @FXML
    private JFXButton btnFoodDelivery;

    @FXML
    private void handleButtonAction(ActionEvent e) {

        Button btn = (Button) e.getSource();

        String path = "/edu/wpi/teamB/views/";
        switch (btn.getId()) {
            case "btnMedicine":
                path = path + "medDeliveryRequestForm.fxml";
                break;
            case "btnSanitation":
                path = path + "sanitationRequestForm.fxml";
                break;
            case "btnIntTransp":
                path = path + "internalTransportationRequestForm.fxml";
                break;
            case "btnSecurity":
                path = path + "securityRequestForm.fxml";
                break;
            case "btnFloralDelivery":
                path = path + "floralDeliveryRequestForm.fxml";
                break;
            case "btnExtTransp":
                path = path + "ExternalTransportationRequestForm.fxml";
                break;
            case "btnReligiousRequest":
                path = path + "religiousRequestForm.fxml";
                break;
            case "btnFoodDelivery":
                path = path + "foodDeliveryRequestForm.fxml";
                break;
            case "btnLaundryRequest":
                path = path + "laundryRequestForm.fxml";
                break;
            default:
                throw new IllegalStateException("WHAT BUTTON IS THIS AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

package edu.wpi.teamB.views.menus;

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
    private JFXButton backBtn;

    @FXML
    private void handleButtonAction(ActionEvent e) {

        Button btn = (Button) e.getSource();

        String viewsPath = "/edu/wpi/teamB/views/requestForms/";
        String menusPath = "/edu/wpi/teamB/views/menus/";

        String path;
        switch (btn.getId()) {
            case "btnMedicine":
                path = viewsPath + "medDeliveryRequestForm.fxml";
                break;
            case "btnSanitation":
                path = viewsPath + "sanitationRequestForm.fxml";
                break;
            case "btnIntTransp":
                path = viewsPath + "internalTransportationRequestForm.fxml";
                break;
            case "btnSecurity":
                path = viewsPath + "securityRequestForm.fxml";
                break;
            case "btnFloralDelivery":
                path = viewsPath + "floralDeliveryRequestForm.fxml";
                break;
            case "btnExtTransp":
                path = viewsPath + "externalTransportationRequestForm.fxml";
                break;
            case "btnReligiousRequest":
                path = viewsPath + "religiousRequestForm.fxml";
                break;
            case "btnFoodDelivery":
                path = viewsPath + "foodDeliveryRequestForm.fxml";
                break;
            case "btnLaundryRequest":
                path = viewsPath + "laundryRequestForm.fxml";
                break;
            case "backBtn":
                path = menusPath + "patientDirectoryMenu.fxml";
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

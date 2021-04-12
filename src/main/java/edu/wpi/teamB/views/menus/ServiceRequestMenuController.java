package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ServiceRequestMenuController {

    private static final String VIEWS_PATH = "/edu/wpi/teamB/views/requestForms/";
    private static final String MENUS_PATH = "/edu/wpi/teamB/views/menus/";

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
    private JFXButton btnBack;

    @FXML
    private void handleButtonAction(ActionEvent e) {
        Button btn = (Button) e.getSource();
        String path;
        switch (btn.getId()) {
            case "btnMedicine":
                path = VIEWS_PATH + "medDeliveryRequestForm.fxml";
                break;
            case "btnSanitation":
                path = VIEWS_PATH + "sanitationRequestForm.fxml";
                break;
            case "btnIntTransp":
                path = VIEWS_PATH + "internalTransportationRequestForm.fxml";
                break;
            case "btnSecurity":
                path = VIEWS_PATH + "securityRequestForm.fxml";
                break;
            case "btnFloralDelivery":
                path = VIEWS_PATH + "floralDeliveryRequestForm.fxml";
                break;
            case "btnExtTransp":
                path = VIEWS_PATH + "externalTransportationRequestForm.fxml";
                break;
            case "btnReligiousRequest":
                path = VIEWS_PATH + "religiousRequestForm.fxml";
                break;
            case "btnFoodDelivery":
                path = VIEWS_PATH + "foodDeliveryRequestForm.fxml";
                break;
            case "btnLaundryRequest":
                path = VIEWS_PATH + "laundryRequestForm.fxml";
                break;
            case "btnBack":
                path = MENUS_PATH + "patientDirectoryMenu.fxml";
                break;
            default:
                throw new IllegalStateException("WHAT BUTTON IS THIS AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }

        try {
            SceneSwitcher.switchScene(getClass(), path);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}

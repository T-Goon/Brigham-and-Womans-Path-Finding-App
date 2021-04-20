package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceRequestMenuController implements Initializable {

    private static final String VIEWS_PATH = "/edu/wpi/teamB/views/requestForms/";

    @FXML
    private FlowPane flowpane;

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
    private JFXButton btnCaseManager;

    @FXML
    private JFXButton btnSocialWorker;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnExit;

    @FXML
    private VBox medicineDelivery;

    @FXML
    private VBox internalTransport;

    @FXML
    private VBox externalTransport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!DatabaseHandler.getDatabaseHandler("main.db").getAuthenticationUser().isAtLeast(User.AuthenticationLevel.STAFF)) {
            flowpane.getChildren().remove(medicineDelivery);
            flowpane.getChildren().remove(internalTransport);
            flowpane.getChildren().remove(externalTransport);
        }
    }

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
            case "btnCaseManager":
                path = VIEWS_PATH + "caseManagerRequestForm.fxml";
                break;
            case "btnSocialWorker":
                path = VIEWS_PATH + "socialWorkerRequestForm.fxml";
                break;
            case "btnEmergency":
                path = VIEWS_PATH + "emergencyForm.fxml";
                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                return;
            case "btnExit":
                Platform.exit();
                return;
            default:
                throw new IllegalStateException("WHAT BUTTON IS THIS AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }

        SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml", path);
    }
}

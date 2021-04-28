package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ServiceRequestMenuCreditsController extends BasePageController {

    private static final String VIEWS_PATH = "/edu/wpi/cs3733/D21/teamB/views/requestForms/";

    @FXML
    public JFXButton btnShowCredits;

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
    private JFXButton btnCaseManager;

    @FXML
    private JFXButton btnSocialWorker;

    @FXML
    private JFXButton btnGiftDelivery;

    @FXML
    private VBox medicineDelivery;

    @FXML
    private VBox internalTransport;

    @FXML
    private VBox externalTransport;


    @FXML
    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);
        Button btn = (Button) e.getSource();

        String path = null;
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
            case "btnGiftDelivery":
                path = VIEWS_PATH + "giftRequestForm.fxml";
                break;
            case "btnLanguageInterpreter":
                path = VIEWS_PATH + "languageRequestForm.fxml";
                break;
            case "btnShowCredits":
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                return;
        }

        if (path != null)
            SceneSwitcher.switchFromTemp(getClass(), path);
    }
}

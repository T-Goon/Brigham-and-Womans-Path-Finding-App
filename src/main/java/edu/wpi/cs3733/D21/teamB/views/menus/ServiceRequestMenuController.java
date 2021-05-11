package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceRequestMenuController extends BasePageController {

    private static final String VIEWS_PATH = "/edu/wpi/cs3733/D21/teamB/views/requestForms/";

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
    private JFXButton btnGiftDelivery;

    @FXML
    private JFXButton btnSocialWorker;

    @FXML
    private JFXButton btnShowCredits;

    @FXML
    private JFXButton btnLanguageInterpreter;

    @FXML
    private Label lblSocialWorker;

    @FXML
    private Label lblLaundry;

    @FXML
    private Label lblFoodDev;

    @FXML
    private Label lblSan;

    @FXML
    private Label lblFloral;

    @FXML
    private Label lblMed;

    @FXML
    private Label lblSecurity;

    @FXML
    private Label lblIntTran;

    @FXML
    private Label lblExtTrans;

    @FXML
    private Label lblRegReq;

    @FXML
    private Label lblCase;

    @FXML
    private Label lblGift;

    @FXML
    private Label lblLang;

    private boolean show = true;


    public void hide(){
        lblSocialWorker.setVisible(false);
        lblLaundry.setVisible(false);
        lblFoodDev.setVisible(false);
        lblSan.setVisible(false);
        lblFloral.setVisible(false);
        lblMed.setVisible(false);
        lblSecurity.setVisible(false);
        lblIntTran.setVisible(false);
        lblExtTrans.setVisible(false);
        lblRegReq.setVisible(false);
        lblCase.setVisible(false);
        lblGift.setVisible(false);
        lblLang.setVisible(false);
    }

    public void show(){
        lblSocialWorker.setVisible(true);
        lblLaundry.setVisible(true);
        lblFoodDev.setVisible(true);
        lblSan.setVisible(true);
        lblFloral.setVisible(true);
        lblMed.setVisible(true);
        lblSecurity.setVisible(true);
        lblIntTran.setVisible(true);
        lblExtTrans.setVisible(true);
        lblRegReq.setVisible(true);
        lblCase.setVisible(true);
        lblGift.setVisible(true);
        lblLang.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        hide();
    }

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
            case "btnHome":
                path = VIEWS_PATH + "userDirectoryMenu.fxml";
                break;
            case "btnGiftDelivery":
                path = VIEWS_PATH + "giftRequestForm.fxml";
                break;
            case "btnLanguageInterpreter":
                path = VIEWS_PATH + "languageRequestForm.fxml";
                break;
            case "btnShowCredits":
                if(show){
                    show();
                    show = false;
                }
                else{
                    hide();
                    show = true;
                }

                //path = "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestMenuCredits.fxml";
                break;
        }

        if (path != null)
            SceneSwitcher.switchScene("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestMenu.fxml", path);
    }
}

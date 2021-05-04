package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController extends BasePageController {

    @FXML
    public JFXButton btnLogin;

    @FXML
    public JFXButton btnRegister;

    @FXML
    private JFXButton btnCovid;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnSettings;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnLogin":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                break;
            case "btnRegister":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml");
                break;
            case "btnDirections":
                switch (DatabaseHandler.getHandler().getAuthenticationUser().getCovidStatus()) {
                    case UNCHECKED:
                        SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                    case PENDING:
                        SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormPending.fxml");
                        //To insert back target
                        SceneSwitcher.pushPath("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                    default:
                        SceneSwitcher.switchScene(getClass(),currentPath,"/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                        break;
                }
                break;
            case "btnGoogle":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml");
                break;
            case "btnCovid":
                switch (DatabaseHandler.getHandler().getAuthenticationUser().getCovidStatus()) {
                    case UNCHECKED:
                        SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                    case PENDING:
                        SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormPending.fxml");
                        //To insert back target
                        SceneSwitcher.pushPath("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                    default:
                        SceneSwitcher.switchScene(getClass(),currentPath,"/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormAccepted.fxml");
                        //To insert back target
                        SceneSwitcher.pushPath("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                }
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnSettings":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml");
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }
}

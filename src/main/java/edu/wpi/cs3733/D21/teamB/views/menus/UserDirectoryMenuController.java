package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class UserDirectoryMenuController extends BasePageController implements Initializable {

    @FXML
    private VBox buttonPane;

    @FXML
    private Text directoryText;

    @FXML
    private VBox covidHolder;

    @FXML
    private JFXButton btnCovid;

    @FXML
    private VBox directionsHolder;

    @FXML
    private JFXButton btnDirections;

    @FXML
    private VBox googleMapsHolder;

    @FXML
    private JFXButton btnGoogle;

    @FXML
    private VBox serviceRequestHolder;

    @FXML
    private JFXButton btnServiceRequests;

    @FXML
    private VBox requestDBHolder;

    @FXML
    private JFXButton btnDatabase;

    @FXML
    private VBox userDBHolder;

    @FXML
    private JFXButton btnUsers;

    @FXML
    private VBox settingsHolder;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnEmergency;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (DatabaseHandler.getHandler().getAuthenticationUser().isAtLeast(User.AuthenticationLevel.ADMIN)) {
            directoryText.setText("Admin Directory");
            ((HBox) buttonPane.getChildren().get(0)).getChildren().remove(covidHolder);
            ((HBox) buttonPane.getChildren().get(1)).getChildren().remove(serviceRequestHolder);
            ((HBox) buttonPane.getChildren().get(0)).getChildren().add(serviceRequestHolder);
        } else if (DatabaseHandler.getHandler().getAuthenticationUser().isAtLeast(User.AuthenticationLevel.STAFF)) {
            directoryText.setText("Staff Directory");
            ((HBox) buttonPane.getChildren().get(0)).getChildren().remove(covidHolder);
            ((HBox) buttonPane.getChildren().get(1)).getChildren().remove(userDBHolder);
        } else {
            directoryText.setText("Patient Directory");
            ((HBox) buttonPane.getChildren().get(1)).getChildren().remove(serviceRequestHolder);
            ((HBox) buttonPane.getChildren().get(1)).getChildren().remove(requestDBHolder);
            ((HBox) buttonPane.getChildren().get(1)).getChildren().remove(userDBHolder);
            ((HBox) buttonPane.getChildren().get(1)).getChildren().remove(settingsHolder);
            ((HBox) buttonPane.getChildren().get(0)).getChildren().add(settingsHolder);
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnCovid":
                switch (DatabaseHandler.getHandler().getAuthenticationUser().getCovidStatus()) {
                    case UNCHECKED:
                        SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                    case PENDING:
                        SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormPending.fxml");
                        //Insert intermediate back target
                        SceneSwitcher.pushPath("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                    default:
                        SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormAccepted.fxml");
                        //Insert intermediate back target
                        SceneSwitcher.pushPath("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                        break;
                }
                break;
            case "btnDirections":
                if (DatabaseHandler.getHandler().getAuthenticationUser().getAuthenticationLevel().equals(User.AuthenticationLevel.PATIENT)){
                    switch (DatabaseHandler.getHandler().getAuthenticationUser().getCovidStatus()) {
                        case UNCHECKED:
                            SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                            break;
                        case PENDING:
                            SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormPending.fxml");
                            //Insert intermediate back target
                            SceneSwitcher.pushPath("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                            break;
                        default:
                            SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                            break;
                    }
                }else {
                    SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                }
                break;
            case "btnGoogle":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml");
                break;
            case "btnServiceRequests":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestMenu.fxml");
                break;
            case "btnDatabase":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml");
                break;
            case "btnUsers":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/userInformationDatabase.fxml");
                break;
            case "btnSettings":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnBack":
                DatabaseHandler.getHandler().deauthenticate();
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                break;
        }
    }
}

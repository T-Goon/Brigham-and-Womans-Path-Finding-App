package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.util.SceneSwitcher;
import edu.wpi.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class UserDirectoryMenuController extends BasePageController implements Initializable {

    @FXML
    private HBox buttonPane;

    @FXML
    private Text directoryText;

    @FXML
    private JFXButton btnCovid;

    @FXML
    private JFXButton btnDatabase;

    @FXML
    private JFXButton btnServiceRequests;

    @FXML
    private JFXButton btnEmergency;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (DatabaseHandler.getDatabaseHandler("main.db").getAuthenticationUser().isAtLeast(User.AuthenticationLevel.STAFF)) {
            directoryText.setText("Staff Directory");
            buttonPane.getChildren().remove(0);
        } else {
            directoryText.setText("Patient Directory");
            buttonPane.getChildren().remove(2, 4);
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/teamB/views/menus/userDirectoryMenu.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnCovid":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/covidSurvey/covidSurvey.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnServiceRequests":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml");
                break;
            case "btnDatabase":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/menus/serviceRequestDatabase.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

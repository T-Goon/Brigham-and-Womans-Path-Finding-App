package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class CovidFormAcceptedController extends BasePageController implements Initializable {

    @FXML
    private JFXButton btnDirections;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private Text statusReadout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(DatabaseHandler.getHandler().getAuthenticationUser().getCovidStatus().equals(User.CovidStatus.SAFE)){
            statusReadout.setText("Your survey does not indicate any special requirements for entry at this time");
        }else{
            statusReadout.setText("Our staff requests that you enter through the emergency entrance. Directions will automatically reflect this.");
        }

    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormAccepted.fxml";
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(2);
                return;
            case "btnDirections":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
        super.handleButtonAction(e);
    }

}
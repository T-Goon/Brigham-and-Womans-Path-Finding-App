package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class CovidFormPendingController extends BasePageController {

    @FXML
    private JFXButton btnDirections;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btnHome;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormPending.fxml";
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(2);
                return;
            case "btnEdit":
                SceneSwitcher.goBack(1);
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnHome":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                break;
        }
        super.handleButtonAction(e);
    }
}
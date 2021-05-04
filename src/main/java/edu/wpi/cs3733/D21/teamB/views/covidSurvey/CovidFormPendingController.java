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
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormPending.fxml";
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(this.getClass(), 2);
                return;
            case "btnEdit":
                SceneSwitcher.goBack(this.getClass(),1);
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
        super.handleButtonAction(e);
    }
}
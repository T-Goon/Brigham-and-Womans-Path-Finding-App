package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class CovidFormPendingController extends BasePageController {

    @FXML
    private JFXButton btnDirections;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private StackPane stackPane;

    @FXML JFXButton btnGame1;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                return;
            case "btnEdit":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                break;
            case "btnGame1":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/misc/arcade.fxml");
                break;
        }
        super.handleButtonAction(e);
    }
}
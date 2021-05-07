package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;

public class CovidSurveyNurseCheckController extends BasePageController {


    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton button = (JFXButton) actionEvent.getSource();

        String currentPath = "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurveyNurseCheck.fxml";
        super.handleButtonAction(actionEvent);
        switch (button.getId()){
            case "btnDeny":
                break;
            case "btnAccept":
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;

        }
    }
}

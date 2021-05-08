package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.requests.CovidSurveyRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.entities.requests.SanitationRequest;
import edu.wpi.cs3733.D21.teamB.util.AutoCompleteComboBoxListener;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import org.hsqldb.Database;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CovidSurveyNurseCheckController extends BasePageController implements Initializable {

    public String id;
    public CovidSurveyRequest covidSurveyRequest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        //If you're editing
        if (SceneSwitcher.peekLastScene().equals("/edu/wpi/cs3733/D21/teamB/views/menus/serviceRequestDatabase.fxml")) {
            this.id = (String) App.getPrimaryStage().getUserData();
            try {
                covidSurveyRequest = (CovidSurveyRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.COVID);
            } catch (SQLException e) {
                covidSurveyRequest = null;
                e.printStackTrace();
                return;
            }
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton button = (JFXButton) actionEvent.getSource();

        String currentPath = "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurveyNurseCheck.fxml";
        super.handleButtonAction(actionEvent);
        switch (button.getId()){
            case "btnDeny":
                try {
                    DatabaseHandler.getHandler().updateCovidRequestAdmitted(covidSurveyRequest, "F");
                } catch (SQLException error) {
                    System.err.println("You fucking donkey");
                }
                SceneSwitcher.goBack(1);
                break;
            case "btnAccept":
                try {
                    DatabaseHandler.getHandler().updateCovidRequestAdmitted(covidSurveyRequest, "T");
                } catch (SQLException throwables) {
                    System.err.println("You fucking donkey");
                }
                SceneSwitcher.goBack(1);
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;

        }
    }
}

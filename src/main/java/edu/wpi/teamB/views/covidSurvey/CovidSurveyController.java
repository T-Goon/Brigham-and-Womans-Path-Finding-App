package edu.wpi.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CovidSurveyController implements Initializable {
    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXRadioButton btnCCYes;

    @FXML
    private JFXRadioButton btnCCNo;

    @FXML
    private JFXRadioButton btnTestYes;

    @FXML
    private JFXRadioButton btnTestNo;

    @FXML
    private ToggleGroup ccGroup;

    @FXML
    private ToggleGroup testGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCCYes.setToggleGroup(ccGroup);
        btnCCNo.setToggleGroup(ccGroup);
        btnTestYes.setToggleGroup(testGroup);
        btnTestNo.setToggleGroup(testGroup);
    }

    @FXML
    void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
            case "btnSubmit":
                //add in the no symptoms once that part gets set up
                Parent root;
                if(btnCCNo.isPressed() && btnTestNo.isPressed()){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamB/views/covidSurvey/covidFormSubmittedWithSymp.fxml"));
                    root = (Parent) loader.load();
                    CovidPopupWithSympController controller = (CovidPopupWithSympController) loader.getController();
                }
                else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamB/views/covidSurvey/covidFormSubmittedNoSymp.fxml"));
                    root = (Parent) loader.load();
                    CovidPopupNoSympController controller = (CovidPopupNoSympController) loader.getController();
                }
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Response Submitted");
                stage.show();

                //SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/formSubmitted.fxml");
                break;
            case "btnEmergency":
                // Not implemented
                break;
        }
    }
}

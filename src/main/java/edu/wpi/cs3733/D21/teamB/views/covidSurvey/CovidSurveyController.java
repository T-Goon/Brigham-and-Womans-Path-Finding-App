package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CovidSurveyController extends BasePageController implements Initializable {

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXCheckBox chkFever;

    @FXML
    private JFXCheckBox chkChills;

    @FXML
    private JFXCheckBox chkCough;

    @FXML
    private JFXCheckBox chkShortBreath;

    @FXML
    private JFXCheckBox chkSoreTht;

    @FXML
    private JFXCheckBox chkHeadache;

    @FXML
    private JFXCheckBox chkAches;

    @FXML
    private JFXCheckBox chkNose;

    @FXML
    private JFXCheckBox chkLostTaste;

    @FXML
    private JFXCheckBox chkNausea;

    @FXML
    private JFXButton btnExit;

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

    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCCYes.setToggleGroup(ccGroup);
        btnCCNo.setToggleGroup(ccGroup);
        btnTestYes.setToggleGroup(testGroup);
        btnTestNo.setToggleGroup(testGroup);
        btnSubmit.setDisable(true);
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnSubmit":
                if (btnCCNo.isSelected() && btnTestNo.isSelected() && !chkCough.isSelected() && !chkChills.isSelected() && !chkAches.isSelected() && !chkFever.isSelected()
                        && !chkHeadache.isSelected() && !chkLostTaste.isSelected() && !chkNausea.isSelected() && !chkNose.isSelected() && !chkShortBreath.isSelected() && !chkSoreTht.isSelected())
                    SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormSubmittedNoSymp.fxml");
                else
                    SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormSubmittedWithSymp.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                !(btnCCYes.isSelected() || btnCCNo.isSelected()) || !(btnTestYes.isSelected() || btnTestNo.isSelected())
        );
    }
}
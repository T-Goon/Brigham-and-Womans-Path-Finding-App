package edu.wpi.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CovidSurveyController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCCYes.setToggleGroup(ccGroup);
        btnCCNo.setToggleGroup(ccGroup);
        btnTestYes.setToggleGroup(testGroup);
        btnTestNo.setToggleGroup(testGroup);
    }

    @FXML
    void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnSubmit":
                if (btnCCNo.isSelected() && btnTestNo.isSelected() && !chkCough.isSelected() && !chkChills.isSelected() && !chkAches.isSelected() && !chkFever.isSelected()
                        && !chkHeadache.isSelected() && !chkLostTaste.isSelected() && !chkNausea.isSelected() && !chkNose.isSelected() && !chkShortBreath.isSelected() && !chkSoreTht.isSelected())
                    SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/covidSurvey/covidFormSubmittedNoSymp.fxml");
                else
                    SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/CovidSurvey/covidFormSubmittedWithSymp.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/covidSurvey/covidSurvey.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}
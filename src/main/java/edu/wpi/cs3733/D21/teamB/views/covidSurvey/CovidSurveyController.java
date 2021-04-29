package edu.wpi.cs3733.D21.teamB.views.covidSurvey;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.CovidSurveyRequest;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

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
                this.handleSubmission();
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    private void handleSubmission(){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateInfo = new Date();
        CovidSurveyRequest request = new CovidSurveyRequest(UUID.randomUUID().toString(),
                timeFormat.format(dateInfo),
                dateFormat.format(dateInfo),
                "F",
                null,
                null,
                "",
                DatabaseHandler.getHandler().getAuthenticationUser().getUsername(),
                User.CovidStatus.PENDING,
                chkFever.isSelected() ? "T" : "F",
                chkCough.isSelected() ? "T" : "F",
                chkShortBreath.isSelected() ? "T" : "F",
                chkSoreTht.isSelected() ? "T" : "F",
                chkHeadache.isSelected() ? "T" : "F",
                chkAches.isSelected() ? "T" : "F",
                chkNose.isSelected() ? "T" : "F",
                chkLostTaste.isSelected() ? "T" : "F",
                chkNausea.isSelected() ? "T" : "F",
                btnCCYes.isSelected() ? "T" : "F",
                btnTestYes.isSelected() ? "T" : "F"
                );

        try {
            DatabaseHandler.getDatabaseHandler("main.db").addRequest(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (btnCCNo.isSelected() && btnTestNo.isSelected() && !chkCough.isSelected() && !chkChills.isSelected() && !chkAches.isSelected() && !chkFever.isSelected()
                && !chkHeadache.isSelected() && !chkLostTaste.isSelected() && !chkNausea.isSelected() && !chkNose.isSelected() && !chkShortBreath.isSelected() && !chkSoreTht.isSelected())
            SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormSubmittedNoSymp.fxml");
        else
            SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidFormSubmittedWithSymp.fxml");

    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                !(btnCCYes.isSelected() || btnCCNo.isSelected()) || !(btnTestYes.isSelected() || btnTestNo.isSelected())
        );
    }
}
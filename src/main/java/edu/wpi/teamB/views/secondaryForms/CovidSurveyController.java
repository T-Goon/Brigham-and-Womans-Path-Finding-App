package edu.wpi.teamB.views.secondaryForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CovidSurveyController implements Initializable {
    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXRadioButton ccYesBtn;

    @FXML
    private JFXRadioButton ccNoBtn;

    @FXML
    private JFXRadioButton testYesBtn;

    @FXML
    private JFXRadioButton testNoBtn;

    @FXML
    private ToggleGroup ccGroup;

    @FXML
    private ToggleGroup testGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ccYesBtn.setToggleGroup(ccGroup);
        ccNoBtn.setToggleGroup(ccGroup);
        testYesBtn.setToggleGroup(testGroup);
        testNoBtn.setToggleGroup(testGroup);

    }

    @FXML
    void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        if (btn.getId().equals("btnBack")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}
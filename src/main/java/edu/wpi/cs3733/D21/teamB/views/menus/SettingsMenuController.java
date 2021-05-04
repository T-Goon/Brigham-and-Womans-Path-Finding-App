package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SettingsMenuController extends BasePageController implements Initializable {
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private StackPane stackContainer;

    @FXML
    private JFXToggleButton toggleTTS;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        toggleTTS.setSelected(ttsOn);
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        super.handleButtonAction(e);
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(),"/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnAdd":
                Stage stage = App.getPrimaryStage();
                stage.setUserData(new User("", "", "", User.AuthenticationLevel.PATIENT, new ArrayList<>()));
                SceneSwitcher.addingUser = true;
                SceneSwitcher.switchScene(getClass(),"/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/menus/editUserMenu.fxml");
                break;
            case "btnHelp":
                loadHelpDialog();
                break;
        }
    }

    @FXML
    public void handleToggleAction(ActionEvent e) {
        JFXToggleButton toggleButton = (JFXToggleButton) e.getSource();
        switch (toggleButton.getId()) {
            case "toggleTTS":
                if (ttsOn) {
                    ttsOn = false;
                } else {
                    ttsOn = true;
                    tts.speak("Text-to-speech has been activated.", 1.0f, false, false);
                }
                break;
        }
    }

    private void loadHelpDialog(){
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText = new Text(">>>Settings help dialogue goes here<<<");
        helpText.setFont(new Font("MS Reference Sans Serif", 14));

        Label headerLabel = new Label("Help");
        headerLabel.setFont(new Font("MS Reference Sans Serif", 18));

        helpLayout.setHeading(headerLabel);
        helpLayout.setBody(helpText);
        JFXDialog helpWindow = new JFXDialog(stackContainer, helpLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> helpWindow.close());
        helpLayout.setActions(button);

        helpWindow.show();

    }
}

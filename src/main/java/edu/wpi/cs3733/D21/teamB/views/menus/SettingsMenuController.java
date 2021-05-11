package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.OnScreenKeyboard;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.HelpDialog;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsMenuController extends BasePageController implements Initializable {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXToggleButton toggleTTS;

    @FXML
    private JFXToggleButton toggleOSK;

    @FXML
    private JFXToggleButton toggleRemoteDatabase;

    @FXML
    private HBox remoteDBHolder;

    @FXML
    private HBox profileHolder;

    @FXML
    private VBox settings;

    @FXML
    private JFXButton btnEditProfile;

    @FXML
    private HBox passwordHolder;

    @FXML
    private JFXButton btnChangePassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        toggleOSK.setSelected(oskOn);
        toggleTTS.setSelected(DatabaseHandler.getHandler().getAuthenticationUser().getTtsEnabled().equals("T"));
        toggleRemoteDatabase.setSelected(DatabaseHandler.getRemote());
        if (!DatabaseHandler.getHandler().getAuthenticationUser().isAtLeast(User.AuthenticationLevel.PATIENT)) {
            profileHolder.getChildren().remove(btnEditProfile);
            passwordHolder.getChildren().remove(btnChangePassword);
        }
        if (!DatabaseHandler.getHandler().getAuthenticationUser().getAuthenticationLevel().equals(User.AuthenticationLevel.ADMIN)) {
            settings.getChildren().remove(remoteDBHolder);
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        String currentPath = "/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml";
        JFXButton btn = (JFXButton) e.getSource();
        super.handleButtonAction(e);
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene("/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnEditProfile":
                Stage stage = App.getPrimaryStage();
                stage.setUserData(DatabaseHandler.getHandler().getAuthenticationUser());
                SceneSwitcher.editingUserState = SceneSwitcher.UserState.EDIT_SELF;
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/editUserMenu.fxml");
                break;
            case "btnChangePassword":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/changePassword.fxml");
                break;
            case "btnHelp":
                HelpDialog.loadHelpDialog(stackPane, "Click the toggle button to enable or disable text-to-speech functionality.\\n You may also click on 'Edit Profile' to edit your profile or 'Change Password' to change your password.");
                break;
            case "btnCredits":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/misc/creditsPage.fxml");
                break;
        }
    }

    @FXML
    public void handleToggleAction(ActionEvent e) {
        JFXToggleButton toggleButton = (JFXToggleButton) e.getSource();
        switch (toggleButton.getId()) {
            case "toggleTTS":
                DatabaseHandler db = DatabaseHandler.getHandler();
                User user = db.getAuthenticationUser();
                if (toggleButton.isSelected())
                    tts.speak("Text-to-speech has been activated.", 1.0f, false, false);
                user.setTtsEnabled(user.getTtsEnabled().equals("F") ? "T" : "F");
                try {
                    db.updateUser(user);
                } catch (SQLException err) {
                    err.printStackTrace();
                }
                break;
            case "toggleOSK":
                if (oskOn) {
                    oskOn = false;
                    OnScreenKeyboard.getInstance().getKeyboard().setVisible(false);
                } else {
                    oskOn = true;
                    OnScreenKeyboard.getInstance().getKeyboard().setVisible(true);
                }
                break;
            case "toggleRemoteDatabase":
                DatabaseHandler.getHandler().changeRemoteStatus(!DatabaseHandler.getRemote());
//                DatabaseHandler.getHandler().getConnection();
                System.out.println(DatabaseHandler.getRemote());
                break;
        }
    }
}

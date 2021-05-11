package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.HelpDialog;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditUserController extends BasePageController implements Initializable {

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private Label lblError;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXComboBox<Label> comboAuth;

    @FXML
    private JFXPasswordField passPassword;

    @FXML
    private Text jobText;

    @FXML
    private JFXComboBox<Label> comboJob;

    @FXML
    private JFXToggleButton toggleTTS;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private StackPane stackPane;

    @FXML
    private Text bigText;

    @FXML
    private VBox vAdminFields;

    @FXML
    private Text smallText;

    private String originalEmail;

    private final Pattern emailPattern = Pattern.compile(".+@.+");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        comboAuth.getItems().add(new Label(User.AuthenticationLevel.ADMIN.toString()));
        comboAuth.getItems().add(new Label(User.AuthenticationLevel.STAFF.toString()));
        comboAuth.getItems().add(new Label(User.AuthenticationLevel.PATIENT.toString()));

        for (Request.RequestType r : Request.RequestType.values()) {
            comboJob.getItems().add(new Label(Request.RequestType.prettify(r)));
        }
        comboJob.getItems().add(0, new Label("None"));

        User u = (User) App.getPrimaryStage().getUserData();
        if (u.getUsername().equals("")) {
            bigText.setText("Add User Form");
            smallText.setText("Add User");
        }
        txtUsername.setText(u.getUsername());
        txtEmail.setText(u.getEmail());
        originalEmail = txtEmail.getText();
        txtFirstName.setText(u.getFirstName());
        txtLastName.setText(u.getLastName());
        toggleTTS.setSelected("T".equals(u.getTtsEnabled()));
        int i = 0;
        if (!u.getJobs().isEmpty()) {
            for (Label label : comboJob.getItems()) {
                if (label.getText().equals(Request.RequestType.prettify(u.getJobs().get(0)))) {
                    comboJob.getSelectionModel().select(i);
                }
                i++;
            }
        } else {
            for (Label label : comboJob.getItems()) {
                if (label.getText().equals("None")) {
                    comboJob.getSelectionModel().select(i);
                }
                i++;
            }
        }
        i = 0;
        for (Label label : comboAuth.getItems()) {
            if (label.getText().equals(u.getAuthenticationLevel().toString())) {
                comboAuth.getSelectionModel().select(i);
            }
            i++;
        }

        validateButtons();

        // TODO: Make it look less bad
        if (SceneSwitcher.editingUserState == SceneSwitcher.UserState.EDIT_SELF) {
            bigText.setText("User Profile");
            smallText.setText("Edit User Profile");
            vAdminFields.setVisible(false);
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        switch (btn.getId()) {
            case "btnSubmit":
                if (!validateEmail()) return;

                String uUsername = txtUsername.getText();
                String uEmail = txtEmail.getText();
                String uFirstName = txtFirstName.getText();
                String uLastName = txtLastName.getText();
                User.AuthenticationLevel uAuthLevel = User.AuthenticationLevel.valueOf(comboAuth.getValue().getText());
                String tts = toggleTTS.isSelected() ? "T" : "F";
                List<Request.RequestType> uJobs = new ArrayList<>();
                if (comboJob.getValue() != null) {
                    if (comboJob.getValue().getText().equals("None")) {
                        uJobs = new ArrayList<>();
                    } else {
                        Request.RequestType uJob = Request.RequestType.uglify(comboJob.getValue().getText());
                        uJobs.add(uJob);
                    }
                }
                User uUpdated = new User(uUsername, uEmail, uFirstName, uLastName, uAuthLevel, tts, uJobs);
                try {
                    if (SceneSwitcher.editingUserState == SceneSwitcher.UserState.ADD) {
                        DatabaseHandler.getHandler().addUser(uUpdated, passPassword.getText());
                    } else {
                        DatabaseHandler.getHandler().updateUser(uUpdated);
                    }
                } catch (SQLException e) {
                    throw new IllegalStateException("Username not found when updating; This should never happen");
                }
                SceneSwitcher.goBack(1);
                break;
            case "btnCancel":
                SceneSwitcher.goBack(1);
                break;
            case "btnHelp":
                HelpDialog.loadHelpDialog(stackPane, "Please fill out this form completely. Once each field is full, you can submit the form and the user will be added.\n" +
                        "If you wish to end your request early, click the back button.");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene("/edu/wpi/cs3733/D21/teamB/views/menus/editUserMenu.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnHome":
                SceneSwitcher.switchScene("/edu/wpi/cs3733/D21/teamB/views/menus/editUserMenu.fxml","/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml");
                break;
        }
    }

    @FXML
    private void validateButtons() {
        btnSubmit.setDisable(
                txtUsername.getText().isEmpty() || txtEmail.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
                        comboAuth.getValue() == null || (passPassword.getText().isEmpty() && SceneSwitcher.editingUserState.equals(SceneSwitcher.UserState.ADD))
        );

        txtUsername.setDisable(SceneSwitcher.editingUserState != SceneSwitcher.UserState.ADD);
        passPassword.setDisable(SceneSwitcher.editingUserState != SceneSwitcher.UserState.ADD);
    }

    @FXML
    private boolean validateEmail() {
        Matcher matcher = emailPattern.matcher(txtEmail.getText());
        if (!txtUsername.getText().equals("temporary") && !originalEmail.equals(txtEmail.getText()) && DatabaseHandler.getHandler().getUserByEmail(txtEmail.getText()) != null) {
            lblError.setText("Email address is already taken!");
            lblError.setVisible(true);
            btnSubmit.setDisable(true);
            return false;
        } else if (!txtUsername.getText().equals("temporary") && !matcher.matches()) {
            lblError.setText("Email address must be valid!");
            lblError.setVisible(true);
            btnSubmit.setDisable(true);
            return false;
        } else {
            lblError.setVisible(false);
            return true;
        }
    }
}

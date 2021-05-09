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
    private JFXTextField username;

    @FXML
    private JFXTextField email;

    @FXML
    private Label lblError;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private Text passwordText;

    @FXML
    private JFXPasswordField password;

    @FXML
    private Text authenticationLevelText;

    @FXML
    private JFXComboBox<Label> authenticationLevel;

    @FXML
    private Text jobText;

    @FXML
    private JFXComboBox<Label> job;

    @FXML
    private JFXToggleButton ttsEnabled;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private StackPane stackPane;

    @FXML
    private Text bigText;

    @FXML
    private Text smallText;

    private String originalEmail;

    private final Pattern emailPattern = Pattern.compile(".+@.+");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        authenticationLevel.getItems().add(new Label(User.AuthenticationLevel.ADMIN.toString()));
        authenticationLevel.getItems().add(new Label(User.AuthenticationLevel.STAFF.toString()));
        authenticationLevel.getItems().add(new Label(User.AuthenticationLevel.PATIENT.toString()));

        for (Request.RequestType r : Request.RequestType.values()) {
            job.getItems().add(new Label(Request.RequestType.prettify(r)));
        }
        job.getItems().add(0, new Label("None"));

        User u = (User) App.getPrimaryStage().getUserData();
        if (u.getUsername().equals("")) {
            bigText.setText("Add User Form");
            smallText.setText("Add User");
        }
        username.setText(u.getUsername());
        email.setText(u.getEmail());
        originalEmail = email.getText();
        firstName.setText(u.getFirstName());
        lastName.setText(u.getLastName());
        ttsEnabled.setSelected("T".equals(u.getTtsEnabled()));
        int i = 0;
        if (!u.getJobs().isEmpty()) {
            for (Label label : job.getItems()) {
                if (label.getText().equals(Request.RequestType.prettify(u.getJobs().get(0)))) {
                    job.getSelectionModel().select(i);
                }
                i++;
            }
        } else {
            for (Label label : job.getItems()) {
                if (label.getText().equals("None")) {
                    job.getSelectionModel().select(i);
                }
                i++;
            }
        }
        i = 0;
        for (Label label : authenticationLevel.getItems()) {
            if (label.getText().equals(u.getAuthenticationLevel().toString())) {
                authenticationLevel.getSelectionModel().select(i);
            }
            i++;
        }

        validateButtons();

        // TODO: Make it look less bad
        if (SceneSwitcher.editingUserState == SceneSwitcher.UserState.EDIT_SELF) {
            bigText.setText("User Profile");
            smallText.setText("Edit User Profile");
            passwordText.setVisible(false);
            password.setVisible(false);
            authenticationLevelText.setVisible(false);
            authenticationLevel.setVisible(false);
            jobText.setVisible(false);
            job.setVisible(false);
            ttsEnabled.setVisible(false);
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        switch (btn.getId()) {
            case "btnSubmit":
                if (!validateEmail()) return;

                String uUsername = username.getText();
                String uEmail = email.getText();
                String uFirstName = firstName.getText();
                String uLastName = lastName.getText();
                User.AuthenticationLevel uAuthLevel = User.AuthenticationLevel.valueOf(authenticationLevel.getValue().getText());
                String tts = ttsEnabled.isSelected() ? "T" : "F";
                List<Request.RequestType> uJobs = new ArrayList<>();
                if (job.getValue() != null) {
                    if (job.getValue().getText().equals("None")) {
                        uJobs = new ArrayList<>();
                    } else {
                        Request.RequestType uJob = Request.RequestType.uglify(job.getValue().getText());
                        uJobs.add(uJob);
                    }
                }
                User uUpdated = new User(uUsername, uEmail, uFirstName, uLastName, uAuthLevel, tts, uJobs);
                try {
                    if (SceneSwitcher.editingUserState == SceneSwitcher.UserState.ADD) {
                        DatabaseHandler.getHandler().addUser(uUpdated, password.getText());
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
        }
    }

    @FXML
    private void validateButtons() {
        btnSubmit.setDisable(
                username.getText().isEmpty() || email.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty() ||
                        authenticationLevel.getValue() == null
        );

        username.setDisable(SceneSwitcher.editingUserState != SceneSwitcher.UserState.ADD);
        password.setDisable(SceneSwitcher.editingUserState != SceneSwitcher.UserState.ADD);
    }

    @FXML
    private boolean validateEmail() {
        Matcher matcher = emailPattern.matcher(email.getText());
        if (!username.getText().equals("temporary") && !originalEmail.equals(email.getText()) && DatabaseHandler.getHandler().getUserByEmail(email.getText()) != null) {
            lblError.setText("Email address is already taken!");
            lblError.setVisible(true);
            btnSubmit.setDisable(true);
            return false;
        } else if (!username.getText().equals("temporary") && !matcher.matches()) {
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

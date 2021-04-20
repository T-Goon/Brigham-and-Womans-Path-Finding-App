package edu.wpi.teamB.views.loginPages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private Label error;

    @FXML
    private JFXButton btnExit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Add event listeners to the text boxes so user can submit by pressing enter
        username.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty()) {
                handleLoginSubmit();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty()) {
                handleLoginSubmit();
            }
        });
    }

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnLogin":
                handleLoginSubmit();
                break;
            case "btnBack":
                DatabaseHandler.getDatabaseHandler("main.db").deauthenticate();
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/loginPages/loginPage.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    /**
     * Handles the login submit process
     */
    private void handleLoginSubmit() {
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
        User user = db.authenticate(username.getText(), password.getText());
        if (user == null) {
            error.setText("Username or password does not exist!");
            error.setVisible(true);
            return;
        } else if (!user.isAtLeast(User.AuthenticationLevel.STAFF)) {
            error.setText("This user does not have access to this area!");
            error.setVisible(true);
            return;
        }
        SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/menus/staffDirectoryMenu.fxml");
    }

    @FXML
    private void validateButton() {
        btnLogin.setDisable(areFormsEmpty());
    }

    /**
     * Returns if either of the two text forms are empty
     *
     * @return true if either form is empty, false if they're both filled
     */
    private boolean areFormsEmpty() {
        return username.getText().isEmpty() || password.getText().isEmpty();
    }
}

package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController extends BasePageController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Label error;

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
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnLogin":
                handleLoginSubmit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
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
        SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml");
    }

    /**
     * Disables the submit button if either the username or
     * the password is empty
     */
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

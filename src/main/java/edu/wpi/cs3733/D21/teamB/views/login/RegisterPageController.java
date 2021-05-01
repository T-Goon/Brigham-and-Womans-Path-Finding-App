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
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class
RegisterPageController extends BasePageController implements Initializable {

    @FXML
    public JFXButton btnEmergency;

    @FXML
    public JFXTextField username;

    @FXML
    public JFXTextField firstName;

    @FXML
    public JFXTextField lastName;

    @FXML
    public JFXPasswordField password;

    @FXML
    public JFXPasswordField retypePassword;

    @FXML
    public Label error;

    @FXML
    public JFXButton btnRegister;

    @FXML
    private JFXButton btnLoginPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        firstName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        lastName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        retypePassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml";
        JFXButton btn = (JFXButton) actionEvent.getSource();
        switch (btn.getId()) {
            case "btnRegister":
                handleRegisterSubmit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnLoginPage":
                SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                break;
        }
        super.handleButtonAction(actionEvent);
    }

    private void handleRegisterSubmit() {
        DatabaseHandler db = DatabaseHandler.getHandler();
        if (db.getUserByUsername(username.getText()) != null) {
            error.setText("Username is already taken!");
            error.setVisible(true);
            return;
        }

        // Check to make sure the passwords are the same
        if (!password.getText().equals(retypePassword.getText())) {
            error.setText("Passwords do not match!");
            error.setVisible(true);
            return;
        }

        // Add the user to the database
        User newUser = new User(username.getText(), firstName.getText(), lastName.getText(), User.AuthenticationLevel.PATIENT, new ArrayList<>());
        try {
            db.addUser(newUser, password.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/login/successfulRegistration.fxml");
    }

    public void validateButton(KeyEvent keyEvent) {
        btnRegister.setDisable(areFormsEmpty());
    }

    /**
     * @return whether any of the fields are empty
     */
    private boolean areFormsEmpty() {
        return username.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty() || password.getText().isEmpty() || retypePassword.getText().isEmpty();
    }
}

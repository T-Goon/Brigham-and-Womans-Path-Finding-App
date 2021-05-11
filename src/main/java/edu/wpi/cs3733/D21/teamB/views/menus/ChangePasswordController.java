package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePasswordController extends BasePageController implements Initializable {

    @FXML
    public JFXButton btnEmergency;

    @FXML
    public JFXPasswordField oldPassword;

    @FXML
    public JFXPasswordField newPasswordOne;

    @FXML
    public JFXPasswordField newPasswordTwo;

    @FXML
    public Label error;

    @FXML
    public JFXButton btnHome;

    @FXML
    public JFXButton btnChangePassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        oldPassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty()) {
                handleChangePassword();
            }
        });

        newPasswordOne.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty()) {
                handleChangePassword();
            }
        });

        newPasswordTwo.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty()) {
                handleChangePassword();
            }
        });
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene("/edu/wpi/cs3733/D21/teamB/views/menus/changePassword.fxml", "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnHome":
                SceneSwitcher.switchScene("/edu/wpi/cs3733/D21/teamB/views/menus/changePassword.fxml","/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml");
            case "btnChangePassword":
                handleChangePassword();
                break;
        }
    }

    /**
     * Actually changes the password
     */
    private void handleChangePassword() {
        error.setVisible(false);
        DatabaseHandler db = DatabaseHandler.getHandler();

        // Make sure the old password is correct
        if (db.authenticate(db.getAuthenticationUser().getUsername(), oldPassword.getText()) == null) {
            error.setText("Old password is incorrect!");
            error.setVisible(true);
            return;
        }

        // Make sure the passwords match
        if (!newPasswordOne.getText().equals(newPasswordTwo.getText())) {
            error.setText("Passwords do not match!");
            error.setVisible(true);
            return;
        }

        // Update password
        String hashed = db.passwordHash(newPasswordOne.getText());
        try {
            db.updatePasswordForUser(hashed);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml");
        SceneSwitcher.goBack(1);
    }

    @FXML
    private void validateButton() {
        btnChangePassword.setDisable(areFormsEmpty());
    }

    /**
     * Returns if either of the two text forms are empty
     *
     * @return true if either form is empty, false if they're both filled
     */
    private boolean areFormsEmpty() {
        return oldPassword.getText().isEmpty() || newPasswordOne.getText().isEmpty() || newPasswordTwo.getText().isEmpty();
    }
}

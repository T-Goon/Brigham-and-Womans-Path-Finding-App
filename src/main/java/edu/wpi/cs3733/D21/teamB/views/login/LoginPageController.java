package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import edu.wpi.cs3733.D21.teamB.views.face.Camera;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

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

    @FXML
    private JFXButton btnRegisterPage;

    @FXML
    private ImageView faceImage;

    private Camera camera;

    @FXML
    private StackPane stackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Camera.stopAcquisition();

        super.initialize(location, resources);

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
        Platform.runLater(() -> {
            if (!PageCache.isTextfieldFocused())
                username.requestFocus();
        });

        camera = new Camera(null, faceImage, null, true);
        camera.setLoginPageController(this);
        camera.toggleCamera();
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml";
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnLogin":
                handleLoginSubmit();
                break;
            case "btnEmergency":
            case "btnBack":
                Camera.stopAcquisition();
                break;
            case "btnRegisterPage":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml");
                break;
        }

        super.handleButtonAction(e);
    }

    public void setUserName(String name) {
        username.setText(name);
        Platform.runLater(() -> {
            if (!PageCache.isTextfieldFocused())
                password.requestFocus();
        });
        validateButton();
    }

    @FXML
    private void toggleCamera(ActionEvent event) {
        JFXToggleButton tog = (JFXToggleButton) event.getSource();

        if (!tog.isSelected()) {
            camera.toggleCamera();
            username.setText("");
            password.setText("");
            Platform.runLater(() -> username.requestFocus());
        } else {
            password.setText("");
            camera.toggleCamera();
        }
    }

    /**
     * Handles the login submit process
     */
    private void handleLoginSubmit() {
        DatabaseHandler db = DatabaseHandler.getHandler();
        User user = db.authenticate(username.getText(), password.getText());
        if (user == null) {
            error.setText("Username or password does not exist!");
            error.setVisible(true);
            return;
        }
        Camera.stopAcquisition();
        SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml");
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

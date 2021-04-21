package edu.wpi.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DatabaseInitController implements Initializable {

    @FXML
    public JFXButton btnEmergency;

    @FXML
    public JFXButton btnExit;

    @FXML
    public ImageView gif;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream = getClass().getResourceAsStream("/edu/wpi/teamB/images/login/databaseInit.gif");
        assert inputStream != null;
        Image image = new Image(inputStream);
        gif.setImage(image);
    }

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnStaff":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/login/loginPage.fxml");
                break;
            case "btnGuest":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/login/loginOptions.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }
}

package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DatabaseInitController extends BasePageController implements Initializable {

    @FXML
    public ImageView gif;

    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream = getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/images/login/databaseInit.gif");
        assert inputStream != null;
        Image image = new Image(inputStream);
        gif.setImage(image);
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnStaff":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                break;
            case "btnGuest":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/menus/userDirectoryMenu.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

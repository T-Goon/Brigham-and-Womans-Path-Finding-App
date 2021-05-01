package edu.wpi.cs3733.D21.teamB.views.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D21.teamB.entities.LastFocused;
//import edu.wpi.cs3733.D21.teamB.entities.TextAreaFocusable;
import edu.wpi.cs3733.D21.teamB.entities.OnScreenKeyboard;
import edu.wpi.cs3733.D21.teamB.entities.TextAreaFocusable;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.SneakyThrows;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController extends BasePageController implements Initializable{

    @FXML
    public JFXButton btnLogin;

    @FXML
    public JFXButton btnRegister;

    @FXML
    private JFXButton btnCovid;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton osk;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextArea testText;
    OnScreenKeyboard onScreenKeyboard = OnScreenKeyboard.getInstance();
/*

    LastFocused lastFocused = LastFocused.getInstance();
   @Override
    public void initialize(URL location, ResourceBundle resources) {
            onScreenKeyboard = OnScreenKeyboard.getInstance();
            keyboardVisible = false;
            lastFocused.setAnode(testText);
            try {
                System.out.println(stackPane);
                onScreenKeyboard.initKeyboard(stackPane);
            } catch (AWTException e) {
                e.printStackTrace();
            }

    }*/


    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "osk":
                onScreenKeyboard.getKeyboard().setVisible(true);
                break;
            case "btnLogin":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                break;
            case "btnRegister":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml");
                break;
            case "btnDirections":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
                break;
            case "btnGoogle":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml");
                break;
            case "btnCovid":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

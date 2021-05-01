package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.entities.LastFocused;
import edu.wpi.cs3733.D21.teamB.entities.OnScreenKeyboard;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javax.xml.stream.EventFilter;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BasePageController implements Initializable{

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton osk;

    private boolean keyboardVisible = false;

    OnScreenKeyboard onScreenKeyboard = OnScreenKeyboard.getInstance();
    LastFocused lastFocused = LastFocused.getInstance();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       EventHandler<MouseEvent> onClick = new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               lastFocused.setAnode(event.getPickResult().getIntersectedNode());
           }
       };
       stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED,onClick);
            onScreenKeyboard = OnScreenKeyboard.getInstance();
            keyboardVisible = false;
            try {
                if(!onScreenKeyboard.getInitialized()) {
                    onScreenKeyboard.initKeyboard(stackPane);
                }
                else {
                    onScreenKeyboard.setParent(stackPane);
                    System.out.println(onScreenKeyboard.getParent());
                }
            } catch (AWTException e) {
                e.printStackTrace();
            }

    }

    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "osk":
                onScreenKeyboard.getKeyboard().setVisible(true);
                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnOSKeyboard":
                keyboardVisible = !keyboardVisible;
                onScreenKeyboard.getKeyboard().setVisible(keyboardVisible);
                break;
        }
    }
}

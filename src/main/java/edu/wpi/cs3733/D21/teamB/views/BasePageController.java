package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.entities.LastFocused;
import edu.wpi.cs3733.D21.teamB.entities.OnScreenKeyboard;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.xml.stream.EventFilter;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BasePageController implements Initializable {

    public static boolean ttsOn = false;
    public static boolean oskOn = false;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private StackPane stackPane;

    public TextToSpeech tts = new TextToSpeech();
    private boolean keyboardVisible = false;

    OnScreenKeyboard onScreenKeyboard = OnScreenKeyboard.getInstance();
    LastFocused lastFocused = LastFocused.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (oskOn){
            onScreenKeyboard.getKeyboard().setVisible(true);
        }
        else {
            onScreenKeyboard.getKeyboard().setVisible(false);

        }
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
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }

        for (Node aNode : stackPane.lookupAll("*")) {
            aNode.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (ttsOn) {
                    if (newValue) {
                        String speechOut = aNode.getAccessibleText();
                        if (speechOut != null) {
                            tts.speak(speechOut, 1.0f, false, false);
                        }
                    }
                }
            });
        }
    }

    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(1);
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }


}

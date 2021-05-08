package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.entities.LastFocused;
import edu.wpi.cs3733.D21.teamB.entities.OnScreenKeyboard;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BasePageController implements Initializable {

    public static boolean ttsOn = false;
    public static boolean oskOn = false;
    public boolean firstFocused;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private StackPane stackPane;

    public final TextToSpeech tts = new TextToSpeech();

    OnScreenKeyboard onScreenKeyboard = OnScreenKeyboard.getInstance();
    final LastFocused lastFocused = LastFocused.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initOSK();
        initTTS();

    }

    /**
     * Initializes the text to speech
     */
    private void initTTS() {
        // tts stuff
        for (Node aNode : stackPane.lookupAll("*")) {
            aNode.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (DatabaseHandler.getHandler().getAuthenticationUser().getTtsEnabled().equals("T")) {
                    if (newValue) {
                        String speechOut = aNode.getAccessibleText();
                        if (firstFocused) {
                            speechOut = null;
                            firstFocused = false;
                        }
                        if (speechOut != null) {
                            tts.speak(speechOut, 1.0f, false, false);
                        }
                    }
                }
            });
        }
        tts.stopSpeaking();
    }

    /**
     * Initializes the on-screen keyboard
     */
    private void initOSK() {
        // On screen keyboard stuff
        firstFocused = true;
        Platform.runLater(() -> stackPane.requestFocus());
        onScreenKeyboard.getKeyboard().setVisible(oskOn);
        EventHandler<MouseEvent> onClick = event -> lastFocused.setAnode(event.getPickResult().getIntersectedNode());
        stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, onClick);
        onScreenKeyboard = OnScreenKeyboard.getInstance();
        try {
            if (!onScreenKeyboard.getInitialized()) {
                onScreenKeyboard.initKeyboard(stackPane);
            } else {
                onScreenKeyboard.setParent(stackPane);
            }
        } catch (AWTException e) {
            e.printStackTrace();
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

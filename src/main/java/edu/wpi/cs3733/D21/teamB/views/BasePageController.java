package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.entities.keyboard.LastFocused;
import edu.wpi.cs3733.D21.teamB.entities.keyboard.OnScreenKeyboard;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
    private JFXButton btnEmergency;

    @FXML
    private StackPane stackPane;

    public final TextToSpeech tts = new TextToSpeech();

    OnScreenKeyboard onScreenKeyboard = OnScreenKeyboard.getInstance();
    final LastFocused lastFocused = LastFocused.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initOSK();
        initTTS();
        initChatbot();
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

    private void initChatbot() {
        try {
            Node chatBox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/misc/chatBox.fxml")));
            stackPane.getChildren().add(chatBox);
            StackPane.setAlignment(chatBox, Pos.BOTTOM_RIGHT);
        } catch (IOException e) {
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
            case "btnEmergency":
                SceneSwitcher.switchScene(PageCache.getCurrentPage(), "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}

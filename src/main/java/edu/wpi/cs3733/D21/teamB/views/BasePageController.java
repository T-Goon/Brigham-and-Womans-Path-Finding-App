package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
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

import java.io.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

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
    private boolean keyboardVisible = false;

    OnScreenKeyboard onScreenKeyboard = OnScreenKeyboard.getInstance();
    final LastFocused lastFocused = LastFocused.getInstance();
    Configuration configuration = new Configuration();
    LiveSpeechRecognizer recognizer = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // On screen keyboard stuff
        firstFocused = true;
        Platform.runLater(() -> stackPane.requestFocus());
        onScreenKeyboard.getKeyboard().setVisible(oskOn);
        EventHandler<MouseEvent> onClick = event -> lastFocused.setAnode(event.getPickResult().getIntersectedNode());
        stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, onClick);
        onScreenKeyboard = OnScreenKeyboard.getInstance();
        keyboardVisible = false;
        try {
            if (!onScreenKeyboard.getInitialized()) {
                onScreenKeyboard.initKeyboard(stackPane);
            } else {
                onScreenKeyboard.setParent(stackPane);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }

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

    public void testSTT(){

        recognizer.startRecognition(true);
        SpeechResult result = recognizer.getResult();
        // Pause recognition process. It can be resumed then with startRecognition(false).
        recognizer.stopRecognition();
        if (result.getNbest(3).toString().contains("hello")){
            System.out.println("Contains keyword");
        }
        System.out.format("Hypothesis: %s\n", result.getNbest(5).toString());
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

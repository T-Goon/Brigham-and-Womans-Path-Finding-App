package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import voce.SpeechInterface;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BasePageController implements Initializable {

    public static boolean ttsOn = false;
    public boolean firstFocused;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private StackPane stackPane;

    public TextToSpeech tts = new TextToSpeech();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        voce.SpeechInterface.init("C:/Users/G/Documents/CS3733/TTS/voce-0.9.1/lib",false,true, "file:/C:/Users/G/Documents/CS3733/TTS/voce-0.9.1/lib/gram",
                "digits");
        firstFocused = true;
        Platform.runLater( () -> stackPane.requestFocus() );
                for (Node aNode : stackPane.lookupAll("*")) {
            aNode.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (ttsOn) {
                    if (newValue) {
                        String speechOut = aNode.getAccessibleText();
                        if (firstFocused){
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

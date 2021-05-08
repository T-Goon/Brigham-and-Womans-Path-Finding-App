package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    public VBox base;

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public JFXTextField input;

    @FXML
    public VBox messageHolder;

    @FXML
    public JFXButton btnClose;

    private final TextToSpeech tts = new TextToSpeech();
    public static Thread userThread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thread for getting messages from the bot
        userThread = new Thread(() -> {
            while (true) {
                // Wait for a new message
                if (PageCache.getNewMessagesWaitingForUser().get() != 0) {
                    // Get message and mark as read
                    PageCache.getNewMessagesWaitingForUser().getAndDecrement();
                    Platform.runLater(() -> sendMessage(PageCache.getBotLastMessage()));
                }
            }
        });
        userThread.start();

        // Add all the messages in the cache
        for (Message m : PageCache.getAllMessages())
            sendMessage(m, false);

        // Scroll pane goes down to the bottom when a new message is sent
        scrollPane.vvalueProperty().bind(messageHolder.heightProperty());

        // When closed, wipe the cache and remove itself
        btnClose.setOnAction(e -> {
            ((AnchorPane) base.getParent()).getChildren().remove(base);
            PageCache.getUserMessages().clear();
            PageCache.getBotMessages().clear();
        });
    }

    @FXML
    public void handleSendMessage(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            Message message = new Message(input.getText(), true);
            sendMessage(message);
            input.clear();
            PageCache.addUserMessage(message);
        }
    }

    /**
     * Given a message and whether it's from the user, add it to the chatbot
     *
     * @param message the message to show
     */
    public void sendMessage(Message message) {
        sendMessage(message, true);
    }

    /**
     * Exists so audio is not played when loading
     *
     * @param message              the message to send
     * @param playAudioIfRequested whether to play audio
     */
    private void sendMessage(Message message, boolean playAudioIfRequested) {
        if (message == null || message.getMessage().isEmpty()) return;

        // Adds HBox with text
        HBox messageBox = new HBox();
        Label text = new Label(message.getMessage());
        text.setFont(new Font("MS Reference Sans Serif", 13));
        text.setWrapText(true);
        text.setPadding(new Insets(5, 10, 5, 10));
        messageBox.getChildren().add(text);

        // Determines alignment if sent from user or not
        if (message.isFromUser()) {
            messageBox.setPadding(new Insets(0, 0, 0, 75));
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            text.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(7), Insets.EMPTY)));
            text.setStyle("-fx-text-fill: white");
        } else {
            messageBox.setPadding(new Insets(0, 75, 0, 0));
            messageBox.setAlignment(Pos.CENTER_LEFT);
            text.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(7), Insets.EMPTY)));
            text.setStyle("-fx-text-fill: black");
        }

        // Play the text if text-to-speech is enabled and it's from the chatbot
        if (playAudioIfRequested && !message.isFromUser() && DatabaseHandler.getHandler().getAuthenticationUser().getTtsEnabled().equals("T")) {
            tts.speak(message.getMessage(), 1.0f, false, false);
        }

        // Hey, it exists!
        message.setIndex(messageHolder.getChildren().size());
        messageHolder.getChildren().add(messageBox);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Message {
        private final String message;
        private final boolean fromUser;

        @Setter
        private int index;

        @Override
        public String toString() {
            return "[" + message + (fromUser ? " from User" : " from Chatbot") + " at index " + index + "]";
        }
    }
}
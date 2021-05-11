package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.chatbot.ChatBot;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public HBox topBar;

    @FXML
    public VBox textFieldHolder;

    @FXML
    public JFXButton btnMinimize;

    @FXML
    public JFXButton btnClose;

    private final TextToSpeech tts = new TextToSpeech();
    public static ScheduledExecutorService userThread = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // If the page starts minimized, keep it minimized
        Platform.runLater(() -> {
            if (PageCache.isPageMinimized()) minimize();
        });

        // Thread for getting messages from the bot
        if (userThread != null) {
            userThread.shutdownNow();
            userThread = null;
        }

        PageCache.getIndex().set(0);
        messageHolder.getChildren().clear();
        // Add all the messages in the cache
        for (Message m : PageCache.getAllMessages())
            addMessage(m, false);

        // If there are cached responses, add them
        if (PageCache.getCachedResponses() != null) {
            for (String s : PageCache.getCachedResponses())
                PageCache.addBotMessage(new ChatBoxController.Message(s, false));
            PageCache.getCachedResponses().clear();
        }

        if (PageCache.isTextfieldFocused())
            Platform.runLater(() -> input.requestFocus());


        Runnable msgListener = () -> {
            // Wait for a new message
            if (PageCache.getNewMessagesWaitingForUser().get() != 0) {
                // Get message and mark as read
                PageCache.getNewMessagesWaitingForUser().getAndDecrement();
                Platform.runLater(() -> {
                    addMessage(PageCache.getBotLastMessage());
                    PageCache.getMessageDisplayed().set(true);
                });
            }
        };

        userThread = Executors.newSingleThreadScheduledExecutor();
        userThread.scheduleAtFixedRate(msgListener, 0, 300, TimeUnit.MILLISECONDS);

        // Scroll pane goes down to the bottom when a new message is sent
        scrollPane.vvalueProperty().bind(messageHolder.heightProperty());

        topBar.setOnMouseClicked(e -> {
            if (PageCache.isPageMinimized()) expand();
        });

        btnMinimize.setOnAction(e -> {
            if (!PageCache.isPageMinimized()) minimize();
            else expand();
        });

        // When closed, wipe the cache and remove itself
        btnClose.setOnAction(e -> {
            if (!PageCache.isPageMinimized()) minimize();
            else expand();
            clearMessages();
        });

        input.focusedProperty().addListener(((observable, oldValue, newValue) -> PageCache.setTextfieldFocused(newValue)));
    }

    @FXML
    public void handleSendMessage(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            Message message = new Message(input.getText(), true);
            addMessage(message);
            input.clear();
            PageCache.addUserMessage(message);
        }
    }

    /**
     * Clears messages from the chatbot
     */
    private void clearMessages() {
        PageCache.getUserMessages().clear();
        PageCache.getBotMessages().clear();
        messageHolder.getChildren().clear();
        ChatBot.stateManager.reset();
    }

    /**
     * Minimizes the chat window
     */
    private void minimize() {
        PageCache.setPageMinimized(true);
        scrollPane.setMaxHeight(0);
        textFieldHolder.setMaxHeight(0);
        base.setPrefHeight(topBar.getHeight());
        base.setMaxHeight(topBar.getHeight());
    }

    /**
     * Expands the chat window
     */
    private void expand() {
        PageCache.setPageMinimized(false);
        scrollPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
        textFieldHolder.setMaxHeight(Region.USE_COMPUTED_SIZE);
        base.setPrefHeight(400);
        base.setMaxHeight(Region.USE_PREF_SIZE);
    }

    /**
     * Given a message and whether it's from the user, add it to the chatbot
     *
     * @param message the message to show
     */
    public void addMessage(Message message) {
        addMessage(message, true);
    }

    /**
     * Exists so audio is not played when loading
     *
     * @param message              the message to send
     * @param playAudioIfRequested whether to play audio
     */
    private void addMessage(Message message, boolean playAudioIfRequested) {
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
        message.setIndex(PageCache.getIndex().getAndIncrement());
        messageBox.setId(text.getText() + "Box" + message.getIndex());
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
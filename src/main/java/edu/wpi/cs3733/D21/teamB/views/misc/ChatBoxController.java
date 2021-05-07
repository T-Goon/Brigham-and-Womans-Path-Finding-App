package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    public JFXTextField input;

    @FXML
    public VBox messageHolder;

    @FXML
    public JFXButton btnClose;

    List<HBox> messages = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnClose.setOnAction(e -> System.out.println("should close now"));
    }

    @FXML
    public void handleSendMessage(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            String message = input.getText();
            input.clear();
            addMessage(message, Math.random() > 0.5); // todo change back to true
        }
    }

    /**
     * Given a message and whether it's from the user, add it to the chatbot
     *
     * @param message  the message to show
     * @param fromUser whether the message is from the user or not
     */
    public void addMessage(String message, boolean fromUser) {
        if (message == null || message.isEmpty()) return;

        // Adds HBox with text
        HBox messageBox = new HBox();
        Label text = new Label(message);
        text.setFont(new Font("MS Reference Sans Serif", 13));
        text.setStyle("-fx-text-fill: white");
        text.setWrapText(true);
        text.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(7), Insets.EMPTY)));
        text.setPadding(new Insets(5, 10, 5, 10));
        messageBox.getChildren().add(text);

        // Determines alignment if sent from user or not
        if (fromUser) {
            messageBox.setPadding(new Insets(0, 0, 0, 75));
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.setPadding(new Insets(0, 75, 0, 0));
            messageBox.setAlignment(Pos.CENTER_LEFT);
        }

        // Hey, it exists!
        messageHolder.getChildren().add(messageBox);
        messages.add(messageBox);

        // If there are too many messages, get rid of the oldest ones
        while (getHeightOfMessages() > messageHolder.getHeight() - 20) {
            messages.remove(0);
            messageHolder.getChildren().remove(0);
        }
    }

    private double getHeightOfMessages() {
        double height = 0;
        for (HBox box : messages)
            height += box.getHeight() + messageHolder.getSpacing(); // Accounts for VBox spacing
        return height;
    }

    /**
     * @return the most recent saved messages
     */
    public List<String> getMessages() {
        List<String> messageList = new ArrayList<>();
        messages.forEach(elem -> messageList.add(((Label) elem.getChildren().get(0)).getText()));
        return messageList;
    }

    /**
     * @return the last message sent
     */
    public String getLastMessage() {
        return ((Label) messages.get(messages.size() - 1).getChildren().get(0)).getText();
    }
}
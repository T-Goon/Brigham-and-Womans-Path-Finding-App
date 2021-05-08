package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;

public class ChatBot implements Runnable {


    @Override
    public void run() {
        // Constantly wait for message, then respond
        while (true) {
            ChatBoxController.Message input = waitForMessage();
            respond(input);
        }
    }

    /**
     * Waits for the user to send a message.
     *
     * @return the message
     */
    private ChatBoxController.Message waitForMessage() {
        // Wait for a new message
        while (PageCache.getNewMessagesWaitingForBot().get() == 0) {
        }

        // Get message and mark as read
        return PageCache.getUserLastMessage();
    }

    /**
     * Here, this should split into different methods to determine
     * how to respond
     *
     * @param input the input from the user
     */
    private void respond(ChatBoxController.Message input) {
        // TODO do more than return the input of whatever it is
        ChatBoxController.Message returnMessage = new ChatBoxController.Message(input.getMessage(), false);
        sendMessage(returnMessage);
    }

    /**
     * Actually send the message to the user
     *
     * @param input the message to send
     */
    private void sendMessage(ChatBoxController.Message input) {
        PageCache.addBotMessage(input);
    }
}

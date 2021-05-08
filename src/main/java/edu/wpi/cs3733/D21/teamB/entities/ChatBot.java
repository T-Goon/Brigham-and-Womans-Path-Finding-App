package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;

import java.util.concurrent.ThreadLocalRandom;

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
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000 + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // TODO do more than return the input of whatever it is
        if (input.getMessage().equals("hello world")) sendMessage("hey");
        else sendMessage(new StringBuilder(input.getMessage()).reverse().toString());
    }

    /**
     * Actually send the message to the user
     *
     * @param message the message to send
     */
    private void sendMessage(String message) {
        PageCache.addBotMessage(new ChatBoxController.Message(message, false));
    }
}

package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.chatbot.ab.Bot;
import edu.wpi.cs3733.D21.teamB.chatbot.ab.Chat;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class ChatBot implements Runnable {

    private final Chat chatSession;

    public ChatBot() {
//        URL resource = getClass().getResource("/edu/wpi/cs3733/D21/teamB");
//
//        String path = resource.getPath().substring(0, resource.getPath().length() - 5);
//        if (path.startsWith("file:/")) path = path.substring(6);
//        System.out.println(path);
        File f = new File(".");
        for(File f1 : f.listFiles()){
            System.out.println(f1.getAbsolutePath());
        }
        String path = f.getAbsolutePath().replaceAll("\\\\","/");
        path = path.substring(0, path.length()-2);
        path = path + "/src/main/resources/edu/wpi/cs3733/D21/teamB";
        Bot bot = new Bot("Mike Bedard", "edu/wpi/cs3733/D21/teamB");
        chatSession = new Chat(bot);
    }

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
        else try {
            // TODO change host to prevent UnknownHostException
            sendMessage(chatSession.multisentenceRespond(input.getMessage()));
        } catch (Exception ignored) {
        }
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

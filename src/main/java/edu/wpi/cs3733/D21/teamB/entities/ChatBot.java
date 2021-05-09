package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.util.FileUtil;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class ChatBot implements Runnable {

    private final Chat chatSession;

    public ChatBot() {
        String base = new File("").getAbsolutePath().replace("\\", "/");
        String copiedPath = base + "/bots.zip";
        try {
            FileUtil.copy(getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/bots.zip"), copiedPath);
            ZipFile zipFile = new ZipFile(copiedPath);
            zipFile.extractAll(base);
        } catch (ZipException e) {
            e.printStackTrace();
        } finally {
            Bot bot = new Bot("Mike Bedard", base);
            chatSession = new Chat(bot);
            new File(copiedPath).delete();
            FileUtil.deleteDirectory(new File(base + "/bots"));
        }
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

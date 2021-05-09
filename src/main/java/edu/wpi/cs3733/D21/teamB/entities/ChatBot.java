package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.util.FileUtil;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import javafx.application.Platform;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;

public class ChatBot implements Runnable {

    private ChatState state;
    private String previousUserMessage = "";
    private String previousBotMessage = "";

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

            // Suppress System.out
            PrintStream printStream = System.out;
            System.setOut(new PrintStream(new OutputStream() {
                public void write(int b) {
                    // NO-OP
                }
            }));

            // Make bot and delete temp files
            state = ChatState.NORMAL;
            Bot bot = new Bot("Mike Bedard", base);
            chatSession = new Chat(bot);
            new File(copiedPath).delete();
            FileUtil.deleteDirectory(new File(base + "/bots"));

            System.setOut(printStream);

        }
    }

    @Override
    public void run() {
        // Constantly wait for message, then respond
        while (App.isRunning()) {
            ChatBoxController.Message input = waitForMessage();
            respond(input);
            if (input != null) previousUserMessage = input.getMessage();
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
            if (!App.isRunning()) return null;
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
        if (input == null) return;

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2001));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (state == ChatState.COVID) respondCovid(input.getMessage());
        else if (state == ChatState.LOGIN) respondLogin(input.getMessage());
        else if (state == ChatState.REGISTER) respondRegister(input.getMessage());
        else if (state == ChatState.PATHFINDING) respondPathfinding(input.getMessage());
        else if (state == ChatState.GOOGLE_MAPS) respondGoogleMaps(input.getMessage());
        else if (state == ChatState.NORMAL) respondNormal(input.getMessage());
        else unsureResponse(input.getMessage());
        previousUserMessage = input.getMessage();
    }

    private void respondNormal(String message) {
        if (message.contains("covid") && !PageCache.getCurrentPage().equals("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml")) {
            state = ChatState.COVID;
            sendMessage("Do you want to go to the COVID-19 survey?");
        } else if (message.contains("login")) {
            state = ChatState.LOGIN;
            sendMessage("Do you want to go to the login page?");
        } else if (message.contains("register") || (message.contains("make") && message.contains("user"))) {
            state = ChatState.REGISTER;
            sendMessage("Do you want to make a new user?");
        } else if (message.contains("directions") && (message.contains("where") || message.contains("hospital"))) {
            state = ChatState.PATHFINDING;
            sendMessage("Do you want to get directions within the hospital?");
        } else if (message.contains("directions") && (message.contains("car") || message.contains("drive"))) {
            state = ChatState.GOOGLE_MAPS;
            sendMessage("Do you want to get directions to the hospital?");
        } else {
            unsureResponse(message);
        }
    }

    private void respondLogin(String message) {
        // If just asked to go to the login page, take them there
        if (previousBotMessage.equals("Do you want to go to the login page?") && containsAny(message, "yes", "y", "ye") && !PageCache.getCurrentPage().equals("/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml")) {
            sendMessage("Taking you to the login page...");
            switchToPage("/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
        } else if (previousBotMessage.equals("Do you want to go to the login page?")) {
            sendMessage("Okay, no problem.");
            state = ChatState.NORMAL;
        } else {
            unsureResponse(message);
        }
    }

    private void respondRegister(String message) {
        // If just asked to go to the register page take them there
        if (previousBotMessage.equals("Do you want to make a new user?") && containsAny(message, "yes", "y", "ye") && !PageCache.getCurrentPage().equals("/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml")) {
            sendMessage("Taking you to the create new user page...");
            switchToPage("/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml");
        } else if (previousBotMessage.equals("Do you want to make a new user?")) {
            sendMessage("Okay, no problem.");
            state = ChatState.NORMAL;
        } else {
            unsureResponse(message);
        }
    }

    private void respondCovid(String message) {
        // If just asked to go to the covid survey, take them there
        if (previousBotMessage.equals("Do you want to go to the COVID-19 survey?") && containsAny(message, "yes", "y", "ye") && !PageCache.getCurrentPage().equals("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml")) {
            sendMessage("Taking you to the covid survey...");
            switchToPage("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
        } else if (previousBotMessage.equals("Do you want to go to the COVID-19 survey?")) {
            sendMessage("Okay, no problem.");
            state = ChatState.NORMAL;
        } else {
            unsureResponse(message);
        }
    }

    private void respondPathfinding(String message) {
        // If just asked to go to the covid survey, take them there
        if (previousBotMessage.equals("Do you want to get directions within the hospital?") && containsAny(message, "yes", "y", "ye") && !PageCache.getCurrentPage().equals("/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml")) {
            sendMessage("Taking you to the directions page...");
            switchToPage("/edu/wpi/cs3733/D21/teamB/views/map/pathfindingMenu.fxml");
        } else if (previousBotMessage.equals("Do you want to get directions within the hospital?")) {
            sendMessage("Okay, no problem.");
            state = ChatState.NORMAL;
        } else {
            unsureResponse(message);
        }
    }

    private void respondGoogleMaps(String message) {
        // If just asked to go to the covid survey, take them there
        if (previousBotMessage.equals("Do you want to get directions to the hospital?") && containsAny(message, "yes", "y", "ye") && !PageCache.getCurrentPage().equals("/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml")) {
            sendMessage("Taking you to the Google Maps page...");
            switchToPage("/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml");
        } else if (previousBotMessage.equals("Do you want to get directions to the hospital?")) {
            sendMessage("Okay, no problem.");
            state = ChatState.NORMAL;
        } else {
            unsureResponse(message);
        }
    }

    private void unsureResponse(String input) {
        try { // Otherwise, just respond however it normally would
            // Suppress System.out
            PrintStream printStream = System.out;
            System.setOut(new PrintStream(new OutputStream() {
                public void write(int b) {
                    // NO-OP
                }
            }));
            sendMessage(chatSession.multisentenceRespond(input));
            System.setOut(printStream);
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
        previousBotMessage = message;
    }

    /**
     * Given n amount of strings and a starting message,
     * checks whether the message contains any of them
     *
     * @param message the message to check
     * @param check   list of things to check
     * @return true if message has one of those in it
     */
    private boolean containsAny(String message, String... check) {
        boolean contains = false;
        for (String s : check) {
            if (message.contains(s)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    /**
     * Switches to a page later so JavaFX doesn't get mad
     *
     * @param path the path to the FXML file to switch to
     */
    private void switchToPage(String path) {
        Platform.runLater(() -> {
            SceneSwitcher.switchScene(PageCache.getCurrentPage(), path);
        });
    }

    private enum ChatState {
        NORMAL,
        COVID,
        PATHFINDING,
        LOGIN,
        REGISTER,
        GOOGLE_MAPS
    }
}

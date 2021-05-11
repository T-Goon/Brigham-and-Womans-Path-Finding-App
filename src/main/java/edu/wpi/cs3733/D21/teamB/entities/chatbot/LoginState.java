package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LoginState implements IState {

    private final Stack<String> messagesSent = new Stack<>();
    private final Stack<String> messagesReceived = new Stack<>();

    @Override
    public List<String> respond(String input) {
        messagesReceived.push(input);
        List<String> response = new ArrayList<>();

        // First message
        if (messagesSent.isEmpty()) {
            response.add("Do you want to log in?");
        } else if (messagesSent.peek().equals("Do you want to log in?")) { // Second message
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                response.add("/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                response.add("Do you need any assistance?");
                PageCache.getCachedResponses().add("Do you need any assistance?");
            } else {
                response.add("No worries!");
                response.add("return");
                PageCache.getCachedResponses().add("Is there anything I can help you with?");
            }
        } else if (messagesSent.peek().equals("Do you need any assistance?")) {
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Please type in your username and password into the fields on the screen.");
            } else if (StateManager.containsAny(input, new String[]{"register", "don't have"})) {
                response.add("Do you have an account already?");
            } else {
                response.add("No worries! I'll be here.");
            }
        } else if (messagesSent.peek().equals("Do you have an account already?")) {
            if (StateManager.containsAny(input, new String[]{"n", "no", "nay", "nope", "nah"})) {
                response.add("Okay! Taking you to make a new account now...");
                response.add("/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml");
                response.add("How can I help?");
            }
        }

        if (!response.isEmpty()) {
            for (String s : response)
                messagesSent.push(s);
        }

        if (PageCache.getCachedResponses() != null && !PageCache.getCachedResponses().isEmpty()) {
            for (String s : PageCache.getCachedResponses()) {
                messagesSent.push(s);
            }
        }

        return response;
    }
}

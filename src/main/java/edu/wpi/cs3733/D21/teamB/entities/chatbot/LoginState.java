package edu.wpi.cs3733.D21.teamB.entities.chatbot;

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
        } else if (messagesSent.peek().equals("Do you want to log in?")) {
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                response.add("/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                response.add("Do you need any help on this page?");
            } else {
                response.add("No worries!");
                response.add("return");
            }
        }


        if (!response.isEmpty()) {
            for (String s : response)
                messagesSent.push(s);
        }
        return response;
    }
}

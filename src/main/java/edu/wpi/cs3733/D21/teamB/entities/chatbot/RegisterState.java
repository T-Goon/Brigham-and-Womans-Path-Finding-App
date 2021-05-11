package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RegisterState implements IState {

    private final Stack<String> messagesSent = new Stack<>();

    @Override
    public List<String> respond(String input) {
        List<String> response = new ArrayList<>();

        if (!messagesSent.isEmpty() && messagesSent.peek().equals("unsure")) {
            messagesSent.pop();
        }

        if (StateManager.containsAny(input, new String[]{"face", "ID", "faceID"})) {
            response.add("Face ID is a technology that allows you to verify your credentials using facial detection.");
            response.add("You must take a picture of your face using the 'Take Picture' button so Face ID can work.");
            response.add("Do you need any assistance?");
        } else if (messagesSent.isEmpty()) { // First message
            response.add("Do you want to make a new account?");
        } else if (messagesSent.peek().equals("Do you want to make a new account?")) { // Second message
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                PageCache.getCachedResponses().add("Do you need any assistance?");
                response.add("/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope"})) {
                response.add("No worries!");
                response.add("Is there anything I can help you with?");
                response.add("return");
            } else {
                response.add("unsure");
                response.add("Do you want to make a new account?");
            }
        } else if (messagesSent.peek().equals("Do you need any assistance?")) { // Third message in
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Please fill in all the information in each field.");
                response.add("You must also take a picture of your face using the 'Take Picture' button so Face ID can work.");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope"})) {
                response.add("No worries! I'll be here.");
            } else if (StateManager.containsAny(input, new String[]{"speech", "text"})) {
                response.add("If you would like to enable text to speech on your account toggle the 'Enable Text-To-Speech' and this will allow elements to provide helpful descriptions.");
                response.add("Do you need any assistance?");
            } else if (StateManager.containsAny(input, new String[]{"user", "username"})) {
                response.add("The username field should be a memorable string that you will use to login to the hospital system in conjunction with the password. Do not share it with others.");
                response.add("Do you need any assistance?");
            } else if (StateManager.containsAny(input, new String[]{"pass", "password", "word"})) {
                response.add("The password field should be a memorable string that you will use to login to the hospital system in conjunction with the username. Absolutely do not share it with others.");
                response.add("Do you need any assistance?");
            } else if (StateManager.containsAny(input, new String[]{"name", "first", "last"})) {
                response.add("Please input your first and last name in the respective fields. This information will not be shared but will allow hospital employees to identify you if need be.");
                response.add("Do you need any assistance?");
            } else if (StateManager.containsAny(input, new String[]{"email", "mail", "address"})) {
                response.add("Please include a valid email address. Future notifications will be sent to you via this address.");
                response.add("Do you need any assistance?");
            } else {
                response.add("unsure");
                response.add("Do you need any assistance?");
            }
        } else if (messagesSent.peek().equals("No worries! I'll be here.") || messagesSent.peek().equals("If you have any questions, feel free to ask!")) {
            response.add("Do you need any assistance?");
        } else { // Otherwise, what the heck?
            response.add("unsure");
        }

        for (String s : response)
            messagesSent.push(s);

        if (PageCache.getCachedResponses() != null && !PageCache.getCachedResponses().isEmpty()) {
            for (String s : PageCache.getCachedResponses()) {
                messagesSent.push(s);
            }
        }

        return response;
    }
}

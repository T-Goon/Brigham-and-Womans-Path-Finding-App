package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CovidState implements IState {

    public CovidState() {
    }

    public CovidState(String initialMessage) {
        messagesSent.push(initialMessage);
    }

    private final Stack<String> messagesSent = new Stack<>();

    @Override
    public List<String> respond(String input) {
        List<String> response = new ArrayList<>();

        if (messagesSent.isEmpty()) { // First message
            response.add("Do you need help filling out a COVID survey?");
        } else if (messagesSent.peek().equals("Do you need help filling out a COVID survey?")) { // Second message
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                PageCache.getCachedResponses().add("Do you need any assistance?");
                response.add("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope"})) {
                response.add("No worries!");
                response.add("Is there anything I can help you with?");
                response.add("return");
            } else {
                response.add("unsure");
                response.add("Do you need help filling out a COVID survey?");
            }
        } else if (messagesSent.peek().equals("Do you need any assistance?")) { // Third message in
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Please fill out the following questions regarding any symptoms you might be experiencing.");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope"})) {
                response.add("No worries! I'll be here.");
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

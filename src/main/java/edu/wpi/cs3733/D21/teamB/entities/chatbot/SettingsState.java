package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SettingsState implements IState {

    private final Stack<String> messagesSent = new Stack<>();

    @Override
    public List<String> respond(String input) {
        List<String> response = new ArrayList<>();

        if (!messagesSent.isEmpty() && messagesSent.peek().equals("unsure")) {
            messagesSent.pop();
        }

        if (messagesSent.isEmpty()) { // First message
            response.add("Do you want to go to the settings page?");
        } else if (messagesSent.peek().equals("Do you want to go to the settings page?")) { // Second message
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                response.add("/edu/wpi/cs3733/D21/teamB/views/menus/settingsMenu.fxml");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope"})) {
                response.add("No worries!");
                response.add("Is there anything I can help you with?");
                response.add("return");
            } else {
                response.add("unsure");
                response.add("Do you want to go to the settings page?");
            }
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

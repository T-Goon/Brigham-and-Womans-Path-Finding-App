package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class GoogleMapsState implements IState {
    private final Stack<String> messagesSent = new Stack<>();

    @Override
    public List<String> respond(String input) {
        List<String> response = new ArrayList<>();

        if (messagesSent.isEmpty()) { // First message
            response.add("Do you need help finding the hospital?");
        } else if (messagesSent.peek().equals("Do you need help finding the hospital?")) { // Second message
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                response.add("/edu/wpi/cs3733/D21/teamB/views/map/directionsMenu.fxml");
                response.add("Do you need any assistance?");
                PageCache.getCachedResponses().add("Do you need any assistance?");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope",})) {
                response.add("No worries!");
                response.add("return");
                PageCache.getCachedResponses().add("Is there anything I can help you with?");
            }
        } else if (messagesSent.peek().equals("Do you need any assistance?")) { // Third message in
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Please fill select your starting point and preferred parking lot from the map");
                response.add("The map will then give you the fastest directions to the hospital");
                response.add("If it is a medical emergency however, please call 911");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope",})) {
                response.add("No worries! I'll be here.");
            } else {
                response.add("I'm sorry, I didn't understand that.");
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

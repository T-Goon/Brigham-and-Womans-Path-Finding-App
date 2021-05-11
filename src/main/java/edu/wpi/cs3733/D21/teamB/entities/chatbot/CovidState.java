package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CovidState implements IState {
    private final Stack<String> messagesSent = new Stack<>();

    @Override
    public List<String> respond(String input) {
        List<String> response = new ArrayList<>();

        if (messagesSent.isEmpty()) { // First message
            response.add("Do you need help filling out a COVID survey?");
        } else if (messagesSent.peek().equals("Do you need help filling out a COVID survey?")) { // Second message
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Okay! Taking you there now...");
                response.add("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
                response.add("Do you need any assistance?");
                PageCache.getCachedResponses().add("Do you need any assistance?");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope",})) {
                response.add("No worries!");
                response.add("return");
                PageCache.getCachedResponses().add("Is there anything I can help you with?");
            }
        } else if (messagesSent.peek().equals("Do you need any assistance?")) { // Third message in
            if (StateManager.containsAny(input, new String[]{"y", "ye", "yes", "yeah", "yup"})) {
                response.add("Please fill out the above questions regarding any symptoms you might be experiencing");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"n", "no", "nah", "nope",})) {
                response.add("No worries! I'll be here.");
            } else if (StateManager.containsAny(input, new String[]{"current","location"})) {
                response.add("In the current location field please input the closest location to where you currently are");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"symptoms","chills","cough","fever","shortness","sore","headache","ache","runny","nausea","smell"})) {
                response.add("If you think you might have any of the symptoms above please check as many as you match");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"close","contact"})) {
                response.add("If you have been around someone diagnosed with COVID in the past two weeks please select yes, otherwise select no");
                response.add("If you have any questions, feel free to ask!");
            } else if (StateManager.containsAny(input, new String[]{"positive","test"})) {
                response.add("If you have received a positive COVID test in the past two weeks please select yes, otherwise select no");
                response.add("If you have any questions, feel free to ask!");
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

package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import java.io.OutputStream;
import java.io.PrintStream;

public class StateManager {

    private RegisterState registerState;
    private LoginState loginState;
    private CovidState covidState;
    private PathfindingState pathfindingState;
    private GoogleMapsState googleMapsState;

    private IState currentState;

    public StateManager() {
        refresh();
    }

    /**
     * Refreshes the states
     */
    private void refresh() {
        this.registerState = new RegisterState();
        this.loginState = new LoginState();
        this.covidState = new CovidState();
        this.pathfindingState = new PathfindingState();
        this.googleMapsState = new GoogleMapsState();
    }

    /**
     * Based on the message, determines into what state
     * the
     *
     * @param input the message to check
     */
    public String respond(String input) {
        // If switching tracks, reset everything back to the beginning
        if (containsAny(input, "covid") && !(currentState instanceof CovidState)) {
            refresh();
            currentState = covidState;
        } else if (containsAny(input, "register", "make", "user") && !(currentState instanceof RegisterState)) {
            refresh();
            currentState = registerState;
        } else if (containsAny(input, "login", "log", "in") && !(currentState instanceof LoginState)) {
            refresh();
            currentState = loginState;
        } else if (containsAny(input, "where", "hospital", "pathfinding", "directions", "map", "path") && !(currentState instanceof PathfindingState)) {
            refresh();
            currentState = pathfindingState;
        } else if (containsAny(input, "google", "maps", "drive", "directions") && !(currentState instanceof GoogleMapsState)) {
            refresh();
            currentState = googleMapsState;
        } else if (currentState == null) {
            return null;
        }

        // Let the current state deal with the response
        return currentState.respond(input);
    }

    /**
     * Given n amount of words and a starting message,
     * checks whether the message contains any of them
     *
     * @param message the message to check
     * @param check   list of things to check
     * @return true if message has one of those in it
     */
    private boolean containsAny(String message, String... check) {
        String[] words = message.split("\\W+");
        boolean contains = false;
        for (String word : words) {
            for (String s : check) {
                if (word.equals(s)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }
}

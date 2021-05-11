package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StateManager {

    // case insensitive
    private static final String[] COVID_KEYWORDS = {"covid", "survey"};
    private static final String[] REGISTER_KEYWORDS = {"register", "registration", "registering", "make", "user", "new", "account"};
    private static final String[] LOGIN_KEYWORDS = {"login", "log", "logging"};
    private static final String[] PATHFINDING_KEYWORDS = {"where", "find", "hospital", "pathfinding", "directions", "map", "path"};
    private static final String[] GOOGLE_MAPS_KEYWORDS = {"google", "maps", "drive", "driving", "navigation", "Boston"};
    private static final String[] SETTINGS_KEYWORDS = {"settings", "speech", "keyboard", "password", "credits"};

    private IState previousState;
    private IState currentState;

    /**
     * Based on the message, determines into what state
     * the
     *
     * @param input the message to check
     */
    public List<String> respond(String input) {
        // Response to "Who am I?"
        User current = DatabaseHandler.getHandler().getAuthenticationUser();
        if (containsAll(input, new String[]{"who", "am", "i"})) {
            if (current.isAtLeast(User.AuthenticationLevel.PATIENT)) {
                return Collections.singletonList("You are " + current.getFirstName() + " " + current.getLastName() + " and your username is " + current.getUsername() + ".");
            } else {
                return Collections.singletonList("You are not logged in.");
            }
        }

        // Makes sure the user doesn't try to access something without taking the covid survey
        if (!containsAny(input, GOOGLE_MAPS_KEYWORDS) && containsAny(input, PATHFINDING_KEYWORDS) && (current.getAuthenticationLevel() == User.AuthenticationLevel.PATIENT || current.getAuthenticationLevel() == User.AuthenticationLevel.GUEST) && (current.getCovidStatus() == User.CovidStatus.PENDING || current.getCovidStatus() == User.CovidStatus.UNCHECKED)) {
            currentState = new CovidState("Do you need help filling out a COVID survey?");
            List<String> responses = new ArrayList<>();
            responses.add("Sorry, but you must take the COVID-19 survey before accessing the map!");
            responses.add("/edu/wpi/cs3733/D21/teamB/views/covidSurvey/covidSurvey.fxml");
            PageCache.getCachedResponses().add("Do you need help filling out a COVID survey?");
            return responses;
        }

        // If switching tracks, reset everything back to the beginning
        if (containsAny(input, COVID_KEYWORDS) && !(currentState instanceof CovidState)) {
            if (!current.isAtLeast(User.AuthenticationLevel.STAFF)) {
                previousState = currentState;
                currentState = new CovidState();
            } else {
                return Collections.singletonList("Sorry, only patients and guests can take the COVID-19 survey.");
            }
        } else if (containsAny(input, REGISTER_KEYWORDS) && !(currentState instanceof RegisterState)) {
            previousState = currentState;
            currentState = new RegisterState();
        } else if (containsAny(input, LOGIN_KEYWORDS) && !(currentState instanceof LoginState)) {
            previousState = currentState;
            currentState = new LoginState();
        } else if (containsAny(input, GOOGLE_MAPS_KEYWORDS) && !(currentState instanceof GoogleMapsState)) {
            previousState = currentState;
            currentState = new GoogleMapsState();
        } else if (containsAny(input, PATHFINDING_KEYWORDS) && !(currentState instanceof PathfindingState)) {
            previousState = currentState;
            currentState = new PathfindingState();
        } else if (containsAny(input, SETTINGS_KEYWORDS) && !(currentState instanceof SettingsState)) {
            previousState = currentState;
            currentState = new SettingsState();
        } else if (currentState == null) {
            return new ArrayList<>();
        }

        // Let the current state deal with the response, and if null is returned, try with the previous state
        List<String> response = currentState.respond(input);
        if (response.isEmpty()) {
            setCurrentToPrev();

            if (currentState != null)
                response = currentState.respond(input);
        }
        return response;
    }

    public void setCurrentToPrev() {
        currentState = previousState;
        previousState = null;
    }

    public void reset() {
        currentState = null;
        previousState = null;
    }

    /**
     * Given n amount of words and a starting message,
     * checks whether the message contains any of them
     *
     * @param message the message to check
     * @param toCheck list of things to check
     * @return true if message has one of those in it
     */
    public static boolean containsAny(String message, String[] toCheck) {
        String[] words = message.split("\\W+");
        boolean contains = false;
        for (String word : words) {
            for (String s : toCheck) {
                if (word.equals(s)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Given n amount of words and a starting message,
     * checks whether the message contains all of them
     *
     * @param message the message to check
     * @param toCheck list of things to check
     * @return true if message has one of those in it
     */
    public static boolean containsAll(String message, String[] toCheck) {
        String[] words = message.split("\\W+");
        boolean contains = true;
        for (String word : toCheck) {
            boolean wordContained = false;
            for (String inputWord : words) {
                if (inputWord.equals(word)) {
                    wordContained = true;
                    break;
                }
            }
            if (!wordContained) {
                contains = false;
                break;
            }
        }
        return contains;
    }
}

package edu.wpi.cs3733.D21.teamB.entities.chatbot;

import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import javafx.application.Platform;

public interface IState {
    String respond(String input);

    /**
     * Switches to a page later so JavaFX doesn't get mad
     *
     * @param path the path to the FXML file to switch to
     */
    default void switchToPage(String path) {
        Platform.runLater(() -> {
            SceneSwitcher.switchScene(PageCache.getCurrentPage(), path);
        });
    }
}

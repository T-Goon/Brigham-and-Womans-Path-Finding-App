package edu.wpi.teamB.util;

import edu.wpi.teamB.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class SceneSwitcher {
    private static final Stack<String> stack = new Stack<>();

    public static void pushScene(String path) {
        stack.push(path);
    }

    public static String popScene() {
        String path = stack.pop();
        return path;
    }

    public static void switchScene(Class newClass, String path) throws IOException {
        Pane root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
        App.getPrimaryStage().getScene().setRoot(root);
    }
}

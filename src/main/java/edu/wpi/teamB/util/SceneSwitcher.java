package edu.wpi.teamB.util;

import edu.wpi.teamB.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class SceneSwitcher {

    private static final Stack<String> stack = new Stack<>();

    /**
     * Goes back to the previous page
     *
     * @param newClass the class instance
     */
    public static void goBack(Class newClass) {
        if (stack.isEmpty()) return;
        String path = stack.pop();
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
    }

    /**
     * Switches to a scene and removes it from the stack.
     *  @param newClass the class instance
     * @param path     the path to the FXML file to switch to
     */
    public static void switchToTemp(Class newClass, String path) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
    }

    /**
     * Switches to a scene and removes it from the stack.
     *  @param newClass the class instance
     * @param path     the path to the FXML file to switch to
     */
    public static void switchScene(Class newClass, String oldPath, String path) {
        stack.push(oldPath);
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
    }
}

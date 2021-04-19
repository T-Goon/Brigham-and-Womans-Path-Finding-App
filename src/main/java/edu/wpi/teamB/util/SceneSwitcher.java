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

    public static String peekLastScene(){
        return stack.peek();
    }

    public static String popLastScene(){
        return stack.pop();
    }

    /**
     * Goes back to a previous page by popping from the stack
     * however many times it is requested to
     *
     * @param newClass the class instance
     * @param pagesBack the number of pages to go back
     */
    public static void goBack(Class newClass, int pagesBack) {
        if (stack.isEmpty()) return;
        String path = "";
        for (int i = 0; i < pagesBack; i++)
            if (!stack.isEmpty()) path = stack.pop();
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
    }


    /**
     * Switches to a scene, but doesn't add it to the stack
     *
     * @param newClass the class instance
     * @param path     the path to the FXML file to switch to
     */
    public static void switchToTemp(Class newClass, String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
    }

    /**
     * Switches to a scene and removes it from the stack.
     *
     * @param newClass the class instance
     * @param path     the path to the FXML file to switch to
     */
    public static void switchScene(Class newClass, String oldPath, String path) {
        stack.push(oldPath);
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(newClass.getResource(path)));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
    }
}

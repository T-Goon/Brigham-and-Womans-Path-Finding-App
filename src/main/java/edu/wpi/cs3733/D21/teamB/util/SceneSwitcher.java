package edu.wpi.cs3733.D21.teamB.util;

import edu.wpi.cs3733.D21.teamB.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class SceneSwitcher {

    private static final Stack<String> stack = new Stack<>();

    //State variables
    public static boolean isEmergencyBtn;
    public static UserState editingUserState;

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
     * @param pagesBack the number of pages to go back
     */
    public static void goBack(int pagesBack) {
        if (stack.isEmpty()) {
            switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
            return;
        }
        String path = "";
        for (int i = 0; i < pagesBack; i++)
            if (!stack.isEmpty()) path = stack.pop();
        switchFromTemp(path);
    }


    /**
     * Switches to a scene, but doesn't add it to the stack
     *
     * @param path the path to the FXML file to switch to
     */
    public static void switchFromTemp(String path) {
        boolean isTextFieldFocused = PageCache.isTextfieldFocused();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource(path)));
            PageCache.setCurrentPage(path);
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Path \"" + path + "\" is malformed or nonexistent!");
            e.printStackTrace();
        }
        PageCache.setTextfieldFocused(isTextFieldFocused);
    }

    /**
     * Switches to a scene and removes it from the stack.
     *
     * @param path     the path to the FXML file to switch to
     */
    public static void switchScene(String oldPath, String path) {
        stack.push(oldPath);
        switchFromTemp(path);
    }

    /**
     * Adds a page to the stack (useful for injecting a page between the next page's back button and this one)
     *
     * @param path the path to the FXML file to push
     */
    public static void pushPath(String path) {
        stack.push(path);
    }

    public enum UserState {
        ADD,
        EDIT,
        EDIT_SELF
    }
}

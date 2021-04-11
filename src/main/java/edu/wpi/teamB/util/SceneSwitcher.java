package edu.wpi.teamB.util;

import edu.wpi.teamB.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SceneSwitcher {

    public static void switchScene(Class newClass, String path) throws IOException {
        Pane root = FXMLLoader.load(newClass.getResource(path));
        App.getPrimaryStage().getScene().setRoot(root);
    }
}

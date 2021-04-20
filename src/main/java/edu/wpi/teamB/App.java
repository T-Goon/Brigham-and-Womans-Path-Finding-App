package edu.wpi.teamB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.util.CSVHandler;
import edu.wpi.teamB.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;
    private DatabaseHandler db;

    @Override
    public void init() {
        System.out.println("Starting Up");
        db = DatabaseHandler.getDatabaseHandler("main.db");

        // If the database is uninitialized, fill it with the csv files
        db.resetDatabase(new ArrayList<>(Collections.singleton("Users")));
        db.executeSchema();
        if (!db.isInitialized())
            db.loadNodesEdges(CSVHandler.loadCSVNodes("/edu/wpi/teamB/csvFiles/bwBnodes.csv"), CSVHandler.loadCSVEdges("/edu/wpi/teamB/csvFiles/bwBedges.csv"));
        db.addUser(new User("admin", "Professor", "X", User.AuthenticationLevel.ADMIN, null), "password");
        db.addUser(new User("staff", "Mike", "Bedard", User.AuthenticationLevel.STAFF, null), "password");
        db.addUser(new User("guest", "T", "Goon", User.AuthenticationLevel.GUEST, null), "password");
    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/loginPages/loginOptions.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            //Press F11 to escape fullscreen. Allows users to use the ESC key to go back to the previous scene
            primaryStage.setFullScreenExitKeyCombination(new KeyCombination() {
                @Override
                public boolean match(KeyEvent event) {
                    return event.getCode().equals(KeyCode.F11);
                }
            });

            primaryStage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        DatabaseHandler.getDatabaseHandler("main.db").shutdown();
        System.out.println("Shutting Down");
    }
}

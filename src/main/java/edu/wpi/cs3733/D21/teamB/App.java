package edu.wpi.cs3733.D21.teamB;

import java.io.*;
import java.util.*;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.ChatBot;
import edu.wpi.cs3733.D21.teamB.util.ExternalCommunication;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class App extends Application {

    private static Stage primaryStage;
    private DatabaseHandler db;
    private Thread dbThread;
    private Thread chatBotThread;
    private PrintStream printStream = System.out;

    @Override
    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("Starting Up");
        db = DatabaseHandler.getHandler();

        // Suppress

        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                // NO-OP
            }
        }));
    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/misc/chatBox.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            //Press F11 to escape fullscreen. Allows users to use the ESC key to go back to the previous scene
            primaryStage.setFullScreenExitKeyCombination(new KeyCombination() {
                @Override
                public boolean match(KeyEvent event) {
                    return event.getCode().equals(KeyCode.F11);
                }
            });

            // Starts the chat bot thread
            chatBotThread = new Thread(new ChatBot());
            chatBotThread.start();
            primaryStage.show();

//            primaryStage.setFullScreen(true);

            // If the database is uninitialized, fill it with the csv files
//            try {
//                db.executeSchema();
//                if (!db.isInitialized()) {
//                    SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/databaseInit.fxml");
//                    primaryStage.show();
//
//                    dbThread = new Thread(() -> {
//                        try {
//                            db.loadNodesEdges(CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBnodes.csv"), CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBedges.csv"));
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                        Platform.runLater(() -> SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml"));
//                    });
//                    dbThread.start();
//                } else primaryStage.show();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return;
//            }
//
//            try {
//                HashMap<User, String> requiredUsers = new HashMap<>();
//
//                //Required users
//                requiredUsers.put(new User("admin", "admin@fakeemail.com", "Professor", "X", User.AuthenticationLevel.ADMIN, "F", null), "admin");
//                requiredUsers.put(new User("staff", "bwhapplication@gmail.com", "Mike", "Bedard", User.AuthenticationLevel.STAFF, "F", null), "staff");
//                requiredUsers.put(new User("patient", "patient@fakeemail.com", "T", "Goon", User.AuthenticationLevel.PATIENT, "F", null), "patient");
//
//                for (User u : requiredUsers.keySet()) {
//                    if (db.getUserByUsername(u.getUsername()) == null) {
//                        db.addUser(u, requiredUsers.get(u));
//                    }
//                }
//                db.resetTemporaryUser();
//                db.deauthenticate();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        if (dbThread != null)
            dbThread.stop();
        if (chatBotThread != null)
            chatBotThread.stop();
        if (ChatBoxController.userThread != null)
            ChatBoxController.userThread.stop();
        for (Thread t : ExternalCommunication.threads)
            t.stop();
        ExternalCommunication.threads.clear();

        System.setOut(printStream);
        DatabaseHandler.getHandler().shutdown();
        System.out.println("Shutting Down");
    }
}

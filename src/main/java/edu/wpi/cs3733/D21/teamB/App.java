package edu.wpi.cs3733.D21.teamB;

import java.io.*;
import java.sql.SQLException;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.chatbot.ChatBot;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.util.FileUtil;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.face.Camera;
import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

public class App extends Application {

    private static Stage primaryStage;
    private DatabaseHandler db;

    @Getter
    private static boolean running = false;

    private final PrintStream printStream = System.out;

    @Override
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileUtil.copy(getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/xml/haarcascade_frontalface_alt.xml"),
                    new File("").getAbsolutePath()+"/haarcascade_frontalface_alt.xml");
            FileUtil.copy(getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/faces/pytorch_models/facenet/facenet.pt"),
                    new File("").getAbsolutePath()+"/facenet/facenet.pt",
                    new File("").getAbsolutePath()+"/facenet");
        } catch (Exception e) {
            e.printStackTrace();
        }

        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        running = true;
        System.out.println("Starting Up");
        db = DatabaseHandler.getHandler();
    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/login/databaseInit.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            //Press F11 to escape fullscreen. Allows users to use the ESC key to go back to the previous scene
            primaryStage.setFullScreenExitKeyCombination(new KeyCombination() {
                @Override
                public boolean match(KeyEvent event) {
                    return event.getCode().equals(KeyCode.F11);
                }
            });

            primaryStage.setFullScreen(true);

            // If the database is uninitialized, fill it with the csv files
            try {
                db.executeSchema();
                if (!db.isInitialized()) {

                    primaryStage.show();
                    // Load database in a separate thread to help with performance
                    Thread dbThread = new Thread(() -> {
                        try {
                            db.loadNodesEdges(CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBnodes.csv"), CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBedges.csv"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml"));
                    });
                    dbThread.setName("dbThread");
                    dbThread.start();

                } else {
                    SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                    primaryStage.show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return;
            } finally {
                // Starts the chat bot thread
                Thread chatBotThread = new Thread(new ChatBot());
                chatBotThread.setName("chatBotThread");
                chatBotThread.start();
            }

            try {
                HashMap<User, String> requiredUsers = new HashMap<>();

                //Required users
                requiredUsers.put(new User("admin", "admin@fakeemail.com", "Professor", "X", User.AuthenticationLevel.ADMIN, "F", null), "admin");
                requiredUsers.put(new User("staff", "bwhapplication@gmail.com", "Mike", "Bedard", User.AuthenticationLevel.STAFF, "F", null), "staff");
                requiredUsers.put(new User("patient", "patient@fakeemail.com", "T", "Goon", User.AuthenticationLevel.PATIENT, "F", null), "patient");

                    for (User u : requiredUsers.keySet()) {
                        if (db.getUserByUsername(u.getUsername()) == null) {
                            db.addUser(u, requiredUsers.get(u));
                        }
                    }
                db.resetTemporaryUser();
                db.deauthenticate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        running = false;
        System.setOut(printStream);
        DatabaseHandler.getHandler().shutdown();
        try {
            FileUtils.forceDelete(new File("haarcascade_frontalface_alt.xml"));
            FileUtils.forceDelete(new File("facenet"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Turn off camera when
        if (Camera.cameraActive) {
            // release the camera
            Camera.stopAcquisition();
        }

        // Turn off the chatbot message listener
        if(!ChatBoxController.userThread.isTerminated()){
            ChatBoxController.userThread.shutdown();

            try {
                ChatBoxController.userThread.awaitTermination(300, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("Shutting Down");
    }
}

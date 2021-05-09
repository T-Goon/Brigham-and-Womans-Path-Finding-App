package edu.wpi.cs3733.D21.teamB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.CSVHandler;
import edu.wpi.cs3733.D21.teamB.util.FileUtil;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.face.Camera;
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


@SuppressWarnings("deprecation")
public class App extends Application {

    private static Stage primaryStage;
    private DatabaseHandler db;
    private Thread dbThread;

    @Override
    public void init() {

        try {
            FileUtil.copy(getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/xml/lbpcascade_frontalface.xml"),
                    new File("").getAbsolutePath()+"/lbpcascade_frontalface.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileUtil.copy(getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/xml/haarcascade_frontalface_alt.xml"),
                    new File("").getAbsolutePath()+"/haarcascade_frontalface_alt.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        System.out.println("Starting Up");
        db = DatabaseHandler.getHandler();

    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/login/mainPage.fxml")));
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
                    SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/login/databaseInit.fxml");
                    primaryStage.show();

                    dbThread = new Thread(() -> {
                        try {
                            db.loadNodesEdges(CSVHandler.loadCSVNodes("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBnodes.csv"), CSVHandler.loadCSVEdges("/edu/wpi/cs3733/D21/teamB/csvFiles/bwBedges.csv"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> SceneSwitcher.switchFromTemp(getClass(), "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml"));
                    });
                    dbThread.start();
                } else primaryStage.show();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            try {
                HashMap<User, String> requiredUsers = new HashMap<>();

                //Required users
                requiredUsers.put(new User("admin", "Professor", "X", User.AuthenticationLevel.ADMIN, null), "admin");
                requiredUsers.put(new User("staff", "Mike", "Bedard", User.AuthenticationLevel.STAFF, null), "staff");
                requiredUsers.put(new User("guest", "T", "Goon", User.AuthenticationLevel.PATIENT, null), "guest");

                for (User u : requiredUsers.keySet()) {
                    if (db.getUserByUsername(u.getUsername()) == null) {
                        db.addUser(u, requiredUsers.get(u));
                    }
                }

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
        if (dbThread != null)
            dbThread.stop();
        DatabaseHandler.getHandler().shutdown();
        try {
            FileUtils.forceDelete(new File("haarcascade_frontalface_alt.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Turn off camera when
        if (Camera.cameraActive) {
            // release the camera
            Camera.stopAcquisition();
        }

        System.out.println("Shutting Down");
    }
}

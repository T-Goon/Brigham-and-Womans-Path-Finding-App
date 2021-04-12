package edu.wpi.teamB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import edu.wpi.teamB.util.CSVHandler;
import edu.wpi.teamB.database.DatabaseHandler;
import javafx.application.Application;
import javafx.application.Platform;
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

    private static final String NODES_PATH = new File("").getAbsolutePath() + "/src/main/resources/edu/wpi/teamB/csvfiles/MapBNodes.csv";
    private static final String EDGES_PATH = new File("").getAbsolutePath() + "/src/main/resources/edu/wpi/teamB/csvfiles/MapBEdges.csv";

    @Override
    public void init() throws SQLException {
        System.out.println("Starting Up");
        db = DatabaseHandler.getDatabaseHandler("main.db");

        // If the database is empty, fill it with the csv files
        // Note later we want to ask the user to give the location using a file chooser to the CSV files to load from
        if (!db.isInitialized())
            db.fillDatabase(CSVHandler.loadCSVNodes(Paths.get(NODES_PATH)), CSVHandler.loadCSVEdges(Paths.get(EDGES_PATH)));
    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(getClass().getResource("views/menus/patientDirectoryMenu.fxml"));

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
            Platform.exit();
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

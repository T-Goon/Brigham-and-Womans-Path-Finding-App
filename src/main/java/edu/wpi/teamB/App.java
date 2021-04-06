package edu.wpi.teamB;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  private static Stage primaryStage;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) {
    App.primaryStage = primaryStage;

        // Open first view
        try {
            Parent root = FXMLLoader.load(getClass().getResource("views/serviceRequestMenu.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
//            primaryStage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

  public static Stage getPrimaryStage(){
    return primaryStage;
  }

    public static void setPrimaryStage(Stage newPrimaryStage) {
        primaryStage = newPrimaryStage;
    }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}

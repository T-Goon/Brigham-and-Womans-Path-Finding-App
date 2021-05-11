package edu.wpi.cs3733.D21.teamB.games.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class gameFrame extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static Stage stage;
    private static gamePanel start;
    private static Scene startScene;
    private static Scene displayScene;

    public gameFrame() {
        start = new gamePanel();
        startScene = new Scene(start, WIDTH, HEIGHT);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameFrame.stage = primaryStage;
        stage.setTitle("Snake");
        stage.setResizable(false);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setScene(startScene);
        stage.show();
    }
//    gameFrame() {
//        this.add(new gamePanel());
//        this.setTitle("Snake");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setResizable(false);
//        this.pack();
//        this.setVisible(true);
//        this.setLocationRelativeTo(null);
//    }

}

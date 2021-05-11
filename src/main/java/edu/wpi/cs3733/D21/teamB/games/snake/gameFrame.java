package edu.wpi.cs3733.D21.teamB.games.snake;

import javax.swing.*;

public class gameFrame extends JFrame {
    public gameFrame() {
        this.add(new gamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setAutoRequestFocus(true);
        this.requestFocus();
        this.requestFocusInWindow();
    }
}

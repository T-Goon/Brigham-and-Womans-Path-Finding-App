package edu.wpi.cs3733.D21.teamB.games.breakout;

import lombok.var;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class Breakout extends JFrame {

    public Breakout() {
        
        initUI();
    }
    
    private void initUI() {

        add(new Board());
        setTitle("Breakout");

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setAlwaysOnTop(true);
        pack();
    }
//
//    public static void main(String[] args) {
//
//        EventQueue.invokeLater(() -> {
//
//            var game = new Breakout();
//            game.setVisible(true);
//        });
//    }
}

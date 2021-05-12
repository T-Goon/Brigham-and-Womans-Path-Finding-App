package edu.wpi.cs3733.D21.teamB.games.PacMan;

import javax.swing.*;

public class Pacman extends JFrame {

    public Pacman() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("Pacman");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(380, 420);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
    }
}

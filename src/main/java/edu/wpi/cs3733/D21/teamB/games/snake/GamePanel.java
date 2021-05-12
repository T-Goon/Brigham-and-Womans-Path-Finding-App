package edu.wpi.cs3733.D21.teamB.games.snake;

import edu.wpi.cs3733.D21.teamB.App;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int boxSize = 25;
    static final int gameUnits = (screenWidth * screenHeight) / boxSize;
    static final int delay = 75;
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int snakeBody = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    static boolean gameOn = false;
    private final JFrame frame;

    GamePanel(JFrame frame) {
        this.frame = frame;
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        placeNewApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void pauseGame() {
        GamePanel.gameOn = true;
        timer.stop();
    }

    public void resumeGame() {
        GamePanel.gameOn = false;
        timer.start();
    }

    public void paintComponent(Graphics g) {
        if (!App.isRunning()) {
            frame.dispose();
        }

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {


        if (running) {
            //start of drawing grid lines
            for (int i = 0; i < screenHeight / boxSize; i++) {
                g.drawLine(i * boxSize, 0, i * boxSize, screenHeight);
                g.drawLine(0, i * boxSize, screenWidth, i * boxSize);
            }
            //end of drawing grid lines
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, boxSize, boxSize);

            for (int i = 0; i < snakeBody; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], boxSize, boxSize);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], boxSize, boxSize);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            checkGameOver(g);
        }

    }

    public void placeNewApple() {
        appleX = random.nextInt((int) (screenWidth / boxSize)) * boxSize;
        appleY = random.nextInt((int) (screenHeight / boxSize)) * boxSize;

    }

    public void snakeMove() {
        for (int i = snakeBody; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - boxSize;
                break;
            case 'D':
                y[0] = y[0] + boxSize;
                break;
            case 'L':
                x[0] = x[0] - boxSize;
                break;
            case 'R':
                x[0] = x[0] + boxSize;
                break;
        }

    }

    public void isAppleEaten() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeBody++;
            applesEaten++;
            placeNewApple();
        }

    }

    public void checkCollisions() {
        //checks if head collides with body
        for (int i = snakeBody; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //checks if head touches right border
        if (x[0] > screenWidth) {
            running = false;
        }
        //checks if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //checks if head touches bottom border
        if (y[0] > screenHeight) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void checkGameOver(Graphics g) {
        //Score text
        g.setColor(Color.red);
        g.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (screenWidth - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screenWidth - metrics2.stringWidth("Game Over")) / 2, screenHeight / 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            snakeMove();
            isAppleEaten();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (GamePanel.gameOn) {
                        resumeGame();
                    } else {
                        pauseGame();
                    }
            }

        }
    }


}

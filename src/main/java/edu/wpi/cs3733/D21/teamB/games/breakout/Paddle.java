package edu.wpi.cs3733.D21.teamB.games.breakout;

import lombok.var;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Paddle extends Sprite  {

    private int dx;

    public Paddle() {
        
        initPaddle();        
    }
    
    private void initPaddle() {

        loadImage();
        getImageDimensions();

        resetState();
    }
    
    private void loadImage() {
        try {
            InputStream stream = getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/games/breakout/paddle.png");
            var ii = new ImageIcon(ImageIO.read(stream));
            image = ii.getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    void move() {

        x += dx;

        if (x <= 0) {

            x = 0;
        }

        if (x >= Commons.WIDTH - imageWidth) {

            x = Commons.WIDTH - imageWidth;
        }
    }

    void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 1;
        }
    }

    void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }

    private void resetState() {

        x = Commons.INIT_PADDLE_X;
        y = Commons.INIT_PADDLE_Y;
    }
}

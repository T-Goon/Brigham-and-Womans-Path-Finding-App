package edu.wpi.cs3733.D21.teamB.games.breakout;

import lombok.var;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.IOException;
import java.io.InputStream;

public class Ball extends Sprite {

    private int xdir;
    private int ydir;

    public Ball() {

        initBall();
    }

    private void initBall() {
        
        xdir = 1;
        ydir = -1;

        loadImage();
        getImageDimensions();
        resetState();
    }

    private void loadImage() {
        try {
            InputStream stream = getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/games/breakout/ball.png");
            var ii = new ImageIcon(ImageIO.read(stream));
            image = ii.getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void move() {

        x += xdir;
        y += ydir;

        if (x == 0) {

            setXDir(1);
        }

        if (x == Commons.WIDTH - imageWidth) {
            setXDir(-1);
        }

        if (y == 0) {

            setYDir(1);
        }
    }

    private void resetState() {

        x = Commons.INIT_BALL_X;
        y = Commons.INIT_BALL_Y;
    }

    void setXDir(int x) {

        xdir = x;
    }

    void setYDir(int y) {

        ydir = y;
    }

    int getYDir() {

        return ydir;
    }
}

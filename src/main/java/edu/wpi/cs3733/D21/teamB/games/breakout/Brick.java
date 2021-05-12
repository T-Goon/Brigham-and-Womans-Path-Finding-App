package edu.wpi.cs3733.D21.teamB.games.breakout;

import lombok.var;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.IOException;
import java.io.InputStream;

public class Brick extends Sprite {

    private boolean destroyed;

    public Brick(int x, int y) {
        
        initBrick(x, y);
    }
    
    private void initBrick(int x, int y) {
        
        this.x = x;
        this.y = y;
        
        destroyed = false;

        loadImage();
        getImageDimensions();
    }
    
    private void loadImage() {
        try {
            InputStream stream = getClass().getResourceAsStream("/edu/wpi/cs3733/D21/teamB/games/breakout/brick.png");
            var ii = new ImageIcon(ImageIO.read(stream));
            image = ii.getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isDestroyed() {
        
        return destroyed;
    }

    void setDestroyed(boolean val) {
        
        destroyed = val;
    }
}

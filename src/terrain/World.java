package terrain;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;

public class World {

    public final static int WL = 2500;
    public final static int WH = 1500;

    private static final Color worldColor = new Color(0, 225, 8);

    // Render Chunk
    public void render(Graphics g) {

        if (!Game.isPaused) {
            g.setColor(worldColor);
        }
        else {
            g.setColor(Color.GRAY);
        }

        g.fillRect(0, 0, WL, WH);

    }

}

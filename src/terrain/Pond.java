package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import main.Game;

public class Pond {

    private int pondLength = 200;
    private int pondHeight = 200;

    // Render Chunk
    public void render(Graphics g) {

        if (!Game.isPaused) {
            g.setColor(Color.BLUE);
        }
        else {
            g.setColor(Color.DARK_GRAY);
        }

        g.fillOval((World.WL / 2) - (pondLength / 2),
                (World.WH / 2) - (pondHeight / 2), pondLength, pondHeight);

    }

    public static Point findWater() {
        return new Point(World.WL / 2, World.WH / 2);
    }

}

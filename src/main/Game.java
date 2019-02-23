package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.Blob;
import terrain.Pond;
import terrain.World;

public class Game extends JPanel implements Runnable {

    private static final long serialVersionUID = 144L;

    public static final int WIDTH = 2500;
    public static final int HEIGHT = 1500;
    public static final int SCALE = 1;
    public static final int tick = 16; // 16

    private static boolean isRunning;

    private static World world = new World();
    private static Pond pond = new Pond();
    private static TextArea textArea = new TextArea();

    // Pause
    public static boolean isPaused = false;
    public static long gameTime = System.currentTimeMillis();

    private static Game gameComponent = new Game();
    private static JFrame frame = new JFrame();

    private static ArrayList<Blob> theBlobs = Blob.blobs;

    public Game() {
        setFocusable(true);
        requestFocus();
        start();
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        isRunning = false;
    }

    public void run() {

        long timeStartLoop = 0;
        long timeEndLoop = 0;

        while (isRunning) {

            try {

                // Game Time Stuff
                timeStartLoop = System.currentTimeMillis();

                // Update
                if (!isPaused) {
                    // Blobs
                    Blob.updateAverages(); // <--- somewhat inefficient
                    for (int i = 0; i < theBlobs.size(); i++) {
                        Blob b = theBlobs.get(i);
                        b.update();
                    }
                }
                // Text Area
                textArea.update();

                repaint();

                Thread.sleep(tick);

                timeEndLoop = System.currentTimeMillis();

                if (!isPaused) {
                    gameTime = gameTime + timeEndLoop - timeStartLoop;
                }
            }
            catch (Exception e) {
                System.out.println("EXCEPTION: " + e.toString());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Paint World
        world.render(g);
        pond.render(g);

        // Paint Blobs
        for (int i = 0; i < theBlobs.size(); i++) {
            Blob b = theBlobs.get(i);
            b.render(g);
        }

        // Paint Text Area
        textArea.render(g);
    }

    // Icons
    static Image icon32 = new ImageIcon("/res/icon32.png").getImage();
    static Image icon16 = new ImageIcon("/res/icon16.png").getImage();

    public static void main(String[] args) {
        Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        frame.setTitle("Blob Pond");
        frame.setVisible(true);
        frame.setSize(size);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameComponent);

        // Icons
        List<Image> icons = new ArrayList<Image>();
        icons.add(icon32);
        icons.add(icon16);
        frame.setIconImages(icons);

        // Keyboard Input
        frame.addKeyListener(new KeyboardInput());

        // Spawn Blobs
        Blob.reset();
    }

}

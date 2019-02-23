package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import main.Game;
import terrain.Pond;
import terrain.World;

public class Blob {

    private static final int startingNumberOfBlobs = 4;

    public static ArrayList<Blob> blobs = new ArrayList<Blob>();

    private static long startTime;

    protected static double averageBoldness;
    protected static double averageDivideChance;
    protected static double averageMaxSize;
    protected static double averageDivideSize;

    protected int lifespan = 0;
    protected int generation = 1;

    // Buttons
    public static boolean showGeneration = false;

    protected int size;
    protected int speed;

    protected Point velocity;
    protected Point location;

    protected double trackWaterChance;
    protected double divideChance;
    protected double maxSize;
    protected double divideSize;

    protected boolean sick;
    protected int sickAge;

    // Load Blob From File
    public Blob(int lol) {
        // TODO
    }

    // Spawned
    public Blob() {

        Random rX = new Random();
        int locX = rX.nextInt(World.WL);

        Random rY = new Random();
        int locY = rY.nextInt(World.WH);

        trackWaterChance = 40.0;
        divideChance = 10.0;
        divideSize = 15;
        maxSize = 50;

        location = new Point(locX, locY);
        velocity = new Point(0, 0);
        size = 25;

        sick = false;

        blobs.add(this);

    }

    // Born
    public Blob(Blob b) {

        // NEW STATS //
        // Boldness
        double newTrackWaterChance = b.trackWaterChance + (5 * plusOrMinus());
        if (newTrackWaterChance < 100 && newTrackWaterChance > 0) {
            trackWaterChance = newTrackWaterChance;
        }
        else {
            trackWaterChance = b.trackWaterChance;
        }

        // Divide Chance
        double newDivideChance = b.divideChance + (5 * plusOrMinus());
        if (newDivideChance < 100 && newDivideChance > 0) {
            divideChance = newDivideChance;
        }
        else {
            divideChance = b.divideChance;
        }

        // Max Size
        double newMaxSize = b.maxSize + (1 * plusOrMinus());
        if (newMaxSize < 100 && newMaxSize > 8) {
            maxSize = newMaxSize;
        }
        else {
            maxSize = b.maxSize;
        }

        // Divide Size
        double newDivideSize = b.divideSize + (2 * plusOrMinus());
        if (newDivideSize < 100 && newDivideSize > 15) {
            divideSize = newDivideSize;
        }
        else {
            divideSize = b.divideSize;
        }

        ///////////////

        generation = b.generation + 1;
        location = new Point(b.location.x, b.location.y);
        velocity = new Point(0, 0);

        if (b.size < 20) {
            sick = b.sick;
        }
        else {
            sick = false;
        }

        if (b.size > 20) {
            size = b.size - 10;
        }
        else {
            size = b.size / 2;
        }
        b.size = size;

        blobs.add(this);
    }

    public void update() {

        lifespan++;

        if (lifespan % 16 == 0) {
            chooseVel();
            chooseDiv();
            drink();
            Virus.infect(this);
        }

        if (lifespan % 2000 == 0) {
            hunger();
        }

        move();
    }

    public static void updateAverages() {

        double sumBoldness = 0.0;
        double sumDivideChance = 0.0;
        double sumMaxSize = 0.0;
        double sumDivideSize = 0.0;

        for (Blob b : blobs) {
            sumBoldness += b.trackWaterChance;
            sumDivideChance += b.divideChance;
            sumMaxSize += b.maxSize;
            sumDivideSize += b.divideSize;
        }

        int numberOfBlobs = blobs.size();

        averageBoldness = (sumBoldness / numberOfBlobs);
        averageDivideChance = (sumDivideChance / numberOfBlobs);
        averageMaxSize = (sumMaxSize / numberOfBlobs);
        averageDivideSize = (sumDivideSize / numberOfBlobs);
    }

    public void move() {

        int newX = location.x + velocity.x;
        int newY = location.y + velocity.y;

        if (newX > size / 2 && newX < World.WL - size / 2) {
            location.x = newX;
        }
        if (newY > size / 2 && newY < World.WH - size / 2) {
            location.y = newY;
        }

    }

    public void chooseVel() {

        Random r = new Random();
        int chance = r.nextInt(100);

        // Track Water
        if (chance < trackWaterChance) {
            trackWater();
        }
        else if (chance > trackWaterChance) {
            runFromClosestBlob();
        }
        // Do Nothing
        else {
            velocity.x = 0;
            velocity.y = 0;
        }

    }

    public void chooseDiv() {

        Random r = new Random();
        int chance = r.nextInt(100);

        if (chance < divideChance && lifespan > 200 && size > divideSize) {

            Blob child = new Blob(this);
        }

    }

    protected void kill() {
        blobs.remove(this);
    }

    protected void drink() {

        if (isWithin(Pond.findWater(), 50) && size < maxSize) {
            size++;

            Random r = new Random();
            int chance = r.nextInt(100);

            // Overpopulation
            if (chance < 1 && blobs.size() > 200) {
                sick = true;
                sickAge = lifespan;
            }
        }

    }

    protected Blob getClosestBlob() {

        Blob closestBlob = null;

        for (Blob b : blobs) {
            if (this != b && (closestBlob == null
                    || getDistance(b) < getDistance(closestBlob))) {
                closestBlob = b;
            }
        }

        return closestBlob;
    }

    protected double getDistance(Blob b) {
        return Math.sqrt(Math.pow(location.x - b.location.x, 2)
                + Math.pow(location.y - b.location.y, 2));
    }

    protected void trackWater() {
        // Thirsty Check
        if (size < 20) {
            // Track Water Hori
            if (Pond.findWater().x > location.x) {
                velocity.x = 1;
            }
            else {
                velocity.x = -1;
            }

            // Track Water Vert
            if (Pond.findWater().y > location.y) {
                velocity.y = 1;
            }
            else {
                velocity.y = -1;
            }
        }
        else {
            // Move Randomly
            setRandomVel();
        }
    }

    protected void runFromClosestBlob() {

        // Size Check
        if (blobs.size() <= 1) {
            setRandomVel();
            return;
        }

        Blob cBlob = getClosestBlob();

        Point cBlobLoc = cBlob.location;

        // Scare Distance
        if (getDistance(cBlob) < 50) {
            // Run Hori
            if (cBlobLoc.x > location.x) {
                velocity.x = -1;
            }
            else {
                velocity.x = 1;
            }

            // Run Vert
            if (cBlobLoc.y > location.y) {
                velocity.y = -1;
            }
            else {
                velocity.y = 1;
            }
        }
    }

    public void setRandomVel() {

        Random vX = new Random();
        int velX = vX.nextInt(2);

        if (velX > 0) {
            velocity.y = 1 * plusOrMinus();
        }
        else {
            velocity.x = 0;
        }

        Random vY = new Random();
        int velY = vY.nextInt(2);

        if (velY > 0) {
            velocity.y = 1 * plusOrMinus();
        }
        else {
            velocity.y = 0;
        }

    }

    protected void hunger() {
        if (size > 4) {
            size--;
        }
        else {
            kill();
        }
    }

    protected int plusOrMinus() {
        Random r = new Random();
        int rI = r.nextInt(100);

        if (rI > 50) {
            return 1;
        }
        else {
            return -1;
        }
    }

    // Render Blob
    private static final Color blobColor = new Color(180, 50, 255);
    private static final Color blobHungryColor = new Color(255, 50, 255);
    private static final Color blobSickColor = new Color(0, 255, 0);

    public void render(Graphics g) {

        // The Blob Itself
        g.setColor(Color.BLACK);
        g.fillOval(location.x - size / 2, location.y - size / 2, size, size);

        // Normal
        if (!Game.isPaused) {
            if (!sick) {
                // Hungry
                if (size < 15) {
                    g.setColor(blobHungryColor);
                }
                else {
                    g.setColor(blobColor);
                }
            }
            // Sick
            else {
                g.setColor(blobSickColor);
            }
        }
        else {
            // Paused
            g.setColor(Color.DARK_GRAY);
        }

        g.fillOval(location.x - size / 2 + 2, location.y - size / 2 + 2,
                size - 4, size - 4);

        // Generation Number

        if (showGeneration) {
            Font font = new Font("TimesRoman", Font.PLAIN, 25);

            g.setColor(Color.BLACK);
            g.setFont(font);

            int stringWidth = g.getFontMetrics()
                    .stringWidth(Integer.toString(generation));
            g.drawString(Integer.toString(generation),
                    location.x - stringWidth / 2, location.y - size);
        }

    }

    // isWithin Helper
    protected boolean isWithin(Point p, int r) {

        if ((location.x > (p.x - r)) && (location.x < (p.x + r))
                && (location.y > (p.y - r)) && (location.y < (p.y + r))) {
            return true;
        }
        else {
            return false;
        }

    }

    // Reset Blobs
    public static void reset() {
        blobs.clear();

        for (int i = 0; i < startingNumberOfBlobs; i++) {
            new Blob();
        }

        startTime = Game.gameTime;
    }

    public static String getSurvivalTime() {

        long survivalTimeSeconds = (Game.gameTime - startTime) / 1000;

        int hours = (int) (survivalTimeSeconds / 3600);
        survivalTimeSeconds = survivalTimeSeconds % 3600;
        int minutes = (int) (survivalTimeSeconds / 60);
        survivalTimeSeconds = survivalTimeSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes,
                survivalTimeSeconds);
    }

    // GETTERS AND SETTERS
    public static double getAverageBoldness() {
        return averageBoldness;
    }

    public static double getAverageDivideChance() {
        return averageDivideChance;
    }

    public static double getAverageMaxSize() {
        return averageMaxSize;
    }

    public static double getDivideSize() {
        return averageDivideSize;
    }
}

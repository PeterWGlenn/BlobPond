package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.Blob;

public class TextArea {

    public static boolean displayTextArea = true;

    private final int fromTheLeft = 25;
    private final int fromTheTop = 25;
    private final int textHeight = 50;
    private final int textSpacing = 75;

    private String textSurvivalTime = "";
    private String textNumberOfBlobs = "";
    private String textBoldness = "";
    private String textDivideChance = "";
    private String textMaxSize = "";
    private String textDivideSize = "";

    public void update() {

        if (Blob.blobs.size() > 0) {
            textSurvivalTime = "Survived: " + Blob.getSurvivalTime();
        }

        textNumberOfBlobs = "Blobs: " + Blob.blobs.size();
        textBoldness = "Boldness: "
                + String.format("%.1f", Blob.getAverageBoldness()) + "%";
        textDivideChance = "Divide Chance: "
                + String.format("%.1f", Blob.getAverageDivideChance()) + "%";
        textMaxSize = "Max Size: "
                + String.format("%.1f", Blob.getAverageMaxSize()) + "%";
        textDivideSize = "Divide Size: "
                + String.format("%.1f", Blob.getDivideSize());
    }

    // Render Text Area
    public void render(Graphics g) {

        if (displayTextArea) {
            Font font = new Font("TimesRoman", Font.PLAIN, 45);

            g.setColor(Color.BLACK);
            g.setFont(font);

            g.drawString(textSurvivalTime, fromTheLeft,
                    fromTheTop + textHeight);

            g.drawString(textNumberOfBlobs, fromTheLeft,
                    fromTheTop + textHeight + textSpacing);
            g.drawString(textBoldness, fromTheLeft,
                    fromTheTop + textHeight + textSpacing * 2);
            g.drawString(textDivideChance, fromTheLeft,
                    fromTheTop + textHeight + textSpacing * 3);
            g.drawString(textMaxSize, fromTheLeft,
                    fromTheTop + textHeight + textSpacing * 4);
            g.drawString(textDivideSize, fromTheLeft,
                    fromTheTop + textHeight + textSpacing * 5);
        }
    }

}

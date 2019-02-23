package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.Blob;

public class KeyboardInput extends KeyAdapter implements KeyListener {

    public KeyboardInput() {
        // Empty
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int c = e.getKeyCode();

        // Pause the Game
        if (c == KeyEvent.VK_P || c == KeyEvent.VK_ESCAPE) {
            if (Game.isPaused) {
                Game.isPaused = false;
            }
            else {
                Game.isPaused = true;
            }
        }

        // Reset
        if (c == KeyEvent.VK_R) {
            if (Game.isPaused) {
                Blob.reset();
            }
        }

        // Clear
        if (c == KeyEvent.VK_C) {
            if (Game.isPaused) {
                Blob.blobs.clear();
            }
        }

        // Toggle Blob Generation Visibility
        if (c == KeyEvent.VK_G) {
            if (Blob.showGeneration) {
                Blob.showGeneration = false;
            }
            else {
                Blob.showGeneration = true;
            }
        }

        // Toggle Text Area Visibility
        if (c == KeyEvent.VK_D) {
            if (TextArea.displayTextArea) {
                TextArea.displayTextArea = false;
            }
            else {
                TextArea.displayTextArea = true;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        int c = e.getKeyCode();

        // TODO
    }

}

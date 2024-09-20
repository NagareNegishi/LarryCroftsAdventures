package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;

import java.awt.Color;

/**
 * This is the class that handles all the rendering logic, 
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 * 
 */


/**to get things started, I should have a way to identify the names or the images i'm going to load
 * I should make a canvas thing to start, on launch
 * then, i can have an update method.
 * 
 * there are a few ways to make this efficient, generally speaking, using enums for singlton is the best
 * there should be a loop, to draw everything around the player, with them being centered
 * for now, I can hardcode values in, but it's much better to use the img's uniform width of sorts
 * 
 * I should assume that Jpanel will run things well on the GPU for now.
 * for now, let's get a jPanel going with a canvas to draw on, then an image
 * after which. let's see the image move whenever there's some sort of update happening
 * 
 */



import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Renderer extends JPanel {

    private final int FPS = 60; // 60 frames per second
    private final int frameTime = 1000 / FPS; // time per frame in milliseconds

    public Renderer() {
        // Timer for updating the canvas every frame
        Timer timer = new Timer(frameTime, e -> updateCanvas());
        timer.start();
    }

    public void updateCanvas() {
       repaint();
       //this, will call paintComponent after it's done painting the last frame?
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        g.setColor(Color.RED);
        
        // Clear the canvas
        
        //run the logic of working in "local space, of just rendering stuff around the player and outwards"
        //on saturday, Make the Icons needed for this whole thing to work, and to actually test the Img class
 
        
        g.fillRect(50, 50, 100, 100); // You can replace this with your custom rendering
    }
}

package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Img class using Singleton pattern to load and cache images.
 * The drawing functionality from RenderImg has been merged here for simplicities sake. 
 * as the main reason for RenderImg was to initlize everything on startup
 */
public class Img {

    private static Img instance = null;

    // Cache of loaded images
    private Map<String, BufferedImage> images = new HashMap<>();

    public final int imgSize = 48;

    
    private Img() {
        loadAllImages();
    }

    public static Img getInstance() {
        if (instance == null) {
            instance = new Img();
        }
        return instance;
    }

    
    /**
     * get the Image's name
     * eg. Img.instance
     * 
     * @param name
     * @return
     */
    public BufferedImage get(String name) {
        if (!images.containsKey(name)) {
            throw new IllegalArgumentException("Image " + name + " not found!");
        }
        return images.get(name);
    }

    
    /**
     * Loads all images on startup to avoid depressing load times
     * 
     */
    private void loadAllImages() {
        String[] imageNames = {
            "FreeTile", "LockedDoor_blue", "LockedDoor_red", "Wall_Tile", "intro1", "intro2", "InfoTile",
            "Stairs", "tp1", "tp2", "tp3", "ExitLock", "ExitOpen", "Treasure1", "Treasure2", "Treasure3",
            "Treasure4", "Red_key", "Blue_key", "chap1", "chap2", "Actor1", "Actor2", "Water1", "Water2",
            "Water3", "Water4", "Water5", "Water6", "Water7", "Kourie"
        };

        for (String name : imageNames) {
            images.put(name, loadImage(name));
        }
    }

    
    private BufferedImage loadImage(String name) {
        Path p = getImagePath(name);
        assert Files.exists(p) : "Image " + name + " not found at path: " + p.toString();
        try {
            return ImageIO.read(p.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    
    private static Path getImagePath(String imageName) {
        return startPath().resolve(imageName + ".png");  // Keep using Path to resolve
    }

    private static Path startPath() {
        return Paths.get(System.getProperty("user.dir"), "src", "imgs");
    }


    // Drawing methods merged from RenderImg

    /**
     * it recives an Image with a string to get from the hashset, and draws it as specified to the screen space
     * 
     * @param g
     * @param name
     * @param x
     * @param y
     */
    public void drawImg(Graphics g, String name, int x, int y) {
        BufferedImage image = get(name);
        int w1 = x - imgSize / 2;
        int h1 = y - imgSize / 2;
        int w2 = w1 + imgSize;
        int h2 = h1 + imgSize;

        g.drawImage(image, w1, h1, w2, h2, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    
    /**
     * Renders an Image in a specific place for the info tile itself
     * 
     * @param g
     * @param name
     * @param d
     */
    public void drawInfoTile(Graphics g, String name, Dimension d) {
        BufferedImage image = get(name);
        int x1 = d.width / 5;
        int y1 = d.height * 3 / 5;
        int x2 = d.width - d.width / 5;
        int y2 = d.height;

        g.drawImage(image, x1, y1, x2, y2, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    
    /**
     * This is not being used anymore
     * 
     * @param g
     * @param name
     * @param d
     */
    public void drawBg(Graphics g, String name, Dimension d) {
        BufferedImage image = get(name);
        g.drawImage(image, 0, 0, d.width, d.height, 0, 0, image.getWidth(), image.getHeight(), null);
    }
}

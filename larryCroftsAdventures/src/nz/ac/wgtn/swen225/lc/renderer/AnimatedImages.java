package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Graphics;
import java.util.List;

/**
 * Animated Images takes in a List of RenderImg, and cycles through the list to animate it all
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 */
public class AnimatedImages {

    private List<RenderImg> frames;
    private int currentFrame; // Index of the current frame

    public AnimatedImages(List<RenderImg> frames) {
        this.frames = frames;
        this.currentFrame = 0; 
    }

    public void draw(Graphics g, int x, int y) {
    	
        RenderImg currentImage = check();
        currentImage.drawImg(g, x, y); 
    }

    private RenderImg check() {
        RenderImg currentImage = frames.get(currentFrame); 
        
        //Recycled CGRA code goes crazy
        currentFrame = (currentFrame + 1) % frames.size(); 
        return currentImage;
    }
}

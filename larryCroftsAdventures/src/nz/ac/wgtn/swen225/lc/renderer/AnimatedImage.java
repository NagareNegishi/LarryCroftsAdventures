package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Graphics;
import java.util.List;



/**
 * Animated Image Accepts a List of RenderImg, it then goes through that list and rendering
 * the next one in the list, and going back to the first item once the list has been exhausted.
 * 
 * It supports only having one Frame as well, that's no issue.
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 */

public class AnimatedImage {

    private List<RenderImg> frames;
    private int currentFrame;

    public AnimatedImage(List<RenderImg> frames) {
        this.frames = frames;
        this.currentFrame = 0; 
    }

    
    public void draw(Graphics g, int x, int y) {
        // Call the check method to get the current frame and update the frame index
        RenderImg currentImage = check();
        currentImage.drawImg(g, x, y); 
    }
    
    
    /**
     * This is specifically for backgrounds
     */
    public void drawBackground(Graphics g, int x, int y) {
    	
    	
    }
    
    private RenderImg check() {
        RenderImg currentImage = frames.get(currentFrame); 

        //Hooray for Cgra
        currentFrame = (currentFrame + 1) % frames.size(); 

        // Return the current frame to be drawn
        return currentImage;
    }
}

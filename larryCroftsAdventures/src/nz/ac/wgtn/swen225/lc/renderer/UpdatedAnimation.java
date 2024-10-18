package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Graphics;

/**
 * UpdatedAnimation handles animations by cycling through multiple image frames.
 * It uses Img class to load the frames dynamically.
 * 
 * frameDuratation is hardcoded with 30fps in mind.
 * 
 * 
 * @author Marwan Mohamed
 * @studentID 300653693
 */
public class UpdatedAnimation {

    private String baseName;     // Base name of the animation (e.g., "Actor")
    private int frameCount;      
    private int currentFrame;    
    private int frameTimer;      // scales the animation to 30 fps
    private int frameDuration;   // How long each frame should be displayed 

    
    /**
     * The constructor accepts a basename to iterate through, a frameCount, for the number of frames total
     * AnimationFps is the framerate of the animation, i.e 24fps, 2fps, etc etc.
     * 
     * eg. in game files there's Actor1, Actor2. so we make
     * new UpdatedAnimation("Actor",2,1)
     * 
     * 
     * @param baseName
     * @param frameCount
     * @param animationFps
     */
    public UpdatedAnimation(String baseName, int frameCount, int frameRate) {
        this.baseName = baseName;
        this.frameCount = frameCount;
        this.currentFrame = 1;  // Start at the first frame (1)
        this.frameTimer = 0;    // Start the frame timer
        this.frameDuration = 30 / frameRate; 
    }

    
    public void updateFrame() {
        frameTimer++;  // Increment the frame timer
        
        // If the timer reaches the duration, update the frame
        if (frameTimer >= frameDuration) {
            frameTimer = 0;  // Reset the frame timer
            currentFrame = (currentFrame % frameCount) + 1;  
        }
    }

    // Draw the current frame
    public void draw(Graphics g, int x, int y) {
        updateFrame();  // Update the frame before drawing
        String frameName = baseName + currentFrame;  // e.g., "Actor_walk_1"
        Img.getInstance().drawImg(g, frameName, x, y);  // Use the Img class to draw the current frame
    }
}

package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Dimension;
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
    private int frameDuration; // How long each frame should last in terms of 30fps updates
    private int frameCount;    // Counter to track the frame update timing

    /**
     * Constructor for AnimatedImage.
     * @param frames the list of frames to animate
     * @param animationFps the desired frame rate for the animation (e.g., 2 FPS)
     * @param systemFps the frame rate of the system (e.g., 30 FPS)
     */
    public AnimatedImage(List<RenderImg> frames, int animationFps) {
        this.frames = frames;
        this.currentFrame = 0;
        this.frameCount = 0;
        this.frameDuration = 30 / animationFps; // Calculate how long each frame should be displayed
    }

    /**
     * Draws the current frame at the given coordinates.
     * @param g the Graphics context to draw on
     * @param x the x position
     * @param y the y position
     */
    public void draw(Graphics g, int x, int y) {
        RenderImg currentImage = check();
        currentImage.drawImg(g, x, y); 
    }

    /**
     * Draw the current frame as a background.
     * @param g the Graphics context to draw on
     * @param dimension the dimensions of the background area
     */
    public void drawBg(Graphics g, Dimension dimension) {
        RenderImg currentImage = check();
        currentImage.drawBg(g, dimension); 
    }

    /**
     * Determines the current frame to be displayed based on frame timing.
     * @return the current RenderImg frame
     */
    private RenderImg check() {
        // If frameCount exceeds the frameDuration, move to the next frame
        if (frameCount >= frameDuration) {
            frameCount = 0; // Reset the frame count
            currentFrame = (currentFrame + 1) % frames.size(); // Move to the next frame
        }
        frameCount++; // Increment the frame count on each tick

        // Return the current frame
        return frames.get(currentFrame);
    }
}

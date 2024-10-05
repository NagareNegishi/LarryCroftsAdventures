package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RenderImg {
    private final Img img;
    private boolean isOut;
    
    private int w1;
    private int h1;
    private int w2;
    private int h2;
    private int imgSize = 48; 
    
    Dimension canvas;
    //This is the img Size

    public RenderImg(Img img) {
        this.img = img;
    }
    
    public int size() {
    	return imgSize;
    }

    public void drawChap(Graphics g, Point center, Dimension canvas) {
        BufferedImage image = img.image;//there's an optimization that can be done here to remove redundancies.
        this.canvas = canvas;
        // Get the actual width and height of the image
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        
        // Calculate the drawing coordinates
        int w1 = center.x -imgSize/2; 
        int h1 = center.y -imgSize/2;
        int w2 = w1 + imgSize;
        int h2 = h1 + imgSize;

        // Check if the image is outside the visible area
        testIfOut(canvas);
        if (!isOut) {
            return;
        }
        
        g.drawImage(image, w1, h1, w2, h2, 0, 0, imageWidth, imageHeight, null);
 
    }
    
    //doesn't actually do anything, this check should be looked over again
    public void testIfOut(Dimension size) {
    	isOut = h2 <= 0 || w2 <= 0 || h1 >= size.height || w1 >= size.width;
        
    }
    
    public void drawImg(Graphics g , int x, int y) {
    	//this will draw the img where it needs to be
        BufferedImage image = img.image;//there's an optimization that can be done here to remove redundancies.

        // Get the actual width and height of the image
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        
        // Calculate the drawing coordinates
        int w1 = x -imgSize/2; 
        int h1 = y -imgSize/2;
        int w2 = w1 + imgSize;
        int h2 = h1 + imgSize;

        // Check if the image is outside the visible area
        testIfOut(canvas);
        if (!isOut) {
            return;
        }
        
        g.drawImage(image, w1, h1, w2, h2, 0, 0, imageWidth, imageHeight, null);
 
    }
    
    
}

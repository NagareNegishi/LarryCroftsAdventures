package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RenderImg {
    private final Img img;
    
    private int w1;
    private int h1;
    private int w2;
    private int h2;

    public RenderImg(Img img) {
        this.img = img;
    }

    public void drawChap(Graphics g, Point center, Dimension size) {
        BufferedImage image = img.image;//there's an optimization that can be done here to remove redundancies.

        // Get the actual width and height of the image
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        
        // Calculate the drawing coordinates
        int w1 = center.x - imageWidth / 2; // Center the image horizontally
        int h1 = center.y - imageHeight / 2; // Center the image vertically
        int w2 = w1 + imageWidth;
        int h2 = h1 + imageHeight;

        // Check if the image is outside the visible area
        testIfOut(size);

        // Draw the image
        
        g.drawImage(image, w1, h1, w2, h2, 0, 0, imageWidth, imageHeight, null);
        g.setColor(Color.ORANGE);
        g.fillOval(size.width-20, size.height-20, 40, 40);
        g.setColor(Color.BLUE);
        g.fillOval(imageWidth-20, (imageHeight/2)-20, 40, 40);
    }
    
    
    public void testIfOut(Dimension size) {
    	boolean isOut = h2 <= 0 || w2 <= 0 || h1 >= size.height || w1 >= size.width;
        if (isOut) {
            return;
        }
    }
    
    public void drawImg(Graphics g , int x, int y) {
    	//this will draw the img where it needs to be
    	BufferedImage image = img.image;
    	
    	int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        
        int size = imageWidth;
        
    	int drawWidth = imageWidth * size;
    	int drawHeight = imageHeight * size;
    	
    	w2 = drawWidth + size;
    	h2 = drawHeight + size;
    	
    	g.drawImage(image, drawWidth, drawHeight, w2, h2, 0, 0, imageWidth, imageHeight, null);
    	
    }
    
    
}

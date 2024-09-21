package nz.ac.wgtn.swen225.lc.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RenderImg {
    private final Img img;

    public RenderImg(Img img) {
        this.img = img;
    }

    public void draw(Graphics g, Point center, Dimension size) {
        BufferedImage image = img.image;

        // Get the actual width and height of the image
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // Calculate the drawing coordinates
        int w1 = center.x - imageWidth / 2; // Center the image horizontally
        int h1 = center.y - imageHeight / 2; // Center the image vertically
        int w2 = w1 + imageWidth;
        int h2 = h1 + imageHeight;

        // Check if the image is outside the visible area
        boolean isOut = h2 <= 0 || w2 <= 0 || h1 >= size.height || w1 >= size.width;
        if (isOut) {
            return;
        }

        // Draw the image
        
        g.drawImage(image, w1, h1, w2, h2, 0, 0, imageWidth, imageHeight, null);
        g.setColor(Color.RED);
        g.fillOval(imageWidth-20, imageHeight-20, 40, 40);
    }
}

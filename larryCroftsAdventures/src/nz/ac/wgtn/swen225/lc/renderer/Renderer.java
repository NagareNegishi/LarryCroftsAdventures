package nz.ac.wgtn.swen225.lc.renderer;


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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Item;
import nz.ac.wgtn.swen225.lc.domain.Key;
import nz.ac.wgtn.swen225.lc.domain.LockedDoorTile;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Treasure;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Renderer extends JPanel {

    
	private static final long serialVersionUID = 1L;
	private final int FPS = 60; // 60 frames per second
    private final int frameTime = 1000 / FPS; // time per frame in milliseconds
    
    private int x;
    private int y;
    private GameState game;
    
    private int cX; //TODO remove this code
    private int cY; //this is legacy code and should be removed
    
    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    
    private Graphics g;
    
    //these fields are for testing and recognizing the fps
    final RenderImg renderKourie = new RenderImg(Img.Kourie);
    
    public Renderer() { //presumambly have to take in the Gamestate here?
        // Timer for updating the canvas every frame
        Timer timer = new Timer(frameTime, e -> updateCanvas());
        timer.start();
        
        System.out.println(this.getSize().getHeight());
        System.out.println(this.getSize().getWidth());
    }
    
    public void gameConsumer(GameState game) {
    	this.game = game;
    }
    
    public void updateCanvas() {
       repaint();
       //this, will call paintComponent after it's done painting the last frame?
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        this.g = g;
        
        fpsCheck();

        // Display FPS on the screen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("FPS: " + fps, 10, 20); // Draw FPS at top left
        

        
        Dimension size = this.getSize();
        
        //Point center = new Point(1,1);
        y = game.getChap().getCol();
        x = game.getChap().getRow();
        
        Point center = new Point(size.width/2,size.height/2);
		
        //There's a specific method for drawing Chap
        renderKourie.drawChap(g, center, size);
        
        
        
        g.setColor(Color.WHITE);
        g.fillOval(size.width/2,(size.height/2)-20, 40, 40);
        
        drawTiles();
        
        
        }
    
    private void fpsCheck() {
    	
    	long currentTime = System.nanoTime();
        frames++;
        if (currentTime - lastTime >= 1_000_000_000) {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
        }
    	
    }
    
    private void drawTiles() {

    	int maxCol = game.getMaze().getCols();
    	int maxRow = game.getMaze().getRows();
    	
    	for (int rowOffset = 0; rowOffset <= maxRow+1; rowOffset++) {
    		for(int colOffset = 0; colOffset <= maxCol+1; colOffset++) {
    			
    			int currentY = x + colOffset;
                int currentX = y + rowOffset;
    			
                
                Tile t = game.getMaze().getTile(currentX, currentY);
                //draw a free tile
                g.setColor(Color.GREEN);
                g.drawRect(currentX, currentY, currentY+32, currentX+32);
                
                //look if the tile has an item
                if(t.hasItem()) {
                	cX = currentX;
                	cY = currentY;
                	drawItem(t.getItem());}
                
    		}
    	
    		
    	}
    	
    }
    
    private void drawItem(Item i) {
    	
    	//run a helper method to find what's on here and draw it}
        	
        	if(i instanceof Key) {
        		//draw key
        		g.setColor(Color.CYAN);
        		g.drawRect(cX, cY, cX+32, cY+32);
        		
        	}
        	
        	if(i instanceof Treasure) {
        		g.setColor(Color.ORANGE);
        		g.drawRect(cX, cY, cX+32, cY+32);
        		
        	}
        	
        	if(i instanceof LockedDoorTile) {
        		g.setColor(Color.BLUE);
        		g.drawRect(cX, cY, cX+32, cY+32);
        		
        	}
        	
        }
    
    
    
}
    	
    	
    	
    	

    	
    
    
   
    


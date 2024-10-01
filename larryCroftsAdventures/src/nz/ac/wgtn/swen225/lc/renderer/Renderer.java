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


/**
 * This is the Renderer that handles the logic for rendering. 
 * 
 * if you want to update the current panel, use updateCanvas()
 * 
 * It Extends JPanel to apply it's own rendering
 * Renderer requires a Gamestate to draw anything, if a Gamestate isn't present, it won't draw anything. 
 * be sure to update the Gamestate accordingly
 * 
 * 
 * @author Marwan Mohamed
 */

public class Renderer extends JPanel {

    
	private static final long serialVersionUID = 1L;
	private final int FPS = 60; // 60 frames per second
    private final int frameTime = 1000 / FPS; // time per frame in milliseconds
    
    private int x;
    private int y;
    private GameState game;
    
    private int cX; //TODO remove this code
    private int cY; //should be removed after the old rendering engine is removed
    
    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    
    private Graphics g;
    
    //Fields for the images
    final RenderImg renderKourie = new RenderImg(Img.Kourie);
    
    
    /**
     * Renderer constuctor inilizes a Timer to update every frame.
     * This timer can be removed in favour for more optimized calls by App or domain, 
     * for whenever the player moves and on a seperate thread for when the enemies move
     */
    public Renderer() { 
        Timer timer = new Timer(frameTime, e -> updateCanvas());
        timer.start();
        
        System.out.println(this.getSize().getHeight());
        System.out.println(this.getSize().getWidth());
    }
    
    /*
     * Sets the game that's currently being used. 
     */
    public void gameConsumer(GameState game) {
    	this.game = game;//should honestly be called setGame. Marco's been whispering in my head for too long
    }
    
    
    /*
     * updates the frame after waiting for the previous frame to finish, do NOT call paintComponent directly
     * it will cause problems
     */
    public void updateCanvas() {
       repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        if(game == null) {return;}
        
        this.g = g;
        
        fpsCheck();

        // Display FPS on the screen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("FPS: " + fps, 10, 20); // Draw FPS at top left
        

        
        Dimension size = this.getSize();
        
        //Point center = new Point(1,1);
        y = game.getChap().getRow();
        x = game.getChap().getCol();
        
        Point center = new Point(x*32,y*32);
		
        //There's a specific method for drawing Chap
        renderKourie.drawChap(g, center, size);
        
      
        
        g.setColor(Color.WHITE);
        //g.fillOval(size.width/2,(size.height/2), 40, 40);
        
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
    
    
    /*
     * Draws the maze itself, centered on chap
     */
    private void drawTiles() {

    	int maxCol = game.getMaze().getCols();
    	int maxRow = game.getMaze().getRows();
    	
    	
    	//the current code doesn't offset properly, it will be reviewed at a later date
    	for (int rowOffset = 0; rowOffset < maxRow; rowOffset++) {
    		for(int colOffset = 0; colOffset < maxCol; colOffset++) {
    			
    			int currentX = colOffset;
    			int currentY = rowOffset;
    			
                if(currentX < 0 || currentX >= maxCol || currentY < 0 || currentY >= maxRow) {
                	g.setColor(Color.BLACK);
                    g.drawRect( (currentX *32), currentY*32, 32,32);
                }else {
                	
                Tile t = game.getMaze().getTile(currentY, currentX);
	                //draw a free tile
	                g.setColor(Color.GREEN);
	                g.drawRect((currentX *32), currentY*32, 32, 32);
	                //look if the tile has an item
	                if(t.hasItem()) {
	                	cX = currentX *32;
	                	cY = currentY*32;
	                	drawItem(t.getItem());}
                }
                
    		}
    	
    		
    	}
    	
    }
    
    /**
     * draws the item set on the current Tile
     * 
     * @param Item in the current tile
     */
    private void drawItem(Item i) {
    	
    	//run a helper method to find what's on here and draw it}
    
        	if(i instanceof Key) {
        		//draw key
        		g.setColor(Color.CYAN);
        		g.drawRect(cX, cY, 32, 32);
        		
        	}
        	
        	if(i instanceof Treasure) {
        		g.setColor(Color.ORANGE);
        		g.drawRect(cX, cY, 32, 32);
        		
        	}
        	
        	if(i instanceof LockedDoorTile) {
        		g.setColor(Color.BLUE);
        		g.drawRect(cX, cY, 32, 32);
        		
        	}
        	
        }
    
    
    
}
    	
    	
    	
    	

    	
    
    
   
    
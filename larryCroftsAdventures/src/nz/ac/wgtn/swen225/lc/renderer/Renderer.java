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

import javax.swing.JPanel;

import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Item;
import nz.ac.wgtn.swen225.lc.domain.Key;
import nz.ac.wgtn.swen225.lc.domain.LockedDoorTile;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Treasure;

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

        //////////////////////////////////////////////
        //Timer timer = new Timer(frameTime, e -> updateCanvas());
        //timer.start();
        ////////////////////////////////////////////////
        
        System.out.println(this.getSize().getHeight());
        System.out.println(this.getSize().getWidth());
    }
    
    public void gameConsumer(GameState game) {
        this.game = game;
    }
    
    public void updateCanvas() {
        if (game == null) {
/////////////////////////////////////////
        System.err.println("Game is null");
        ////////////////////////////////////////
            return;
            
        }
        repaint();
       //this, will call paintComponent after it's done painting the last frame?
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
    /** 
    private void drawTiles() {

    	int maxCol = game.getMaze().getCols();
    	int maxRow = game.getMaze().getRows();
    	
    	for (int rowOffset = 0; rowOffset < maxRow; rowOffset++) {
    		for(int colOffset = 0; colOffset < maxCol; colOffset++) {
    			
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
        	
        }*/
    
    

    private void drawTiles() {
        int maxCol = game.getMaze().getCols();
        int maxRow = game.getMaze().getRows();
        
        // Define the visible area around the player
        int visibleRadius = 5;
        int tileSize = 32;
    
        for (int rowOffset = -visibleRadius; rowOffset <= visibleRadius; rowOffset++) {
            for (int colOffset = -visibleRadius; colOffset <= visibleRadius; colOffset++) {
                
                int currentX = x + rowOffset;
                int currentY = y + colOffset;
                
                // Check if the current tile is within the maze bounds
                if (currentX >= 0 && currentX < maxRow && currentY >= 0 && currentY < maxCol) {
                    Tile t = game.getMaze().getTile(currentX, currentY);
                    
                    // Calculate screen position
                    int screenX = (getWidth() / 2) + (rowOffset * tileSize);
                    int screenY = (getHeight() / 2) + (colOffset * tileSize);
                    
                    // Draw a free tile
                    g.setColor(Color.GREEN);
                    g.drawRect(screenX, screenY, tileSize, tileSize);
                    
                    // Look if the tile has an item
                    if (t.hasItem()) {
                        drawItem(t.getItem(), screenX, screenY);
                    }
                }
            }
        }
    }
    
    private void drawItem(Item i, int screenX, int screenY) {
        if (i instanceof Key) {
            g.setColor(Color.CYAN);
        } else if (i instanceof Treasure) {
            g.setColor(Color.ORANGE);
        } else if (i instanceof LockedDoorTile) {
            g.setColor(Color.BLUE);
        }
        g.drawRect(screenX, screenY, 32, 32);
    }










}


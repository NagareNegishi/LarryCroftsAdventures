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

import nz.ac.wgtn.swen225.lc.domain.*;

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
    
    private int playerX;
    private int playerY;
    private GameState game;
    
    private Graphics g;
    
    //Fields for the images
    final RenderImg Chap1 = new RenderImg(Img.chap);
    final RenderImg Chap2 = new RenderImg(Img.Kourie);
    //more fields for diffrent images for a simple animation
    
    final RenderImg LockedDoor = new RenderImg(Img.LockedDoor_blue);
    final RenderImg Wall = new RenderImg(Img.Wall_Tile);
    final RenderImg FreeTile = new RenderImg(Img.FreeTile);
    final RenderImg Treasure = new RenderImg(Img.Treasure);
    final RenderImg Blue_key = new RenderImg(Img.Blue_key);
    
    final RenderImg Actor1 = new RenderImg(Img.Actor);
    
    final int imgSize;
    
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
        imgSize = Chap1.size();
        this.setBackground(Color.BLACK);
    }
    
    /*
     * Sets the game that's currently being used. 
     */
    public void gameConsumer(GameState game) {
    	this.game = game;//should honestly be called setGame. Marco's been whispering in my head for too long
    }
    
    
    /*
     * updates the frame after waiting for the previous frame to finish, 
     * Only call updateCanvas
     * do NOT call paintComponent directly
     * it will cause problems.
     */
    public void updateCanvas() {
       repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        if(game == null) {return;}//in case the game is not set, display nothing
        
        this.g = g;
        
        
        playerY = game.getChap().getRow() * imgSize;
        playerX = game.getChap().getCol() * imgSize;
        
        
        drawTiles();
      //Draws Chap at the center of the screen after drawing all the tiles
        Chap1.drawImg(g, getWidth() / 2, getHeight()/2); 
        
    }
    
    
    
    /*
     * Draws the maze itself, centered on chap
     * 
     * There's an additional Offset `by half a size as to make it even more centered.
     * as for some reasons, it doesn't "feel" very centered
     * 
     */
    private void drawTiles() {
    	
    	//Extra Offset, the current iteration doesn't quite need it
    	int offset = 0;
    	
    	int screenCenterX = getWidth() / 2; // Center of the screen horizontally
        int screenCenterY = getHeight() / 2; // Center of the screen vertically

    	int offsetX = screenCenterX - (playerX);  // Offset for X
        int offsetY = screenCenterY - (playerY);  // Offset for Y
    	
    	int maxCol = game.getMaze().getCols(); //Max Col for maze
    	int maxRow = game.getMaze().getRows(); //max Row for maze
    	
    	
    	for (int row = 0; row < maxRow; row++) {
    		for(int col = 0; col < maxCol; col++) {
    			
    			int currentX = col * imgSize + offsetX - offset;
    			int currentY = row * imgSize + offsetY - offset;
    			
    			Tile t = game.getMaze().getTile(row, col);
                
                drawTile(t, currentX, currentY);
                
                //look if the tile has an item
                if(t.hasItem()) {
          
                	drawItem(t.getItem(),currentX, currentY);}
            
                
    		}
    	
    		
    	}
    	
    }
    
    private void drawTile(Tile i, int x, int y) {
    	
    	if (i instanceof FreeTile || i instanceof KeyTile || i instanceof TreasureTile) {
    		FreeTile.drawImg(g,x,y);
    	}

    	else if (i instanceof LockedDoorTile) {
    		FreeTile.drawImg(g, x, y);
    		LockedDoor.drawImg(g,x,y);
    	}
    	
    	else if (i instanceof WallTile){
    		Wall.drawImg(g,x,y);
    	}
    	
    	else if (i instanceof ExitLockTile) {//I'm not sure what this one is
    		g.setColor(Color.BLACK);
    		g.fillRect(x, y, imgSize, imgSize);
    	}
    	
    	else if (i instanceof InfoFieldTile) {//I'm not sure what this one is either
    		g.setColor(Color.MAGENTA);
    		g.drawRect(x, y, imgSize, imgSize);
    	}    	
    	
    }
    
    /**
     * draws the item set on the current Tile
     * 
     * @param Item in the current tile
     */
    private void drawItem(Item i, int x, int y) {
    	
    	//run a helper method to find what's on here and draw it}
    
        	if(i instanceof Key) {
        		//draw key
        		Blue_key.drawImg(g, x, y);
        		
        	}
        	
        	if(i instanceof Treasure) {
	        	Treasure.drawImg(g, x, y);
        		
        	}
        	

        	
        }
    
    
    
}
    	
    	
    	
    	

    	
    
    
   
    
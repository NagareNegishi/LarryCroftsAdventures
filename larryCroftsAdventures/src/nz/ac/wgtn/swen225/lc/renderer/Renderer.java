package nz.ac.wgtn.swen225.lc.renderer;


import java.awt.Color;

import java.awt.Graphics;
import java.util.List;

import nz.ac.wgtn.swen225.lc.domain.*;

import javax.swing.JPanel;

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
 * @studentID 300653693
 */

public class Renderer extends JPanel {

    
	private static final long serialVersionUID = 1L;
    
    private int playerX;
    private int playerY;
    private GameState game;
    
    private Graphics g;
    
    final UpdatedAnimation chap = new UpdatedAnimation("chap",2,2);
    
    final UpdatedAnimation tp = new UpdatedAnimation("tp",3,3);
    
    final UpdatedAnimation water = new UpdatedAnimation("Water",7,1);
    
    final UpdatedAnimation Actor = new UpdatedAnimation("Actor",2,1);
    
    final UpdatedAnimation Treasure = new UpdatedAnimation("Treasure",2,1);
    
    
    Img img = Img.getInstance();
    
    final int imgSize;
    
    
    private int offsetX;
	private int offsetY;
	private int screenCenterX;
	private int screenCenterY;
	private int maxCol;
	private int maxRow;
    
    /**
     * Renderer constuctor inilizes a Timer to update every frame.
     * This timer can be removed in favour for more optimized calls by App or domain, 
     * for whenever the player moves and on a seperate thread for when the enemies move
     */
    public Renderer() { 
        //Timer timer = new Timer(frameTime, e -> updateCanvas());
        //timer.start();
        
        System.out.println(this.getSize().getHeight());
        System.out.println(this.getSize().getWidth());
        imgSize = img.imgSize;
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
        
        
        
    	screenCenterX = getWidth() / 2; // Center of the screen horizontally
        screenCenterY = getHeight() / 2; // Center of the screen vertically

    	offsetX = screenCenterX - (playerX);  // Offset for X
        offsetY = screenCenterY - (playerY);  // Offset for Y
    	maxCol = game.getMaze().getCols(); //Max Col for maze
    	maxRow = game.getMaze().getRows(); //max Row for maze
        
        
        playerY = game.getChap().getRow() * imgSize;
        playerX = game.getChap().getCol() * imgSize;
        
        
        drawTiles();
      //Draws Chap at the center of the screen after drawing all the tiles
        drawActors(game.enemies());
        chipToriel();
        chap.draw(g, getWidth() / 2, getHeight()/2); 
        
       
    }
    
    /**
     * Draws the Tutorial fields
     */
    private void chipToriel() {    	
    	Tile t = game.getMaze().getTile(game.getChap().getRow(), game.getChap().getCol());
    	int level = game.getLevel();
    	
    	if(t instanceof InfoFieldTile) {
    		if(level == 1) {
    			img.drawInfoTile(g, "intro1",this.getSize() );
    		}else if (level == 2) {
    			img.drawInfoTile(g, "intro2",this.getSize());
    		}
    		
    	}
    }
    
    /**
     * Draws Actors based on the list found in the GameState
     * 
     * @param actors
     */
    private void drawActors(List<Actor> actors) {
    	if (actors != null){
	    	for (Actor actor : actors) {
	    		int col = actor.getCol();
	    		int row = actor.getRow();
	    		int currentX = col * imgSize + offsetX;
				int currentY = row * imgSize + offsetY;
	    		
				Actor.draw(g, currentX, currentY);
	    	}
    	}
    	
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
    
    /**
     * Draws a single tile where x and y is based off of the screen space
     * 
     * @param i
     * @param x
     * @param y
     */
    private void drawTile(Tile i, int x, int y) {
    	
    	if (i instanceof FreeTile || i instanceof KeyTile || i instanceof TreasureTile) {
    		img.drawImg(g,"FreeTile",x,y);
    	}

    	else if (i instanceof LockedDoorTile) {
    		img.drawImg(g,"FreeTile",x,y);
    		
    		String col = ((LockedDoorTile) i).colour();
    		if(col.equals("Blue")) {
    			img.drawImg(g,"LockedDoor_blue", x, y);
    		}else if(col.equals("Red")) {
    			img.drawImg(g,"LockedDoor_red", x, y);
    		}
    		
    		
    		
    	}
    	
    	else if (i instanceof WallTile){
    		img.drawImg(g,"Wall_Tile",x,y);
    	}
    	
    	else if(i instanceof TeleportTile) {
    		tp.draw(g,x,y);
    	}
    	
    	else if(i instanceof WaterTile) {
    		water.draw(g, x, y);
    	}
    	
    	else if (i instanceof Exit) {
    		img.drawImg(g,"Stairs", x, y);
    	}
    	
    	else if(i instanceof ExitLockTile) {
    		boolean open = ((ExitLockTile) i).canMoveTo();
    		if(!open) {
    			img.drawImg(g,"ExitLock",x,y);    			
    		}else {
    			img.drawImg(g,"ExitOpen", x, y);
    			
    		}
    		
    	}
    	
    	else if (i instanceof InfoFieldTile) {
    		img.drawImg(g,"InfoTile", x, y);
    	}    	
    	
    }
    
    /**
     * Draws a single Item where x and y is based off of the screen space
     * 
     * @param Item i and x and y
     */
    private void drawItem(Item i, int x, int y) {
    	
        	if(i instanceof Key) {
        		String col = ((Key) i).colour();
        		if(col.equals("Blue")) {
        			img.drawImg(g,"Blue_key", x, y);
        		}else if(col.equals("Red")) {
        			img.drawImg(g,"Red_key", x, y);
        		}
        		
        	}
        	
        	if(i instanceof Treasure) {
	        	Treasure.draw(g, x, y);
        		
        	}
        }
    
}
    	
    	
    	
    	

    	
    
    
   
    
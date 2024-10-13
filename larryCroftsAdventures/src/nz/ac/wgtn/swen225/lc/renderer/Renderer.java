package nz.ac.wgtn.swen225.lc.renderer;


import java.awt.Color;

import java.awt.Graphics;
import java.util.List;

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
	private final int FPS = 2; // 60 frames per second
    private final int frameTime = 1000 / FPS; // time per frame in milliseconds
    
    private int playerX;
    private int playerY;
    private GameState game;
    
    private Graphics g;
    
    //I can most definitely improve this
    final RenderImg Chap1 = new RenderImg(Img.chap1);
    final RenderImg Chap2 = new RenderImg(Img.chap2);
    
    final AnimatedImage chap = new AnimatedImage(List.of(Chap1, Chap2));
    
    final RenderImg w1 = new RenderImg(Img.Water1);
    final RenderImg w2 = new RenderImg(Img.Water2);
    final RenderImg w3 = new RenderImg(Img.Water3);
    final RenderImg w4 = new RenderImg(Img.Water4);
    final RenderImg w5 = new RenderImg(Img.Water5);
    final RenderImg w6 = new RenderImg(Img.Water6);
    final RenderImg w7 = new RenderImg(Img.Water7);
    
    final AnimatedImage water = new AnimatedImage(List.of(w1,w2,w3,w4,w5,w6,w7));
    
    final RenderImg Actor1 = new RenderImg(Img.Actor);
    final RenderImg Actor2 = new RenderImg(Img.Actor); //Actor is meant to have one more image
    final AnimatedImage Actor = new AnimatedImage(List.of(Actor1,Actor2));
    
    final RenderImg Wall = new RenderImg(Img.Wall_Tile);
    final RenderImg FreeTile = new RenderImg(Img.FreeTile);
    final RenderImg Treasure = new RenderImg(Img.Treasure);
    final RenderImg Blue_key = new RenderImg(Img.Blue_key);
    final RenderImg Red_key = new RenderImg(Img.Red_key);
    final RenderImg Exit = new RenderImg(Img.Stairs);
    final RenderImg info = new RenderImg(Img.InfoTile);
    final RenderImg Blue_LockedDoor = new RenderImg(Img.LockedDoor_blue);
    //Make a red Locked door
    //make open versions of those doors
    
    //This is a temp for a background image
    final RenderImg bg = new RenderImg(Img.intro);
    
    
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
        
        //Background Logic
        water.drawBg(g, this.getSize());
        
        
    	screenCenterX = getWidth() / 2; // Center of the screen horizontally
        screenCenterY = getHeight() / 2; // Center of the screen vertically

    	offsetX = screenCenterX - (playerX);  // Offset for X
        offsetY = screenCenterY - (playerY);  // Offset for Y
    	
        //Can move this somewhere else
    	maxCol = game.getMaze().getCols(); //Max Col for maze
    	maxRow = game.getMaze().getRows(); //max Row for maze
        
        
        
        playerY = game.getChap().getRow() * imgSize;
        playerX = game.getChap().getCol() * imgSize;
        
        
        drawTiles();
      //Draws Chap at the center of the screen after drawing all the tiles
        chap.draw(g, getWidth() / 2, getHeight()/2); 
        


    }
    
    private void drawActors(List<Object> actors) {
//    	
//    	for (Object actor : actors) {
//    		col = actor.getcol();
//    		row = actor.getRow();
//    		int currentX = col * imgSize + offsetX;
//			int currentY = row * imgSize + offsetY;
//    		
//			Actor.draw(g, currentX, currentY);
//    	}
    	
    	
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
    
    private void drawTile(Tile i, int x, int y) {
    	
    	if (i instanceof FreeTile || i instanceof KeyTile || i instanceof TreasureTile) {
    		FreeTile.drawImg(g,x,y);
    	}

    	else if (i instanceof LockedDoorTile) {
    		FreeTile.drawImg(g, x, y);
    		
    		String col = ((LockedDoorTile) i).colour();
    		if(col == "Blue") {
    			Blue_LockedDoor.drawImg(g, x, y);
    		}else if(col == "Red") {
    			//Red_key.drawImg(g, x, y);
    		}
    		
    		
    		
    	}
    	
    	else if (i instanceof WallTile){
    		Wall.drawImg(g,x,y);
    	}
    	
//    	else if(i instanceof TP) {
//    		tp.drawImg(g,x,y);
//    	}
    	
    	else if (i instanceof ExitLockTile) {
    		Exit.drawImg(g, x, y);
    	}
    	
    	else if (i instanceof InfoFieldTile) {
    		info.drawImg(g, x, y);
    	}    	
    	
    }
    
    /**
     * draws the item set on the current Tile
     * 
     * @param Item i and x and y
     */
    private void drawItem(Item i, int x, int y) {
    	
    	
    
        	if(i instanceof Key) {
        		String col = ((Key) i).colour();
        		if(col == "Blue") {
        			Blue_key.drawImg(g, x, y);
        		}else if(col == "Red") {
        			Red_key.drawImg(g, x, y);
        		}
        		
        	}
        	
        	if(i instanceof Treasure) {
	        	Treasure.drawImg(g, x, y);
        		
        	}
        }
    
}
    	
    	
    	
    	

    	
    
    
   
    
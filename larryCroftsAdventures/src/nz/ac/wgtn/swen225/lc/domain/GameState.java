package nz.ac.wgtn.swen225.lc.domain;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

public class GameState {
	
	private Maze maze;
	private Chap chap;
	private int treasuresCollected;
	private int totalTreasures;
	
	// field for collected keys: map from key to colour? 
	//private Map<Key, String> keysCollected;
	
	public GameState(Maze maze, Chap chap, int totalTreasures) {
		if(maze.equals(null) || chap.equals(null)) {throw new IllegalArgumentException("Chap or Maze is null");}
		if(totalTreasures < 0) {throw new IllegalArgumentException("Total treasures must be greater than 0");}
		this.maze = maze;
		this.chap = chap;
		this.treasuresCollected = 0;
		this.totalTreasures = totalTreasures;
		//this.keysCollected = new HashMap<>();
	}
	
	public int totalTreasures() {return totalTreasures;}
	public int getTreasuresCollected() {return treasuresCollected;}
	public void treasureCollected() {this.treasuresCollected++;}	
	public boolean allTreasureCollected() {return treasuresCollected == totalTreasures ? true : false;}
	
	//public Map<Key,String> keysCollected(){return keysCollected;}
	
	public String chapPosition(){return chap.getPosition();}
	
	public void moveChap(Direction direction) {
	    chap.move(direction, maze);
	    checkForItem();
	}

	public void checkForItem() {
		Tile currentTile = maze.getTile(chap.getRow(), chap.getCol());
        if (currentTile.hasItem()) {
            Item item = currentTile.getItem();
            chap.pickUpItem(item);
            if(item instanceof Treasure) {this.treasureCollected();}
            currentTile.removeItem();
            maze.setTile(chap.getRow(), chap.getCol() , new FreeTile());
	}
}
	// TODO: method to change game state when treasureCollected == totalTreasures so exit lock opens
}

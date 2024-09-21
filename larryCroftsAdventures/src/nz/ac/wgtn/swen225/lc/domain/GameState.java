package nz.ac.wgtn.swen225.lc.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

public class GameState implements GameStateInterface {
	
	@JsonProperty
	private Maze maze;
	@JsonProperty
	private Chap chap;
	@JsonProperty
	private int treasuresCollected;
	@JsonProperty
	private int totalTreasures;
	
	// field for collected keys: map from key to colour? 
	//private Map<Key, String> keysCollected;
	
	@JsonCreator
	public GameState(@JsonProperty("maze") Maze maze,
					@JsonProperty("chap") Chap chap,
					@JsonProperty("totalTreasures") int totalTreasures) {
		
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
	
	// move Chap in a given direction, will see where Chap is planning to move and take care of actions
	public void moveChap(Direction direction) {
		int newRow = chap.getRow() + direction.rowDirection();
		int newCol = chap.getCol() + direction.colDirection();
		Tile nextTile = maze.getTile(newRow, newCol);
		
		// using ifs to get logic down can edit later to avoid ifs
		if(nextTile instanceof LockedDoorTile) {
			 LockedDoorTile tile = (LockedDoorTile) nextTile;
			 // stop Chap from moving if he doesn't have the right key
			 if(!checkForMatchingKey(tile.colour())) {
				 return;
			 // otherwise unlock the door and move him onto it
			 } else {
				 tile.unlock();
			 }
		}
		
	    chap.move(direction, maze);
	    
	    // possibility of causing issues down the line, will test
	    if (nextTile instanceof LockedDoorTile) {
	        maze.setTile(newRow, newCol, new FreeTile());
	    }
	    checkForItem();
	}

	// could use streams to tidy up
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
		
	public boolean checkForMatchingKey(String doorColour) {
		return chap.inventory().stream()
							   .filter(item -> item instanceof Key)
							   .map(item -> (Key) item)
							   .anyMatch(key -> key.colour().equals(doorColour));
	}
	// TODO: method to change game state when treasureCollected == totalTreasures so exit lock opens
}



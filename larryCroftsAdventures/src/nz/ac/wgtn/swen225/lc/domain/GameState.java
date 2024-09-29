package nz.ac.wgtn.swen225.lc.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameState implements GameStateInterface {
	
	@JsonProperty
	private Maze maze;
	@JsonProperty
	private Chap chap;
	@JsonProperty
	private int treasuresCollected;
	@JsonProperty
	private int totalTreasures;
	@JsonProperty
	private Map<Key, String> keysCollected;
	// Added by Adam
	// seconds left for level
	@JsonProperty
	private int timeLeft;
	
	
	public GameState(Maze maze, Chap chap, int totalTreasures) {
		
		if(maze.equals(null) || chap.equals(null)) {throw new IllegalArgumentException("Chap or Maze is null");}
		if(totalTreasures < 0) {throw new IllegalArgumentException("Total treasures must be greater than 0");}
		this.maze = maze;
		this.chap = chap;
		this.treasuresCollected = 0;
		this.totalTreasures = totalTreasures;
		this.keysCollected = new HashMap<>();
	}
	
	
	/**
	 * New constructor specifically for Jackson reconstruction
	 * @param maze
	 * @param chap
	 * @param totalTreasures
	 * @param keysCollected
	 * @param time
	 */
	@JsonCreator
	public GameState(@JsonProperty("maze") Maze maze,
					@JsonProperty("chap") Chap chap,
					@JsonProperty("totalTreasures") int totalTreasures,
					@JsonProperty("keysCollected") Map<Key, String> keysCollected,
					@JsonProperty("timeLeft") int timeLeft){
		
		if(maze.equals(null) || chap.equals(null)) {throw new IllegalArgumentException("Chap or Maze is null");}
		if(totalTreasures < 0) {throw new IllegalArgumentException("Total treasures must be greater than 0");}
		this.maze = maze;
		this.chap = chap;
		this.treasuresCollected = 0;
		this.totalTreasures = totalTreasures;
		this.keysCollected = keysCollected;
		this.timeLeft =timeLeft;
	}
	
	
	public int totalTreasures() {return totalTreasures;}
	public int getTreasuresCollected() {return treasuresCollected;}
	public void treasureCollected() {this.treasuresCollected++;}	
	public boolean allTreasureCollected() {return treasuresCollected == totalTreasures ? true : false;}
	public Map<Key,String> keysCollected(){return keysCollected;}
	
	public String chapPosition(){return chap.getPosition();}
	// Added by Adam
	public int getTime() {return timeLeft;}
	public void setTime(int time) {this.timeLeft = time;}
	
	// move Chap in a given direction, will see where Chap is planning to move and take care of actions
	public void moveChap(Direction direction) {
		int newRow = chap.getRow() + direction.rowDirection();
		int newCol = chap.getCol() + direction.colDirection();
		Tile nextTile = maze.getTile(newRow, newCol);
		
		switch (nextTile) {
        case LockedDoorTile tile -> {
            if (checkForMatchingKey(tile.colour())) {
                tile.unlock();
				////////////////////////////
				System.out.println("Chap has unlocked the door");
				//////////////////////////
                maze.setTile(newRow, newCol, new FreeTile()); // Replace the locked door with a free tile
            } else {
                return; // Stop Chap from moving if he doesn't have the right key
            }
        }
        case ExitLockTile tile -> {
            if (allTreasureCollected()) {
                tile.unlock();
				////////////////////////////////
				System.out.println("Chap has unlocked the exit");
				//////////////////////////////
            } else {
                return; // Stop Chap from moving if there's still treasure to collect
            }
        }
        case InfoFieldTile tile -> {
			/////////////////////////////
			System.out.println("Chap has entered an info field");
			///////////////////////////

            tile.displayText(); // Display information on the tile
        }
        case Exit tile ->{
			/////////////////////////////
			System.out.println("Chap has reached the exit");
			///////////////////////////
        	// Finish level an go to next level
        }
        default -> {
            
        }
    }
	    chap.move(direction, maze);
	    // checks for an item everytime Chap moves to a tile
	    checkForItem();
	}

	// could use streams to tidy up
	public void checkForItem() {
		Tile currentTile = maze.getTile(chap.getRow(), chap.getCol());
        if (currentTile.hasItem()) {
            Item item = currentTile.getItem();
            chap.pickUpItem(item);
            switch (item) {
            case Treasure treasure -> treasureCollected();
            case Key key -> keysCollected.put(key, key.colour());
            default -> {}
        }
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
	public String chapSurroundings() {
		return "To Chap's left is a: " + maze.getTile(chap.getRow(), chap.getCol() - 1).tileType() + "\n"
				+ "Above Chap is a " + maze.getTile(chap.getRow() -1, chap.getCol()).tileType() + "\n"
				+ "To Chap's right is a " + maze.getTile(chap.getRow(), chap.getCol() + 1).tileType() + "\n"
				+ "Below Chap is a " + maze.getTile(chap.getRow() + 1, chap.getCol()).tileType();
	}



	////////////////////nagi's code remove me later
	public Chap getChap() {
		return chap;
	}
	public Maze getMaze() {
		return maze;
	}
}



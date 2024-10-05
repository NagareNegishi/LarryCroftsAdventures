package nz.ac.wgtn.swen225.lc.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

/**
 * GameState class represents the state the game is currently in. This includes the current maze, Chap,
 * number of treasures collected, total treasures, keys, time left, enemies etc.
 * 
 * @author fergusbenj1 300656321
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameState{
	
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
	// List for enemies in the level
	List<Actor> enemies;
	// AppNotifier
	public AppNotifier appNotifier;
	
	public GameState(Maze maze, Chap chap, int totalTreasures) {
		
		if(maze == null || chap == null) {throw new IllegalArgumentException("Chap or Maze is null");}
		if(totalTreasures < 0) {throw new IllegalArgumentException("Total treasures must be greater than 0");}
		
		this.maze = maze;
		this.chap = chap;
		this.treasuresCollected = 0;
		this.totalTreasures = totalTreasures;
		this.keysCollected = new HashMap<>();
		this.timeLeft = 60; // 60 seconds by default

		assert this.totalTreasures == totalTreasures;
		assert keysCollected.isEmpty() == true;
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
					@JsonProperty("timeLeft") int timeLeft,
					AppNotifier appNotifier){
		
		if(maze == null || chap == null) {throw new IllegalArgumentException("Chap or Maze is null");}
		if(totalTreasures < 0 || timeLeft < 0) {throw new IllegalArgumentException("Total treasures and time left must be greater than 0");}
		if(keysCollected == null) {throw new IllegalArgumentException("KeysCollected list is null");}
		
		this.maze = maze;
		this.chap = chap;
		this.treasuresCollected = 0;
		this.totalTreasures = totalTreasures;
		this.keysCollected = keysCollected;
		this.timeLeft =timeLeft;
		this.appNotifier = appNotifier;

		assert this.totalTreasures == totalTreasures;
		assert this.timeLeft == timeLeft;
	}
	
	
	public int totalTreasures() {return totalTreasures;}
	public int getTreasuresCollected() {return treasuresCollected;}
	public void treasureCollected() {this.treasuresCollected++;}	
	public boolean allTreasureCollected() {return treasuresCollected == totalTreasures ? true : false;}
	public Map<Key,String> keysCollected(){return keysCollected;}
	
	// Added by Adam
	public int getTime() {return timeLeft;}
	public void setTime(int time) {this.timeLeft = time;}

	///////////////////////////////////////////////////////
	// if we want to store the time at the save/load we probably need it for level too
	// remove if it not needed
	public int currentLevel;
	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	///////////////////////////////////////////////////////////////

	// move Chap in a given direction, will see where Chap is planning to move and take care of actions
	public void moveChap(Direction direction) {
		if(direction.equals(null)) {throw new IllegalArgumentException("Cannot move because direction is null");}
		
		int newRow = chap.getRow() + direction.rowDirection();
		int newCol = chap.getCol() + direction.colDirection();
		Tile nextTile = maze.getTile(newRow, newCol);
		
		switch (nextTile) {
        case LockedDoorTile tile -> {
            if (checkForMatchingKey(tile.colour())) {
                tile.unlock();
				//System.out.println("Chap has unlocked the door");
                maze.setTile(newRow, newCol, new FreeTile()); // Replace the locked door with a free tile
            } else {
                return; // Stop Chap from moving if he doesn't have the right key
            }
        }
        case ExitLockTile tile -> {
            if (allTreasureCollected()) {
                tile.unlock();
				//System.out.println("Chap has unlocked the exit");
            } else {
                return; // Stop Chap from moving if there's still treasure to collect
            }
        }
        case InfoFieldTile tile -> {
			//System.out.println("Chap has entered an info field");
            tile.displayText(); // Display information on the tile
        }
        case Exit tile ->{
			//System.out.println("Chap has reached the exit");
			Win();
        }
        case WaterTile tile ->{
        	Lose();
        }
        case TeleportTile tile ->{
        	chap.moveTo(tile.partner().row(),tile.partner().col(), maze);
        }
        default -> {
            
        }
    }
	    chap.move(direction, maze);
	    // checks for an item and enemy everytime Chap moves to a tile
	   // checkForEnemy();
	    checkForItem();
	}

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
	
	/*public void checkForEnemy() {
		int row = chap.getRow();
		int col = chap.getCol();
		
		for(Actor a : enemies) {
			if(a.getRow() == row && a.getCol() == col) {
				Lose();
			}
		}
	} */
		
	public boolean checkForMatchingKey(String doorColour) {
		return chap.inventory().stream()
							   .filter(item -> item instanceof Key)
							   .map(item -> (Key) item)
							   .anyMatch(key -> key.colour().equals(doorColour));
	}
	
	public Chap getChap() {return chap;}
	public Maze getMaze() {return maze;}
	
	// this should go but for the test, I need it
	public void setAppNotifier(AppNotifier appNotifier) {
		this.appNotifier = appNotifier;
	}

	public void Win(){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onGameWin();
	}

	public void Lose(){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onGameLose();
		System.out.println("Game Over is called in GameStateController");
	}

	public void KeyPickup(int keyCount){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onKeyPickup(keyCount);
	}

	public void TreasurePickup(int treasureCount){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onTreasurePickup(treasureCount);
	}
}



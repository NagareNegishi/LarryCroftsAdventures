package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.persistency.MockAppNotifier;
import test.nz.ac.wgtn.swen225.lc.fuzz.Fuzz;

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
	private Direction chapDirection;
	// List for enemies in the level
	@JsonProperty
	private ArrayList<Actor> enemies;
	// AppNotifier
	@JsonProperty
	@JsonSerialize(as = MockAppNotifier.class)
	@JsonDeserialize(as = MockAppNotifier.class)
	private AppNotifier appNotifier;
	@JsonProperty
	private int level;

	/**
	 * @param maze that the level is based on
	 * @param current chap 
	 * @param totalTreasures in the level
	 * @param keysCollected in the level
	 * @param time of the level
	 */
	@JsonCreator
	public GameState(@JsonProperty("maze") Maze maze,
					@JsonProperty("chap") Chap chap,
					@JsonProperty("totalTreasures") int totalTreasures,
					@JsonProperty("treasuresCollected")int treasuresCollected,
					@JsonProperty("keysCollected") Map<Key, String> keysCollected,
					@JsonProperty("timeLeft") int timeLeft,
					@JsonProperty("appNotifier") AppNotifier appNotifier,
					@JsonProperty("enemies")ArrayList<Actor> enemies,
					@JsonProperty("level") int level) {
		
		if(maze == null || chap == null) {throw new IllegalArgumentException("Chap or Maze is null");}
		if(totalTreasures < 0 || timeLeft < 0) {throw new IllegalArgumentException("Total treasures and time left must be greater than 0");}
		if(keysCollected == null) {throw new IllegalArgumentException("KeysCollected list is null");}
		if(enemies == null) {throw new IllegalArgumentException("List of enemies is null");}
		if(appNotifier == null) {throw new IllegalArgumentException("App notifier is null");}
		if(level < 0) {throw new IllegalArgumentException("Level must be greater than 0");}
		
		this.maze = maze;
		this.chap = chap;
		this.treasuresCollected = treasuresCollected;
		this.totalTreasures = totalTreasures;
		this.keysCollected = keysCollected;
		this.timeLeft =timeLeft;
		this.appNotifier = appNotifier;
		this.enemies = (ArrayList<Actor>) enemies;
		this.level = level;
		

		assert this.treasuresCollected == treasuresCollected;
		assert this.totalTreasures == totalTreasures;
		assert this.timeLeft == timeLeft;
		assert this.level == level;
	}
	
	
	public int totalTreasures() {return totalTreasures;}
	public int getTreasuresCollected() {return treasuresCollected;}
	public void treasureCollected() {this.treasuresCollected++;}
	public boolean allTreasureCollected() {return treasuresCollected == totalTreasures ? true : false;}
	public Map<Key,String> keysCollected(){return keysCollected;}
	
	// Added by Adam
	public int getTime() {return timeLeft;}

	public ArrayList<Actor> enemies(){
		return enemies;
	}
	
	/**
	 * @param time to set for the level
	 */
	public void setTime(int time) {this.timeLeft = time;}

	// if we want to store the time at the save/load we probably need it for level too
	public int getLevel() { return level;}
	public Direction chapDirection() {return chapDirection;}
	
	/** move Chap in a given direction, will see where Chap is planning to move and take care of actions
	 *
	 * @param direction for chap to move in 
	 */
	public void moveChap(Direction direction) {
		this.chapDirection = direction;
		if(direction == null) {throw new IllegalArgumentException("Cannot move because direction is null");}
		
		int newRow = chap.getRow() + direction.rowDirection();
		int newCol = chap.getCol() + direction.colDirection();
		Tile nextTile = maze.getTile(newRow, newCol);
		
		switch (nextTile) {
        case LockedDoorTile tile -> {
            if (checkForMatchingKey(tile.colour())) {
                tile.unlock();
                Fuzz.events.add("chap unlocked the door");
                maze.setTile(newRow, newCol, new FreeTile()); // Replace the locked door with a free tile
                assert maze.getTile(newRow, newCol) instanceof FreeTile;
                
            } else {
            	Fuzz.events.add("chap tried the door");
                return; // Stop Chap from moving if he doesn't have the right key
            }
        }
        case ExitLockTile tile -> {
            if (allTreasureCollected()) {
                tile.unlock();

                assert tile.canMoveTo() == true;

                Fuzz.events.add("chap unlocked the exit");

            } else {
            	Fuzz.events.add("chap tried the exit");
                return; // Stop Chap from moving if there's still treasure to collect
            }
        }
        case InfoFieldTile tile -> {
        	Fuzz.events.add("chap opened info ");
            tile.displayText(); // Display information on the tile
        }
        case Exit tile ->{
        	Fuzz.events.add("chap exited, win");

			Win();
        }
        case WaterTile tile ->{
        	Fuzz.events.add("chap fell into water, lose");
        	Lose();
        }
        case TeleportTile tile ->{
        	chap.moveTo(tile.teleportRow(),tile.teleportCol(), maze);

        	assert chap.getRow() == tile.teleportRow() && chap.getCol() == tile.teleportCol();

        	Fuzz.events.add("chap got teleported");

        	return;
        }
        default -> {}
    }
	    chap.move(direction, maze);
	    
	    // checks for an item and enemy everytime Chap moves to a tile
	    checkForEnemy();
	    checkForItem();
	}
	
	public void moveActor() {
		enemies.forEach(a -> a.move(maze));
		}

	/**
	 * check for an item each time it is called and execute behaviour based on instance of the item
	 */
	public void checkForItem() {
		Tile currentTile = maze.getTile(chap.getRow(), chap.getCol());
        if (currentTile.hasItem()) {
            Item item = currentTile.getItem();
            chap.pickUpItem(item);
            switch (item) {
            case Treasure treasure -> {
            	int treasuresCollectedBefore = treasuresCollected;
				treasureCollected();
				TreasurePickup(treasuresCollected); ////////////// Added by Nagi
				int treasuresCollectedAfter = treasuresCollected;
				assert treasuresCollectedAfter == treasuresCollectedBefore+1;
			}
            case Key key -> {
				keysCollected.put(key, key.colour());
				KeyPickup(key.colour());
				assert chap.inventory().contains(item);
			}
            default -> {}
        }
            currentTile.removeItem();
            maze.setTile(chap.getRow(), chap.getCol() , new FreeTile());
            assert maze.getTile(chap.getRow(), chap.getCol()) instanceof FreeTile;
        }
	}
	
	/**
	 * checks for a collision with Chap and an enemy and if there is a collision the game is over
	 */
	public void checkForEnemy() {
		int row = chap.getRow();
		int col = chap.getCol();
		
		for(Actor a : enemies) {
			if(a.getRow() == row && a.getCol() == col) {
				Lose();
			}
		}
	} 
		
	/**
	 * check a given doorColour matching a key in chaps inventory
	 * @param doorColour
	 * @return true if key colour matches door, false otherwise 
	 */
	public boolean checkForMatchingKey(String doorColour) {
		return chap.inventory().stream()
							   .filter(item -> item instanceof Key)
							   .map(item -> (Key) item)
							   .anyMatch(key -> key.colour().equals(doorColour));
	}
	
	public Chap getChap() {return chap;}
	public Maze getMaze() {return maze;}
	
	// added by Nagi
	public void setAppNotifier(AppNotifier appNotifier) {
		this.appNotifier = appNotifier;
	}

	// added by Nagi
	public void Win(){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onGameWin();
		//ANTHONY: added for debugging :)
		//System.out.println("Game Win is called in GameStateController");
	}

	// added by Nagi
	public void Lose(){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onGameLose();
		//System.out.println("Game Over is called in GameStateController");
	}

	// added by Nagi
	public void KeyPickup(String keyName){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onKeyPickup(keyName);
	}

	// added by Nagi
	public void TreasurePickup(int treasureCount){
		assert appNotifier != null: "AppNotifier is null";
		appNotifier.onTreasurePickup(treasureCount);
	}
	
	/**
	 * creating a mock game state for testing basic logic and functionality
	 * @return the mockGameState to be used for testing/debugging etc.
	 */
	public static GameState mockGameState() {
		return new GameState(Maze.createBasicMaze(5, 5), new Chap(2,2, 
				new ArrayList<>()),1, 0, new HashMap<Key,String>(), 0, new AppNotifier() {
		@Override
		public void onGameWin() {}
		@Override
	    public void onGameLose() {}
		@Override
	    public void onKeyPickup(String keyName) {}
		@Override
	    public void onTreasurePickup(int treasureCount) {}
	}, new ArrayList<Actor>(), 1);
	}
}



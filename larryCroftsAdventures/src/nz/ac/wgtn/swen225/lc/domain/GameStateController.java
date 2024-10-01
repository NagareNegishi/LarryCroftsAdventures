package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

// Entry point for other modules to access 
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStateController implements GameStateControllerInterface {


	@JsonProperty
	private GameState gameState;
	@JsonIgnore
	private Chap chap;
	@JsonIgnore
	private Maze maze;

	
	/**
	 * Constructor for Jackson de-serialization
	 * @param maze : Placement of tiles in game space
	 * @param chap : player character 
	 * @param gameState : contains all game information. Must also contain provided maze and chap
	 */
	@JsonCreator
	public GameStateController(@JsonProperty("gameState") GameState gameState) {
		this.gameState = gameState;
		this.chap = gameState.getChap();
		this.maze = gameState.getMaze();
	}
	
	
	public void moveChap(Direction direction) {gameState.moveChap(direction);}
	public String getChapPosition() {return gameState.getChap().getPosition();}
	@JsonIgnore
	public List<Item> getChapInventory(){return gameState.getChap().inventory();}
	public void getChapInventoryDesc(){ gameState.getChap().inventoryDescription();}
	public Map<Key,String> getKeysCollected(){return gameState.keysCollected();}
	public int getTotalTreasures() {return gameState.totalTreasures();}
	public int getTreasuresCollected() {return gameState.getTreasuresCollected();}
	public boolean isAllTreasureCollected() {return gameState.allTreasureCollected();}
	 
	public Maze getMaze() {return gameState.getMaze();}
	public Chap getChap() {return gameState.getChap();}
	// Added by Adam
	public int getTime() {return gameState.getTime();}
	public void setTime(int time) {gameState.setTime(time);}
	 
	// can change to return a tile instead of string if needed for comparison later
	//public String getTileAtChapPosition() { return maze.getTile(chap.getRow(), chap.getCol()).tileType();}
	public Tile getTileAtChapPosition() { return maze.getTile(chap.getRow(), chap.getCol());}
	 
	public String mazeSize() {return maze.getRows() + " rows and " + maze.getCols() + " cols";}
	 
	// debugging method for returning the tiles around Chap
	public String chapSurroundings() {
		return "To Chap's left is a: " + maze.getTile(chap.getRow(), chap.getCol() - 1).tileType() + "\n"
				+ "Above Chap is a " + maze.getTile(chap.getRow() -1, chap.getCol()).tileType() + "\n"
				+ "To Chap's right is a " + maze.getTile(chap.getRow(), chap.getCol() + 1).tileType() + "\n"
				+ "Below Chap is a " + maze.getTile(chap.getRow() + 1, chap.getCol()).tileType();
	}




	//////////////this is nagi delete me
	public GameState getGameState() {
		return gameState;
	}
}

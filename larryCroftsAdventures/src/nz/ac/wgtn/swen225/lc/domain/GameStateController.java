package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

/**
 * GameState class represents the entry point for the other modules to use to access and 
 * interact with the main logic of the game.
 * 
 * @author fergusbenj1 300656321
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStateController{
	@JsonProperty
	private GameState gameState;
	@JsonIgnore
	private Chap chap;
	@JsonIgnore
	private Maze maze;	
	public GameStateController(@JsonProperty("gameState") GameState gameState) {
		if(gameState == null) {throw new IllegalArgumentException("GameState is null");}

		this.gameState = gameState;
		this.chap = gameState.getChap();
		this.maze = gameState.getMaze();
	}
	
	/**
	 * move chap in a given direction in the level
	 * @param direction for Chap to move in
	 */
	public void moveChap(Direction direction) {gameState.moveChap(direction);}
	
	/**
	 *  move each enemy one step and check for collision with Chap
	 */
	public void moveActor() {
		gameState.moveActor();
		gameState.checkForEnemy();
		}
	
	// getters for gamestate fields 
	@JsonIgnore
	public List<Item> getChapInventory(){return gameState.getChap().inventory();}
	public Map<Key,String> getKeysCollected(){return gameState.keysCollected();}
	public int getTotalTreasures() {return gameState.totalTreasures();}
	public int getTreasuresCollected() {return gameState.getTreasuresCollected();}
	public boolean isAllTreasureCollected() {return gameState.allTreasureCollected();}
	public Direction getChapDirection() {return gameState.chapDirection();}
	public Maze getMaze() {return gameState.getMaze();}
	public Chap getChap() {return gameState.getChap();}
	public List<Actor> getActors(){return gameState.enemies();}
	
	// Added by Adam
	public int getTime() {return gameState.getTime();}
	public void setTime(int time) {gameState.setTime(time);}

	/**
	 * @return current level
	 */
	public int getLevel() { return gameState.getLevel();}

	/**
	 * @return tile chap is standing on currently
	 */
	public Tile getTileAtChapPosition() { return maze.getTile(chap.getRow(), chap.getCol());}	
	
	/**
	 * @return current GameState
	 */
	public GameState getGameState() {return gameState;}
	
}

package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
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

	// most likely need to change mazeRows and mazeCols for the shape of the maze choose 
	/*public GameStateController(int mazeRows, int mazeCols, int startRow, int startCol, int totalTreasures) {
		if(mazeRows < 0 || mazeCols < 0 || startRow < 0 || startCol < 0 || totalTreasures < 0) {
			throw new IllegalArgumentException("Maze must have parameters above 0 to create properly");}
		if(startRow > mazeRows || startCol > mazeCols) {
			throw new IllegalArgumentException("Chap must spawn within the bounds of the maze");}
		
		// basic maze initially but will be extended for the maze we choose
        this.maze = Maze.createCustomMaze();
        this.chap = new Chap(startRow, startCol);
        this.gameState = new GameState(maze, chap, totalTreasures);
        
        assert mazeRows == maze.getRows() && mazeCols == maze.getCols();
        assert startRow == chap.getRow() && startCol == chap.getCol();
        } */

	@JsonIgnore
	private Maze maze;
	
	public GameStateController(@JsonProperty("gameState") GameState gameState) {
		if(gameState == null) {throw new IllegalArgumentException("GameState is null");}

		this.gameState = gameState;
		this.chap = gameState.getChap();
		this.maze = gameState.getMaze();
	}
	
	public void moveChap(Direction direction) {gameState.moveChap(direction);}
	public void moveActor() {gameState.enemies.forEach(a -> a.move(maze));}
	
	@JsonIgnore
	public List<Item> getChapInventory(){return gameState.getChap().inventory();}
	public Map<Key,String> getKeysCollected(){return gameState.keysCollected();}
	public int getTotalTreasures() {return gameState.totalTreasures();}
	public int getTreasuresCollected() {return gameState.getTreasuresCollected();}
	public boolean isAllTreasureCollected() {return gameState.allTreasureCollected();}
	 
	public Maze getMaze() {return gameState.getMaze();}
	public Chap getChap() {return gameState.getChap();}
	public List<Actor> getActors(){return gameState.enemies;}
	
	// Added by Adam
	public int getTime() {return gameState.getTime();}
	public void setTime(int time) {gameState.setTime(time);}

	public Tile getTileAtChapPosition() { return maze.getTile(chap.getRow(), chap.getCol());}	 
	public GameState getGameState() {return gameState;}
	/*
	// debugging method for returning the tiles around Chap REMOVE
	public String chapSurroundings() {
		return "To Chap's left is a: " + maze.getTile(chap.getRow(), chap.getCol() - 1).tileType() + "\n"
				+ "Above Chap is a " + maze.getTile(chap.getRow() -1, chap.getCol()).tileType() + "\n"
				+ "To Chap's right is a " + maze.getTile(chap.getRow(), chap.getCol() + 1).tileType() + "\n"
				+ "Below Chap is a " + maze.getTile(chap.getRow() + 1, chap.getCol()).tileType();
	}
	*/
}

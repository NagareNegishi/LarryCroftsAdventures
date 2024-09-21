package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

// Entry point for other modules to access 
public class GameStateController implements GameStateControllerInterface {

	@JsonProperty
	private Maze maze;
	@JsonProperty
	private GameState gameState;
	@JsonProperty
	private Chap chap;
	
	// most likely need to change mazeRows and mazeCols for the shape of the maze choose 
	public GameStateController(int mazeRows, int mazeCols, int startRow, int startCol, int totalTreasures) {
		// basic maze initially but will be extended for the maze we choose
        this.maze = Maze.createCustomMaze(mazeRows, mazeCols);
        this.chap = new Chap(startRow, startCol);
        this.gameState = new GameState(maze, chap, totalTreasures);
	}
	
	
	/**
	 * Constructor for Jackson de-serialization
	 * @param maze : Placement of tiles in game space
	 * @param chap : player character 
	 * @param gameState : contains all game information. Must also contain provided maze and chap
	 */
	@JsonCreator
	public GameStateController(@JsonProperty("maze") Maze maze,
							@JsonProperty("chap") Chap chap,
							@JsonProperty("gameState") GameState gameState) {
		this.maze = maze;
		this.chap = chap;
		this.gameState = gameState;
	}
	
	
	public void moveChap(Direction direction) {gameState.moveChap(direction);}
	public String getChapPosition() {return chap.getPosition();}
	 
	//public List<Item> getChapInventory(){return chap.inventory();}
	public void getChapInventory(){ chap.inventoryDescription();}
	 
	public int totalTreasures() {return gameState.totalTreasures();}
	public int getTreasuresCollected() {return gameState.getTreasuresCollected();}
	public boolean isAllTreasureCollected() {return gameState.allTreasureCollected();}
	 
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
}

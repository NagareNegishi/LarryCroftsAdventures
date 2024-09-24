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
	private Maze maze;
	@JsonProperty
	private GameState gameState;
	@JsonProperty
	private Chap chap;
	
	// change constructtor to avoid double use of maze
	
	// most likely need to change mazeRows and mazeCols for the shape of the maze choose 
	public GameStateController(int mazeRows, int mazeCols, int startRow, int startCol, int totalTreasures) {
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
	@JsonIgnore
	public List<Item> getChapInventory(){return chap.inventory();}
	public void getChapInventoryDesc(){ chap.inventoryDescription();}
	public Map<Key,String> getKeysCollected(){return gameState.keysCollected();}
	public int getTotalTreasures() {return gameState.totalTreasures();}
	public int getTreasuresCollected() {return gameState.getTreasuresCollected();}
	public boolean isAllTreasureCollected() {return gameState.allTreasureCollected();}
	 
	public Maze getMaze() {return maze;}
	public Chap getChap() {return chap;}
	 
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

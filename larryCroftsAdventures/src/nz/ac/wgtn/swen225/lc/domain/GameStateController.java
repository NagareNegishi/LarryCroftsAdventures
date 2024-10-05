package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

// Entry point for other modules to access 
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStateController implements GameStateControllerInterface {


	@JsonProperty
	private GameState gameState;
	@JsonIgnore
	private Chap chap;

	// change constructor to avoid double use of maze
	
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

	@JsonIgnore
	private Maze maze;


	
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
		if(maze.equals(null) || chap.equals(null) || gameState.equals(null)) {
			throw new IllegalArgumentException("Can't create new GameStateController with null parameters");}
		
		this.maze = maze;
		this.chap = chap;
	}

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

//////////////////////////////////////////////////////////////////////////////////
//////////////////attempt to inject AppNotifer into GameStateController/////////////
////////////////// maybe what need though method is GameState, not GameStateController??//////////
public AppNotifier appNotifier;
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

///////////////////////////////////////////////////////////////////////////////////////



}

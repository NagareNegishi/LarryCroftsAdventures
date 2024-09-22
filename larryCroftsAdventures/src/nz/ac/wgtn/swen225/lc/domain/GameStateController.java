package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

// Entry point for other modules to access 
public class GameStateController {

	private Maze maze;
	private GameState gameState;
	private Chap chap;
	
	// most likely need to change mazeRows and mazeCols for the shape of the maze choose 
	public GameStateController(int mazeRows, int mazeCols, int startRow, int startCol, int totalTreasures) {
		if(mazeRows < 0 || mazeCols < 0 || startRow < 0 || startCol < 0 || totalTreasures < 0) {
			throw new IllegalArgumentException("Maze must have parameters above 0 to create properly");}
		if(startRow > mazeRows || startCol > mazeCols) {
			throw new IllegalArgumentException("Chap must spawn within the bounds of the maze");}
		
		// basic maze initially but will be extended for the maze we choose
        this.maze = Maze.createCustomMaze(mazeRows, mazeCols);
        this.chap = new Chap(startRow, startCol);
        this.gameState = new GameState(maze, chap, totalTreasures);
        
        assert mazeRows == maze.getRows() && mazeCols == maze.getCols();
        assert startRow == chap.getRow() && startCol == chap.getCol();
        }
	
	public void moveChap(Direction direction) {gameState.moveChap(direction);}
	public String getChapPosition() {return chap.getPosition();}
	 
	public List<Item> getChapInventory(){return chap.inventory();}
	public void getChapInventoryDesc(){ chap.inventoryDescription();}
	public Map<Key,String> getKeysCollected(){return gameState.keysCollected();}
	public int getTotalTreasures() {return gameState.totalTreasures();}
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

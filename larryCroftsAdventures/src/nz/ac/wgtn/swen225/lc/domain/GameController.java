package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;

// Entry point for other modules to access 
public class GameController {

	private Maze maze;
	private GameState gameState;
	private Chap chap;
	
	// most likely need to change mazeRows and mazeCols for the shape of the maze choose 
	public GameController(int mazeRows, int mazeCols, int startRow, int startCol, int totalTreasures) {
		// basic maze initially but will be extended for the maze we choose
        this.maze = Maze.createBasicMaze(mazeRows, mazeCols);
        this.chap = new Chap(startRow, startCol);
        this.gameState = new GameState(maze, chap, totalTreasures);
    }
	
	// Chap movement methods
	 public void moveChapUp() {chap.moveUp(maze);}
	 public void moveChapDown() {chap.moveDown(maze);}
	 public void moveChapLeft() {chap.moveLeft(maze);}
	 
	 // currently being used for checking item pick up works
	 public void moveChapRight() {
		 chap.moveRight(maze);
		 gameState.checkForItem();
		 }
	 
	 public String getChapPosition() {return chap.getPosition();}
	 public List<Item> getChapInventory(){return chap.inventory();}
	 
	 public int totalTreasures() {return gameState.totalTreasures();}
	 public int treasuresCollected() {return gameState.treasureCollected();}
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
	 
	 // helper method that should be called when chap moves to check the tile he is moving to and what should happen
	 // when he moves to the tile e.g treasure tile should pick up the treasure and turn it to a freeTile.
	 public void onChapMove() {}
}

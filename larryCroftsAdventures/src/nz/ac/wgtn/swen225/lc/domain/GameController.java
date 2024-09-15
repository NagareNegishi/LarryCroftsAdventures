package nz.ac.wgtn.swen225.lc.domain;

// Entry point for other modules to access 
public class GameController {

	private Maze maze;
	private GameState gameState;
	private Chap chap;
	
	public GameController(int mazeRows, int mazeCols, int startRow, int startCol, int totalTreasures) {
		// basic maze initially but will be extended for the maze we choose
        this.maze = Maze.createBasicMaze(mazeRows, mazeCols);
        this.chap = new Chap(startRow, startCol);
        this.gameState = new GameState(maze, chap, totalTreasures);
    }
	
	 public void moveChapUp() {chap.moveUp(maze);}

	 public void moveChapDown() {chap.moveDown(maze);}

	 public void moveChapLeft() {chap.moveLeft(maze);}

	 public void moveChapRight() {chap.moveRight(maze);}
	 
	 public int[] getChapPosition() {return new int[] { chap.getRow(), chap.getCol() };}

	 public int treasuresCollected() {return gameState.treasureCollected();}

	 public boolean isAllTreasureCollected() {return gameState.allTreasureCollected();}

	 // still in the works
	 public void chapPickUpItem(Item item) {chap.pickUpItem(item);}
	    
	 public Tile getTileAtChapPosition() { return maze.getTile(chap.getRow(), chap.getCol());}
}

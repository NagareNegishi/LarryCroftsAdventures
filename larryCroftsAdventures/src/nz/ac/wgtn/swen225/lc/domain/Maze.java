package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Maze {

	// could be renamed to grid or similar if confusing
	private Tile[][] maze;
	@JsonProperty
	private int rows;
	@JsonProperty
    private int cols;
	
	public Maze(int rows, int cols) {
		if(rows < 0 || cols < 0) {throw new IllegalArgumentException("Maze must be created with more than 0 rows and cols");}
		this.rows = rows;
		this.cols = cols;
		maze = new Tile[rows][cols];
		}
	
	@JsonCreator
    public Maze(@JsonProperty("maze") Tile[][] maze, 
    			@JsonProperty("rows") int rows, 
                @JsonProperty("cols") int cols) {
        this.maze = maze;
        this.rows = rows;
        this.cols = cols;
    }
	
	public int getRows() { return rows; }
    public int getCols() { return cols; }
    
	// retrieve Tile that is at specified coords
	public Tile getTile(int row, int col) {return maze[row][col];}
	
	// set tile of maze to a specified tile, will add an assert to check inside method
	public void setTile(int row, int col, Tile tile) {
		if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
	        throw new IllegalArgumentException("Row or column is outside the bounds of the maze.");
	    }
		maze[row][col] = tile;
		// TODO: assert to check tile is correctly inserted
		// assert tile instanceof ...
	}
	
	public boolean validMove(int row, int col) {return maze[row][col].canMoveTo();}
	
	// helper method for creating a custom maze for testing different tiles
	public static Maze createCustomMaze(int rows, int cols) {
		Maze maze = new Maze(rows, cols);
		for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row == 0 || row == rows -1 || col == 0 || col == cols -1) {
                    maze.setTile(row, col, new WallTile());
                } 
                //creating a Tile for testing
                else if(row == 5 && col == 3) {
                	maze.setTile(row, col, new LockedDoorTile("Blue"));}
                // creating a Tile for testng
                else if(row == 4 && col == 3) {
                	maze.setTile(row, col, new KeyTile(new Key("Blue")));
                }else {
                    maze.setTile(row, col, new FreeTile());
                }
            }
        }
		return maze;
	}
	
	// helper method for creating a square maze with FreeTiles in the middle and WallTiles on the outside
	public static Maze createBasicMaze(int rows, int cols) {
		Maze maze = new Maze(rows, cols);
		for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row == 0 || row == rows -1 || col == 0 || col == cols -1) {
                    maze.setTile(row, col, new WallTile());    
                }else {
                    maze.setTile(row, col, new FreeTile());
                }
            }
		}
                return maze;
	}
	
}

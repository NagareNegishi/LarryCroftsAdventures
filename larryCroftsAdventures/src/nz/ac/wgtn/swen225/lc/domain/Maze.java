package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Maze class represents a 2D array of tiles used in the game for each Level, also holds information for each tile 
 * in the array and includes method to access that information.
 * 
 * @author fergusbenj1 300656321 
 */

public class Maze {


	@JsonProperty
	private Tile[][] maze;
	@JsonProperty
	private int rows;
	@JsonProperty
    private int cols;
	
	public Maze(int rows, int cols) {
		if(rows < 0 || cols < 0) {
			throw new IllegalArgumentException("Maze must be created with more than 0 rows and cols");}
		
		this.rows = rows;
		this.cols = cols;
		maze = new Tile[rows][cols];
		
		assert rows > 0 && cols > 0;
		}
	
	@JsonCreator
    public Maze(@JsonProperty("maze") Tile[][] maze, 
    			@JsonProperty("rows") int rows, 
                @JsonProperty("cols") int cols) {
		if(rows < 0 || cols < 0 || maze == null) {
			throw new IllegalArgumentException("Rows and Cols must be greater than 0  and 2d array cannot be nullto create maze");}
		
        this.maze = maze;
        this.rows = rows;
        this.cols = cols;
        
        assert rows > 0 && cols > 0;
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
	}
	
	public boolean validMove(int row, int col) {
		if (row < 0 || row >= rows || col < 0 || col >= cols) {
			System.err.println("Invalid move: row or column is outside the bounds of the maze.");
			return false;
		}
		return maze[row][col] != null && maze[row][col].canMoveTo();
		}
	
	//helper method for creating a square maze with FreeTiles in the middle and WallTiles on the outside
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

	// method for creating level 1
	public static Maze createLevel1() {
	    Maze maze = new Maze(17, 17);

	    for (int row = 0; row < 17; row++) {
	        for (int col = 0; col < 17; col++) {
	            // Set WallTile on the outer boundary
	            if (row == 0 || row == 16 || col == 0 || col == 16) {
	                maze.setTile(row, col, new WallTile());
	            } else {
	                // Set FreeTile for the inner area
	                maze.setTile(row, col, new FreeTile());
	            }
	        }
	    }
	    
	    // key tiles
	    maze.setTile(8, 14, new KeyTile(new Key("Blue")));
	    maze.setTile(8, 2, new KeyTile(new Key("Red")));
	    
	    // info tile
	    maze.setTile(10, 8, new InfoFieldTile("TEST"));

	    // create 7x7 wall rooms in each corner
	    // Top-left corner 
	    createRoom(maze, 0, 0);
	    maze.setTile(6, 3, new LockedDoorTile("Blue"));
	    maze.setTile(2, 2, new TreasureTile());
	    
	    // Top-right corner 
	    createRoom(maze, 0, 10);
	    maze.setTile(6, 13, new LockedDoorTile("Red"));
	    maze.setTile(2, 14, new TreasureTile());
	    
	    // Bottom-left corner 
	    createRoom(maze, 10, 0);
	    maze.setTile(13, 6, new LockedDoorTile("Blue"));
	    maze.setTile(13, 3, new TreasureTile());
	    
	    // Bottom-right corner 
	    createRoom(maze, 10, 10);
	    maze.setTile(13, 10, new LockedDoorTile("Red"));
	    maze.setTile(13, 13, new TreasureTile());
	    
	    // Create final room to exit at the top of the maze
	   
	    // layer with a ExitLockTile
	    maze.setTile(4, 7, new WallTile());
	    maze.setTile(4, 8, new ExitLockTile());
	    maze.setTile(4, 9, new WallTile());
	
	    // adding exit
	    maze.setTile(2, 8, new Exit());
	    
	    return maze;
	}

	// helper method to create a 7x7 room with walls at the specified start position
	private static void createRoom(Maze maze, int startRow, int startCol) {
	    // Create the outer walls (7x7 area)
	    for (int row = startRow; row < startRow + 7; row++) {
	        for (int col = startCol; col < startCol + 7; col++) {
	            // Set WallTile on the edges (outer boundary)
	            if (row == startRow || row == startRow + 6 || col == startCol || col == startCol + 6) {
	                maze.setTile(row, col, new WallTile());
	            } else {
	                // Set FreeTile inside the room (inner 5x5 area)
	                maze.setTile(row, col, new FreeTile());
	            }
	        }
	    }
	}
	
	// helpful method to print maze using tile initials to check design is accurate
	public void printMaze() {
		for (int i = 0; i<rows; i++) {
			for (int j = 0; j<cols; j++) {
				System.out.print(maze[i][j].initial());
			}
			System.out.println();
		}
	}

	
}

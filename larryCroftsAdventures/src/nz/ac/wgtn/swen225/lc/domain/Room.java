package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;


/**
 * Set chunck ammount, predefined chuck methods that fill same size
 */

/**
 * Tool to help build a maze
 */
public class Room {
	
	public enum Direction{
		Up,
		Down,
		Left,
		Right;
	}
	
	private static final int rows = 9;
	private static final int cols = 9;
	
	
	private Room upRoom;
	private Room rightRoom;
	private Room downRoom;
	private Room leftRoom;
	
	
	Tile[][] tileArray;
	
		
	
	public int height;
	public int width;
	
	// Placement of room top left corner in relation to origin
	static int top = 1;
	static int bottom = 0;
	static int leftMost = 0;
	static int rightMost = 1;

	
	/**
	 * 11 * 11 sized space leaves space for 9 * 9 empty space
	 */
	
	/**
	 * Each chunk is 9x9
	 * 1 space thick wall between chunks
	 * 1 thick wall around entire space
	 * 
	 */
	
	
	int chunk = 0;
	
	//Map<Coord, Tile> singleTiles;
	static List<Room> rooms = new ArrayList<>();
	List<Door> doors = new ArrayList<>();
	List<LockedDoor> lockedDoors = new ArrayList<>();
	
	/**
	 * Initialise the builder
	 */
	public Room() {
		this.height = 1;
		this.width = 1;
		
		this.tileArray = new Tile[rows][cols];
	}
	
	public Room(Tile[][] tiles ) {
		this.tileArray = tiles;
	}
	
	
	public Maze buildMaze() {
		// maze dimension in rooms
		int mazeWidth = rightMost - leftMost;
		int mazeHeight = top - bottom;
		
		int tileWidth = mazeWidth*10 + 1; 
		int tileHeight = mazeHeight*10 + 1;
		
		Tile[][] mazeTiles = new Tile[tileWidth][tileHeight];
		
		for (int row = 0; row < tileHeight; row++) {
	        for (int col = 0; col < tileWidth; col++) {
	            // Place wallTiles on the edges
	            if (row == 0 || row == tileHeight - 1 || col == 0 || col == tileWidth - 1) {
	                mazeTiles[row][col] = new WallTile();
	            } 
	            // Place wallTiles in the grid with 9x9 space between
	            else if (row % 10 == 0 || col % 10 == 0) {
	                mazeTiles[row][col] = new WallTile();
	            } 
	            
//	            else {
//	                mazeTiles[row][col] = new FreeTile();
//	            }
	        }
		}
		
		for (Room room : rooms) {
	        // Calculate the starting row and column for this room in the maze
	        int roomRowStart = (room.height - bottom) * 10 + 1;  // +1 to skip outer wall
	        int roomColStart = (room.width - leftMost) * 10 + 1;
	        for(Door door : room.doors) {
	        	switch (door.direction()) {
	            case Up:
	            	mazeTiles[roomRowStart - 1][roomColStart + 4] = door.tile();            break;
	            case Down:
	                mazeTiles[roomRowStart + rows][roomColStart + 4] = door.tile(); // Adjusted index	                break;
	            case Left:
	                mazeTiles[roomRowStart + 4][roomColStart - 1] = door.tile(); // Adjusted index	                break;
	            case Right:
	                mazeTiles[roomRowStart + 4][roomColStart + cols] = door.tile(); // Adjusted index	            	
	                break;
	    		}
	        }
	        // Place the room's tiles into the maze
	        for (int row = 0; row < rows; row++) {
	            for (int col = 0; col < cols; col++) {
	                mazeTiles[roomRowStart + row][roomColStart + col] = room.tileArray[row][col];
	            }
	        }
	    }
	
		for(LockedDoor door : lockedDoors) {
			mazeTiles[door.row()][door.col()] = new LockedDoorTile(door.colour(), true);
		}
		return new Maze(mazeTiles, tileWidth, tileHeight);
	}
	
	
	/**
	 * Creates room with single key in the middle
	 * @param direction
	 * @param colour
	 * @return
	 */
	public Room keyRoom(Direction direction, String colour) {
		
		Tile[][] tiles = new Tile[rows][cols];
		freeFill(tiles);
		Key key = new Key(colour);
		tiles[3][3] = new KeyTile(key);
		Room newRoom = new Room(tiles);
		setDir(direction, newRoom, true);
		return newRoom;
	}
	
	/**
	 * Adds empty room
	 * @param direction, direction from previous room to add new room
	 * @return
	 */
	public Room addSimpleRoom(Direction direction) {
		Tile[][] tiles = new Tile[rows][cols];
		// Fills with empty space
		freeFill(tiles);
		Room newRoom = new Room(tiles);
		this.doors.add(new Door(direction, new FreeTile()));
		setDir(direction, newRoom, true);
		return newRoom;
	}
	
	
	private void freeFill(Tile[][] tiles) {
		for(Tile[] row : tiles) {
			Arrays.fill(row,  new FreeTile());
		}		
	}

	private void setDir(Direction direction, Room newRoom, boolean door) {
		rooms.add(newRoom);
		switch (direction) {
        case Up:
        	newRoom.height = this.height++;
        	newRoom.width = this.width;
        	if(top < newRoom.height) { top = newRoom.height;}
        	this.upRoom = newRoom;
            newRoom.downRoom = this;
            break;
        case Down:
        	newRoom.height = this.height--;
        	newRoom.height = this.width;
        	if(bottom > newRoom.height) { bottom = newRoom.height;}
            this.downRoom = newRoom;
            newRoom.upRoom = this;
            break;
        case Left:
        	newRoom.width = this.width--;
        	newRoom.height = this.height;
        	if(leftMost > newRoom.width) { leftMost = newRoom.width;}
            this.leftRoom = newRoom;
            newRoom.rightRoom = this;
            break;
        case Right:
        	newRoom.width = this.width++;
        	newRoom.height = this.height;
        	if(rightMost < newRoom.width) { rightMost = newRoom.width;}
            this.rightRoom = newRoom;
            newRoom.leftRoom = this;
            break;
		}
	}
	
	
	
	/**
	 * Complete build process
	 * @return
	 */
//	public Maze build() {
//		Maze maze = new Maze(rows, cols);
//
//		for(Room room : rooms) {
//			Maze.createRoom(maze, room.row(), room.col(),room.width(), room.height());
//		}
//	}
}

record LockedDoor(int row, int col, String colour) {}
record Door(Room.Direction direction, Tile tile) {}

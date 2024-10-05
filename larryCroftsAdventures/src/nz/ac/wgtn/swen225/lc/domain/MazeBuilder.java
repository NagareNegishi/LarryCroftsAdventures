package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Set chunck ammount, predefined chuck methods that fill same size
 */

/**
 * Tool to help build a maze
 */
public class MazeBuilder {
	
	List<List<Tile>> flexMaze;
	int rows;
	int cols;
	
	/**
	 * 11 * 11 sized space leaves space for 9 * 9 empty space
	 */
	
	int chunk;
	
	Map<Coord, Tile> singleTiles; 
	List<Room> rooms = new ArrayList<>();
	
	
	/**
	 * Initialise the builder
	 */
	public MazeBuilder(int chunk) {
		flexMaze = new ArrayList<List<Tile>>();
		this.chunk = chunk;
	}
	
	
	/**
	 * Complete build process
	 * @return
	 */
	public Maze build() {
		Maze maze = new Maze(rows, cols);
		
		for(Room room : rooms) {
			Maze.createRoom(maze, room.row(), room.col(),room.width(), room.height());
		}
	}
}

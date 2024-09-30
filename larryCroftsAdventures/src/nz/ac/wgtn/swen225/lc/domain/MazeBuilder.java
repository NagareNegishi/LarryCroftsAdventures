package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tool to help build a maze
 */
public class MazeBuilder {
	
	List<List<Tile>> flexMaze;
	int rows;
	int cols;
	
	Map<Coord, Tile> singleTiles; 
	
	List<Room> rooms = new ArrayList<>();
	
	
	/**
	 * Initialise the builder
	 */
	public MazeBuilder() {
		flexMaze = new ArrayList<List<Tile>>();
	}
	
	
	/**
	 * Complete build process
	 * @return
	 */
	public Maze build() {
		Maze maze = new Maze(rows, cols);
		
		for(Room room : rooms) {
			Maze.createRoom(maze, room.row(), room.col(),room.width(), room.height())_
		}
		
		
	}
}

package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;


/**
 * Set chunck ammount, predefined chuck methods that fill same size
 */

/**
 * Tool to help build a maze
 */
public class MazeBuilder {
	
	public enum Direction{
		Up,
		Down,
		Left,
		Right;
	}
	
	
	
	List<List<Tile>> flexMaze;
	int rows;
	int cols;
	
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
	List<Room> rooms = new ArrayList<>();
	
	
	/**
	 * Initialise the builder
	 */
	public MazeBuilder() {
		flexMaze = new ArrayList<List<Tile>>();
	}
	
	
	public MazeBuilder chapRoom() {
		
		
	}
	
	public MazeBuilder addSimpleRoom(Direction direction) {
		
		
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

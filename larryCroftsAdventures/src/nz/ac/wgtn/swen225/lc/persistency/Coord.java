package nz.ac.wgtn.swen225.lc.persistency;


/**
 * Used to track row and col independent of useage (Room inside Builder, Tile within Room, Tile within Maze)
 * @author titheradam	300652933
 */
public record Coord(int row, int col) {
	public Coord{
		// -1 is a valid row/col used to refer to walls
		if(row < -1 || col < -1) {
			throw new IllegalArgumentException();
		}
	}
}

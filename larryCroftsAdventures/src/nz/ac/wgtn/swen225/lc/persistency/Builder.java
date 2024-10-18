package nz.ac.wgtn.swen225.lc.persistency;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import nz.ac.wgtn.swen225.lc.domain.FreeTile;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.WallTile;

/**
 * Fluid builder for nz.ac.wgtn.swen225.lc.domain.Maze
 * @author titheradam	300652933
 */

public class Builder {
	
	private static final int roomSize = 5;
	private final Coord topLeft = new Coord(0, 0);
	private Coord botRight = new Coord(0, 0);
	
	private Map<Coord, Room> roomMap = new HashMap<Coord, Room>();
	
	// Singleton for repeated Tiles
	static final WallTile wall = new WallTile();
	static final FreeTile freeTile = new FreeTile();
	
	
	public Builder() {}
	
	/**
	 * Adds room to builder list
	 * @param coord		Coord of room placement within maze
	 * @param room		Room obj to be placed
	 */
	public void addRoom(Coord coord, Room room) {
		
		if(coord.row() < 0 || coord.col() < 0) {
			throw new IllegalArgumentException("Negative room coord");
		}
		roomMap.put(coord, room);
		if(botRight.row() < coord.row()) { botRight = new Coord(coord.row(), botRight.col());} 
		if(botRight.col() < coord.col()) { botRight = new Coord(botRight.row(), coord.col()); }
	}
	
	
	/**
	 * Builds maze specified in this Builder
	 * @return domain.Maze for use in domain.GameState
	 */
	public Maze build() {
		int chunkRows = botRight.row();
		System.out.println("chunkRow: " + chunkRows);
		int chunkCols = botRight.col();
		System.out.println("chunkCol: " + chunkCols);
		int rows = 1 + (chunkRows + 1) * (roomSize+1);
		int cols = 1 + (chunkCols + 1) * (roomSize+1);
		
		
		System.out.println("Rows: " + rows);
		System.out.println("Cols: " + cols);

		Tile[][] tiles = new Tile[rows][cols];
		
		
		IntStream.range(0, tiles.length)
			.forEach(row -> IntStream.range(0, tiles[row].length)
				.forEach(col -> tiles[row][col] = wall));		
		
		roomMap.entrySet().forEach(es -> setRoom(tiles, es.getKey(), es.getValue()));
		
		return new Maze(tiles, rows, cols);
				
	}
	
	// Maps interior of room to maze tiles
	private void setRoom(Tile[][] tiles, Coord coord, Room room) {
		int firstRow = 1 + coord.row() * (roomSize+1);
		int firstCol = 1 + coord.col() * (roomSize+1);
		
		// Set interior as freeTiles
		IntStream.range(firstRow, firstRow + roomSize)
	    .forEach(row -> IntStream.range(firstCol, firstCol + roomSize)
	        .forEach(col -> tiles[row][col] = freeTile));
		
		// Set inner tiles
		room.innerTile.entrySet().forEach(
				e -> tiles[e.getKey().row() + firstRow][e.getKey().col() + firstCol] = e.getValue());
	}
	
	/**
	 * Gives absolute coord of tile within maze
	 * @param roomLoc		Coord of room within maze
	 * @param insideLoc		Coord of Tile within Room
	 * @return
	 */
	protected static Coord mazeLocation(Coord roomLoc, Coord insideLoc ) {
		if(roomLoc.row() < 0 || roomLoc.col() < 0) {
			throw new IllegalArgumentException("Room negative");
		}
		
		int roomRow = (roomLoc.row() * (roomSize+1)) + 1;
		int roomCol = (roomLoc.col() * (roomSize+1) + 1);
		
		int row = roomRow + insideLoc.row();
		int col = roomCol + insideLoc.col();
		
		return new Coord(row, col);
	}
}








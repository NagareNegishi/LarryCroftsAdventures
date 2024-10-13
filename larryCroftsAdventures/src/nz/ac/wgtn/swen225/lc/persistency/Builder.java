package nz.ac.wgtn.swen225.lc.persistency;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nz.ac.wgtn.swen225.lc.domain.FreeTile;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.WallTile;

public class Builder {
	
	private static final int roomSize = 5;

	
	Coord topLeft = new Coord(0, 0);
	Coord botRight = new Coord(0, 0);
	
	private Map<Coord, Room> roomMap = new HashMap<Coord, Room>();
	
	
	public Builder() {}
	
	public void addRoom(Coord coord, Room room) {
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
		
		// Fill tiles with walls
		for(int row = 0; row < tiles.length; row++) {
		    for(int col = 0; col < tiles[row].length; col++) {
		        tiles[row][col] = new WallTile();
		    }
		}
		
		for(Entry<Coord, Room> roomChunk : roomMap.entrySet()) {
			Coord coord = roomChunk.getKey();
			Room room = roomChunk.getValue();
			setRoom(tiles, coord, room);	
		}
		
		return new Maze(tiles, rows, cols);
				
	}
	
	// Maps interior of room to maze tiles
	private void setRoom(Tile[][] tiles, Coord coord, Room room) {
		int firstRow = 1 + coord.row() * (roomSize+1);
		int firstCol = 1 + coord.col() * (roomSize+1);
		
		for(int row = firstRow; row < firstRow + roomSize; row++) {
			for(int col = firstCol; col < firstCol + roomSize; col++) {
				tiles[row][col] = new FreeTile();
			}
		}
		// Set inner tiles
		for(Entry<Coord, Tile> innerTile : room.innerTile.entrySet()) {
			tiles[innerTile.getKey().row() + firstRow][innerTile.getKey().col() + firstCol] = innerTile.getValue();
		}
	}
}








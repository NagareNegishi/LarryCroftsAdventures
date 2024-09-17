package nz.ac.wgtn.swen225.lc.domain;

// super class of Tile that all tiles will extend
public abstract class Tile {

	public abstract boolean canMoveTo();
	
	// behaviour to define when Chap steps on tile
	public abstract void onMove(Chap chap);
	
	// return type could be String or Tile, leaving as string for debugging purposes for now
	public abstract String tileType();

	public abstract boolean hasItem();
	
	// method/methods to return x and y coords of a specific tile?
}


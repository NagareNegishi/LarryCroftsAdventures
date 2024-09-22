package nz.ac.wgtn.swen225.lc.domain;

// super class of Tile that all tiles will extend
public abstract class Tile {

	public abstract boolean canMoveTo();
	
	public abstract String tileType();

	public abstract boolean hasItem();
	
	// both these methods could be abstract, leaving as default methods for now as only treasure and key tiles need to use
	public Item getItem() {return null;}
	public  void removeItem() {}
}


package nz.ac.wgtn.swen225.lc.domain;

/**
 *@author fergusbenj1 300656321 
 */

public class TreasureTile extends Tile{
	
	//public static final String  YELLOW = "\u001B[33m"; 
	//public static final String RESET = "\u001B[0m";
		
	private Treasure treasure;
	
	// might not need since all TreasureTilees have a treasure and turn into FreeTile on collection
	private boolean hasTreasure;
	
	public TreasureTile() {
		this.treasure = new Treasure();
		hasTreasure = true;
	}
	
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {
		return hasTreasure? "Treasure Tile, has treasure = " + hasTreasure : "FreeTile, has treasure = " + hasTreasure;
	}

	@Override
	public boolean hasItem() {return hasTreasure;}
	
	@Override
	public Item getItem() {return treasure;}
	
	// using = null for now, can potentially use new NullItem() later
	@Override
	public void removeItem() {treasure = null;}

	@Override
	public String initial() {return "T ";}
}

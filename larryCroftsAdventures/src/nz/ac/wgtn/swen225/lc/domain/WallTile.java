package nz.ac.wgtn.swen225.lc.domain;

public class WallTile extends Tile{
	
	//public static final String RED = "\u001B[31m";
	//public static final String RESET = "\u001B[0m";
	
	@Override
	public boolean canMoveTo() {return false;}

	@Override
	public String tileType() {return "Wall Tile";}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {return "W ";}
}

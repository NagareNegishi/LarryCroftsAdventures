package nz.ac.wgtn.swen225.lc.domain;

/**
 * Water Tile that will end game and restart level if Chap touches it,
 * 
 *@author fergusbenj1 300656321 
 */

public class WaterTile extends Tile{
	
	public WaterTile() {}
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Water Tile";}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {return "D ";}
}
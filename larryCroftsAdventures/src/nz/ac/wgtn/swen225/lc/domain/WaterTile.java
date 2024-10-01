package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

// Tile for level 2....
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
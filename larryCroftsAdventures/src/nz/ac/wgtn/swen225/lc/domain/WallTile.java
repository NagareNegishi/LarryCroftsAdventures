package nz.ac.wgtn.swen225.lc.domain;

public class WallTile extends Tile{
	
	@Override
	public boolean canMoveTo() {return false;}

	@Override
	public String tileType() {return "Wall Tile";}

	@Override
	public boolean hasItem() {return false;}

}

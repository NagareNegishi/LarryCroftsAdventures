package nz.ac.wgtn.swen225.lc.domain;

public class FreeTile extends Tile{
	
	
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Free Tile";}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {return "F ";}

}

package nz.ac.wgtn.swen225.lc.domain;

public class Exit extends Tile {

	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Exit";}

	@Override
	public boolean hasItem() {return false;}

}

package nz.ac.wgtn.swen225.lc.domain;

public class Exit extends Tile {

	//public static final String CYAN = "\u001B[36m";
	//public static final String RESET = "\u001B[0m";
	
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Exit";}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {return "P ";}
}

package nz.ac.wgtn.swen225.lc.domain;

public class ExitLockTile extends Tile{
	
	//public static final String GREEN = "\u001B[32m";
	//public static final String RESET = "\u001B[0m";
	
	private boolean canMoveTo = false;
	private boolean locked = true;

	@Override
	public boolean canMoveTo() {return canMoveTo;}

	@Override
	public String tileType() {return "Exit-Lock Tile, locked = " + locked ;}

	public void unlock() {
		locked = false;
		canMoveTo = true;
		}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {return "E ";}
}

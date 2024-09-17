package nz.ac.wgtn.swen225.lc.domain;

//*** TODO ***//
public class zExitLockTileTODO extends Tile{
	
	private boolean locked = true;

	@Override
	public boolean canMoveTo() {
		return false;
	}

	@Override
	public String tileType() {return "Exit-Lock Tile, locked = " + locked ;}

	public void unLock() {locked = false;}

	@Override
	public boolean hasItem() {return false;}

}

package nz.ac.wgtn.swen225.lc.domain;

public class ExitLockTile extends Tile{
	
	private boolean locked = true;

	@Override
	public boolean canMoveTo() {
		return false;
	}

	@Override
	public void onMove(Chap chap) {
		// TODO: Implementation is like WallTile until all treasures are collected
	}

	@Override
	public String tileType() {return "Exit-Lock Tile, locked = " + locked ;}

	public void unLock() {locked = false;}

	@Override
	public boolean hasItem() {return false;}
}

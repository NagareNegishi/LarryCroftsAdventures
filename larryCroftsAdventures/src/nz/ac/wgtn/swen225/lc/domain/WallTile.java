package nz.ac.wgtn.swen225.lc.domain;

public class WallTile extends Tile{
	
	@Override
	public boolean canMoveTo() {return false;}

	@Override
	public void onMove(Chap chap) {
		// TODO: implement behaviour when Chap attempts to step on tile:
		// Chap doesn't move/moves back to previous tile
	}

	@Override
	public String tileType() {return "Wall Tile";}

	@Override
	public boolean hasItem() {return false;}

}

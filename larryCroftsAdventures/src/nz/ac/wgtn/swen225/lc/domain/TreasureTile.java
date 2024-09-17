package nz.ac.wgtn.swen225.lc.domain;

public class TreasureTile extends Tile{

	private boolean hasTreasure = true;
	
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public void onMove(Chap chap) {
		// TODO: implementation of Chap moving onto Tile:
		// Chap collects treasure and it is added to Chap's inventory
		// Treasure is removed from tile and tile is changed to FreeTile
	}

	@Override
	public String tileType() {
		return hasTreasure? "Treasure Tile, has treasure = " + hasTreasure : "FreeTile, has treasure = " + hasTreasure;
	}

	@Override
	public boolean hasItem() {return true;}

}

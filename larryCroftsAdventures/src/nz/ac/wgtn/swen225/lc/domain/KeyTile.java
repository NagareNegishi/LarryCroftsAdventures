package nz.ac.wgtn.swen225.lc.domain;

public class KeyTile extends Tile{
	
	private final Key key;
	private boolean keyCollected = false;
	
	public KeyTile(Key key) {this.key = key;}

	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public void onMove(Chap chap) {
		// TODO: implement behaviour for when Chap moves to tile:
		// Collect key
		// Add key to Chaps inventory
		// Make this tile into a FreeTile
	}

	@Override
	public String tileType() {
		return "Key Tile, contains a key of colour = " + key.colour() + ". Key collected = " + keyCollected;
	}

	@Override
	public boolean hasItem() {return true;}
}

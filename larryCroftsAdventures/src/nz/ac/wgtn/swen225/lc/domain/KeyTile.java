package nz.ac.wgtn.swen225.lc.domain;

public class KeyTile extends Tile{
	
	private Key key;
	private boolean keyCollected = false;
	
	public KeyTile(Key key) {this.key = key;}

	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {
		return "Key Tile, contains a key of colour = " + key.colour() +  ". Key collected = " + keyCollected;
	}

	@Override
	public boolean hasItem() {return true;}

	@Override
	public Item getItem() {return key;}
	@Override
	public void removeItem() {
		key = null;
		keyCollected = true;
		}

	@Override
	public String initial() {return "K ";}
	
}

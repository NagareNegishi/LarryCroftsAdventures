package nz.ac.wgtn.swen225.lc.domain;

public class LockedDoorTile extends Tile{
	
	private String colour;
	
	// field could be intialised as true or set to true through constructor... may change later
	private boolean locked = true;
	
	public LockedDoorTile(String colour) {this.colour = colour;}

	@Override
	public boolean canMoveTo() {return false;}

	@Override
	public void onMove(Chap chap) {
		// TODO: same implementation as WallTile until key with same colour is found
		
		// TODO: once key with same colour is in Chap's inventory, Chap can move onto Tile
		
		// TODO: once Chap moves onto Tile, turns into FreeTile	
	}

	@Override
	public String tileType() {
		return "Locked Door: Colour = " + colour + ". Locked = " + locked;
	}

	public boolean locked() {return locked;}
	public void unlock() {locked = false;}

	@Override
	public boolean hasItem() {return false;}
}

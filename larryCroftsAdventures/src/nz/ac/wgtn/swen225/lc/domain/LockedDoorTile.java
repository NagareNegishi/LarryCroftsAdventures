package nz.ac.wgtn.swen225.lc.domain;

public class LockedDoorTile extends Tile{
	
	private String colour;
	
	// field could be intialised as true or set to true through constructor... may change later
	private boolean locked;
	
	public LockedDoorTile(String colour) {
		this.colour = colour;
		this.locked = true;
		}

	// checks for if Chap has a key or not is delegated to the GameState class may change later
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Locked Door: Colour = " + colour + ". Locked = " + locked;}

	public boolean locked() {return locked;}
	public void unlock() {locked = false;}

	@Override
	public boolean hasItem() {return false;}
	
	public String colour() {return colour;}

}

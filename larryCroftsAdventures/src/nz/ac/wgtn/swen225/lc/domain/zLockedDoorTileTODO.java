package nz.ac.wgtn.swen225.lc.domain;

//*** TODO ***//
public class zLockedDoorTileTODO extends Tile{
	
	private String colour;
	
	// field could be intialised as true or set to true through constructor... may change later
	private boolean locked = true;
	
	//public LockedDoorTile(String colour) {this.colour = colour;}

	@Override
	public boolean canMoveTo() {return false;}

	@Override
	public String tileType() {
		return "Locked Door: Colour = " + colour + ". Locked = " + locked;
	}

	public boolean locked() {return locked;}
	public void unlock() {locked = false;}

	@Override
	public boolean hasItem() {return false;}

}

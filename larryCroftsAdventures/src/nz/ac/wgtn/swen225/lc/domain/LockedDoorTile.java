package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LockedDoorTile extends Tile{
	
	@JsonProperty
	private String colour;
	private boolean locked;
	
	public LockedDoorTile(String colour) {
		this.colour = colour;
		this.locked = true;
		}

	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Locked Door: Colour = " + colour + ". Locked = " + locked;}

	public boolean locked() {return locked;}
	public void unlock() {locked = false;}

	@Override
	public boolean hasItem() {return false;}
	public String colour() {return colour;}

	@Override
	public String initial() {return "L ";}
}

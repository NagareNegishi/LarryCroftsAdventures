package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class LockedDoorTile extends Tile{
	
	@JsonProperty
	private String colour;
	@JsonProperty
	private boolean locked;
	
	
	public LockedDoorTile(String colour) {
		this.colour = colour;
		this.locked = true;
		}
	
	@JsonCreator
	public LockedDoorTile(@JsonProperty("colour") String colour,
							@JsonProperty("locked") boolean locked) {
		this.colour = colour;
		this.locked = locked;
	}

	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Locked Door: Colour = " + colour + ". Locked = " + locked;}

	public boolean locked() {return locked;}
	public void unlock() {locked = false;}

	@JsonIgnore
	@Override
	public boolean hasItem() {return false;}
	public String colour() {return colour;}

	@Override
	public String initial() {return "L ";}
}

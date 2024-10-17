package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *@author fergusbenj1 300656321 
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class LockedDoorTile extends Tile{
	
	//public static final String RESET = "\u001B[0m";
	//public static final String GREEN = "\u001B[32m";.
	@JsonProperty
	private String colour;
	@JsonProperty
	private boolean locked;
	
	
	public LockedDoorTile(String colour) {
		if(colour == null) {throw new IllegalArgumentException("Cannot instantiate LockedDoorTile with null colour");}
		
		this.colour = colour;
		this.locked = true;
		
		assert this.colour.equals(colour);
		assert this.locked == true;
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

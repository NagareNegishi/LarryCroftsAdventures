package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author fergusbenj1 300656321
 */

public class ExitLockTile extends Tile{
	
	//public static final String GREEN = "\u001B[32m";
	//public static final String RESET = "\u001B[0m";
	
	@JsonProperty
	private boolean canMoveTo = false;
	@JsonProperty
	private boolean locked = true;

	
	public ExitLockTile() {}
	
	@JsonCreator
	public ExitLockTile(@JsonProperty("canMoveTo") boolean canMoveTo,
						@JsonProperty("locked") boolean locked) {
		this.canMoveTo = canMoveTo;
		this.locked = locked;
	}
	
	@Override
	public boolean canMoveTo() {return canMoveTo;}

	@Override
	public String tileType() {return "Exit-Lock Tile, locked = " + locked ;}

	public void unlock() {
		locked = false;
		canMoveTo = true;
		}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {return "E ";}
}

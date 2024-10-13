package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Super class that all tiles in the game will extend from.
 * @author fergusbenj1 300656321
 */

// Annotations for serialization
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FreeTile.class, name = "FreeTile"),
    @JsonSubTypes.Type(value = WallTile.class, name = "WallTile"),
    @JsonSubTypes.Type(value = LockedDoorTile.class, name = "LockedDoorTile"),
    @JsonSubTypes.Type(value = KeyTile.class, name = "KeyTile"),
    @JsonSubTypes.Type(value = TreasureTile.class, name = "TreasureTile"),
    @JsonSubTypes.Type(value = Exit.class, name = "Exit"),
    @JsonSubTypes.Type(value = ExitLockTile.class, name = "ExitLockTile"),
    @JsonSubTypes.Type(value = InfoFieldTile.class, name = "InfoFieldTile"),
    @JsonSubTypes.Type(value = WaterTile.class, name = "WaterTile"),
    @JsonSubTypes.Type(value = TeleportTile.class, name = "TeleportTile"),
    @JsonSubTypes.Type(value = Exit.class, name = "Exit")

})

@JsonIgnoreProperties(ignoreUnknown = true)

public abstract class Tile {
	
	
	public abstract boolean canMoveTo();

	@JsonIgnore
	public abstract String tileType();

	public abstract boolean hasItem();
	
	public Item getItem() {return null;}
	
	public  void removeItem() {}
	
	// purely to help print maze
	public abstract String initial();
}
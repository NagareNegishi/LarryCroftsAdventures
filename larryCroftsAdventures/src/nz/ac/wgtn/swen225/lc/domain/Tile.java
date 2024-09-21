package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FreeTile.class, name = "FreeTile"),
    @JsonSubTypes.Type(value = WallTile.class, name = "WallTile"),
    @JsonSubTypes.Type(value = LockedDoorTile.class, name = "LockedDoorTile"),
    @JsonSubTypes.Type(value = KeyTile.class, name = "KeyTile")
})

// super class of Tile that all tiles will extend
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Tile {
	
	
	public abstract boolean canMoveTo();
	
	// return type could be String or Tile, leaving as string for debugging purposes for now
	public abstract String tileType();

	public abstract boolean hasItem();
	
	// both these methods could be abstract, leaving as default methods for now as only treasure and key tiles need to use
	public Item getItem() {return null;}
	public  void removeItem() {}
	
	// method/methods to return x and y coords of a specific tile?
}


package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Annotations for serialization
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FreeTile.class, name = "FreeTile"),
    @JsonSubTypes.Type(value = WallTile.class, name = "WallTile"),
    @JsonSubTypes.Type(value = LockedDoorTile.class, name = "LockedDoorTile"),
    @JsonSubTypes.Type(value = KeyTile.class, name = "KeyTile")
})

@JsonIgnoreProperties(ignoreUnknown = true)
//super class of Tile that all tiles will extend
public abstract class Tile {
	
	
	public abstract boolean canMoveTo();
	
	public abstract String tileType();

	public abstract boolean hasItem();
	
	// both these methods could be abstract, leaving as default methods for now as only treasure and key tiles need to use
	public Item getItem() {return null;}
	
	public  void removeItem() {}
	
	// purely to help print maze
	public abstract String initial();
}


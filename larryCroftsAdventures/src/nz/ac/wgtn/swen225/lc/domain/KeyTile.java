package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyTile extends Tile{
	
	 //public static final String RESET = "\u001B[0m"; 
	 //public static final String PURPLE = "\u001B[35m";
	
	@JsonProperty
	private Key key;
	private boolean keyCollected = false;
	
	@JsonCreator
	public KeyTile(Key key) {this.key = key;}

	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {
		return "Key Tile, contains a key of colour = " + key.colour() +  ". Key collected = " + keyCollected;
	}

	@Override
	public boolean hasItem() {return true;}

	@JsonIgnore
	@Override
	public Item getItem() {return key;}
	@Override
	public void removeItem() {
		key = null;
		keyCollected = true;
		}

	@Override	public String initial() {return "K " ;}
	
}

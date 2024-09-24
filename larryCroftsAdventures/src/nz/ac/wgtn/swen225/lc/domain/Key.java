package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Key implements Item{
	
	String itemType() {return "key";}	
	@JsonProperty
	private String colour;
	
	public Key() {}
	public Key(String colour) {this.colour = colour;}
	public String colour() {return this.colour;}

	@JsonIgnore
	@Override
	public String description() {return "Key, Colour = " + colour;}
}	
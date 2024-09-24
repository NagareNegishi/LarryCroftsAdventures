package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Key implements Item{
	
	@JsonProperty
	private String colour;
	@JsonIgnore
	public Key(String colour) {this.colour = colour;}
	@JsonIgnore
	public String colour() {return this.colour;}

	@JsonIgnore
	@Override
	public String description() {return "Key, Colour = " + colour;}
}	
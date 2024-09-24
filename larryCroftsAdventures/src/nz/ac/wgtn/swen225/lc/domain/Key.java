package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use =JsonTypeInfo.Id.NONE )

public class Key implements Item{
	
	private String colour;
	
	public Key() {}
	public Key(String colour) {this.colour = colour;}
	public String colour() {return this.colour;}

	@Override
	public String description() {return "Key, Colour = " + colour;}
}
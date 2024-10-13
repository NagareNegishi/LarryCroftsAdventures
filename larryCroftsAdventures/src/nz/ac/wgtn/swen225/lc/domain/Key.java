package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 *@author fergusbenj1 300656321 
 */

@JsonTypeInfo(use =JsonTypeInfo.Id.NONE )

public class Key implements Item{
	
	@JsonProperty
	private String colour;
	
	@JsonCreator
	public Key(@JsonProperty("colour") String colour) {this.colour = colour;}
	public String colour() {return this.colour;}

	@Override
	public String description() {return colour + " Key";}
}
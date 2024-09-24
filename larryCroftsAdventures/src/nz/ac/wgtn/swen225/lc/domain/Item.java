package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = Key.class),
	@JsonSubTypes.Type(value = Treasure.class)
})

public interface Item {
	
	// description method (may not be needed but could help with debugging)
	String description();

}

package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Treasure.class, name = "Treasure"),
    @JsonSubTypes.Type(value = Key.class, name = "Key")
})
@JsonIgnoreProperties(ignoreUnknown = true)


public interface Item {
	
	// description method (may not be needed but could help with debugging)
	String description();

}
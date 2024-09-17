package nz.ac.wgtn.swen225.lc.domain;

public class zKeyTODO implements Item{
	
	private String colour;
	
	//public Key(String colour) {this.colour = colour;}
	
	public String colour() {return this.colour;}

	@Override
	public String description() {
		return "Key, Colour = " + colour;
	}

}
	
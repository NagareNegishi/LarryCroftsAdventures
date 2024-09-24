package nz.ac.wgtn.swen225.lc.domain;

public class InfoFieldTile extends Tile{
	
	private String helpText;

	public InfoFieldTile(String helpText) {this.helpText = helpText;}

	@Override
	public boolean canMoveTo() {
		return true;
	}
	
	// (Applies to all tiles) could change method to just return tile name and add another
	// method to return description (helpText in this case) of tile
	@Override
	public String tileType() {
		return "Info-Field Tile with helpText: " + helpText;
	}

	@Override
	public boolean hasItem() {return false;}
	
	public String displayText() {return helpText;}

	@Override
	public String initial() {return "I";}
}

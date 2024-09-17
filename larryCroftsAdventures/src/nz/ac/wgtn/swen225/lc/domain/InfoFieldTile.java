package nz.ac.wgtn.swen225.lc.domain;

public class InfoFieldTile extends Tile{
	
	private String helpText;

	public InfoFieldTile(String helpText) {this.helpText = helpText;}

	@Override
	public boolean canMoveTo() {
		return true;
	}

	@Override
	public void onMove(Chap chap) {
		// TODO: pop-up window for help text displayed when Chap moves to tile
		
	}
	
	// (Applies to all tiles) could change method to just return tile name and add another
	// method to return description (helpText in this case) of tile
	@Override
	public String tileType() {
		return "Info-Field Tile with helpText: " + helpText;
	}

	@Override
	public boolean hasItem() {return false;}
}

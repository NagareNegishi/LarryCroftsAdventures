package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author fergusbenj1 300656321
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class TeleportTile extends Tile{

	
	//private TeleportTile partner;
	@JsonProperty
	private int row;
	@JsonProperty
	private int col;
	@JsonProperty
	private int teleportRow;
	@JsonProperty
	private int teleportCol;
	
	
	@JsonCreator
	public TeleportTile(@JsonProperty("row") int row,
						@JsonProperty("col") int col,
						@JsonProperty("teleportToRow") int teleportToRow,
						@JsonProperty("teleportToCol")int teleportToCol) {
		this.row = row;
		this.col = col;
		this.teleportRow = teleportToRow;
		this.teleportCol = teleportToCol;
	}
	
	public int teleportRow() {return teleportRow;}
	public int teleportCol() {return teleportCol;}
	
	public int row() {return row;}
	public int col() {return col;}
	
	//public void setPartner(TeleportTile partner) {this.partner = partner;}
	
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Teleport Tile";}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {	return "0 ";}
}
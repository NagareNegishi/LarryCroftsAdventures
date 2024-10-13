package nz.ac.wgtn.swen225.lc.domain;


/**
 * @author fergusbenj1 300656321
 */

public class TeleportTile extends Tile{
	
	private TeleportTile partner;
	private int row;
	private int col;
	
	public TeleportTile() {}

	public TeleportTile partner() {return partner;}
	
	public void row(int row) {this.row = row;}
	public void col(int col) {this.col = col;}
	
	public int row() {return row;}
	public int col() {return col;}
	
	public void setPartner(TeleportTile partner) {this.partner = partner;}
	
	@Override
	public boolean canMoveTo() {return true;}

	@Override
	public String tileType() {return "Teleport Tile";}

	@Override
	public boolean hasItem() {return false;}

	@Override
	public String initial() {	return "0 ";}
}
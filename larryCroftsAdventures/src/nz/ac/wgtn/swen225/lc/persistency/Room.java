package nz.ac.wgtn.swen225.lc.persistency;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Exit;
import nz.ac.wgtn.swen225.lc.domain.ExitLockTile;
import nz.ac.wgtn.swen225.lc.domain.TeleportTile;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.WallTile;
import nz.ac.wgtn.swen225.lc.domain.WaterTile;

/**
 * Composite class for persistency.Builder
 * Used to set rooms within the produced Maze 
 * @author titheradam	300652933
 */
public class Room {
	// Potential doors to room
    public final Coord left = new Coord(2, -1); // original top
    public final Coord right = new Coord(2, 5);
    public final Coord top = new Coord(-1, 2);
    public final Coord bot = new Coord(5, 2);
    
    public final Coord centre = new Coord(2, 2);
    
    // Internal tiles
    protected Map<Coord ,Tile> innerTile =  new HashMap<Coord, Tile>();
    
    public Room() {}
    
    public void setTile(Coord coord, Tile tile) {
    	innerTile.put(coord, tile);
    }
}


/**
 * Room with water around centre
 */
class WaterRoom extends Room {
	
	public WaterRoom(){
		super.innerTile.put(new Coord(1,1), new WaterTile());
		super.innerTile.put(new Coord(1, 2), new WaterTile());
		super.innerTile.put(new Coord(2, 1), new WaterTile());
		super.innerTile.put(new Coord(3, 1), new WaterTile());
		super.innerTile.put(new Coord(3, 2), new WaterTile());
	}
}


/**
 * 
 */
class PortalRoom extends Room {
	
	/**
	 * 
	 * @param self, Coord of room within maze
	 * @param dest, Coord of room within maze
	 */
	PortalRoom(Coord self, Coord dest){
		
		self = Builder.mazeLocation(self, this.centre);
		dest = Builder.mazeLocation(dest, this.centre);
		
		super.innerTile.put(this.centre, new TeleportTile(self.row(), self.col(), dest.row(), dest.col()));
	}
	
	
	/**
	 * Used to set the teleportTile in each PortalRoom as each other's destination
	 * @param other
	 * @return boolean : true if successful
	 * 					false if failed
	 */
	public boolean pairPortal(Room other) {
		if(other instanceof PortalRoom) {
			TeleportTile tele = ((TeleportTile) innerTile.get(centre));
			TeleportTile otherT = (TeleportTile) other.innerTile.get(centre);
			return true;
		}
		return false;
	}
}

// Used to place Room with exit tile and exitLockedDoor
class ExitRoom extends Room{
	ExitRoom(){
		super.innerTile.put(new Coord(1,1), new WallTile());
		super.innerTile.put(new Coord(2,1), new WallTile());
		super.innerTile.put(new Coord(3,1), new WallTile());
		super.innerTile.put(new Coord(1,2), new WallTile());
		super.innerTile.put(new Coord(3,2), new WallTile());
		super.innerTile.put(new Coord(1,3), new WallTile());
		super.innerTile.put(new Coord(3,3), new WallTile());
		
		super.innerTile.put(this.centre, new Exit());
		super.innerTile.put(new Coord(2,3), new ExitLockTile());
	}
	
}

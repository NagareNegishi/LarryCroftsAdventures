package nz.ac.wgtn.swen225.lc.persistency;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.WallTile;
import nz.ac.wgtn.swen225.lc.domain.WaterTile;

class Room {
	// Entry points to room
    private Tile top = new WallTile();
    private Tile bot = new WallTile();
    private Tile left = new WallTile();
    private Tile right = new WallTile();
    public final Coord centre = new Coord(2, 2);
    
    // Internal tiles
    protected Map<Coord ,Tile> innerTile =  new HashMap<Coord, Tile>();
    
    public Room() {}
    
    
    public void setTile(Coord coord, Tile tile) {
    	innerTile.put(coord, tile);
    }
    
    public void setTile(Direction dir, Tile tile) {
        switch (dir) {
            case Up:
                top = tile;
                break; // Add break to prevent fall-through
            case Down:
                bot = tile;
                break;
            case Left:
                left = tile;
                break;
            case Right:
                right = tile;
                break;
            default:
                return; // Default case if none of the directions match
        }
    }
    
    public enum Direction {
        Up,
        Down,
        Left,
        Right;
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
		super.innerTile.put(new Coord(4, 2), new WaterTile());
	}
}

package nz.ac.wgtn.swen225.lc.persistency;

import java.util.ArrayList;
import java.util.HashMap;

import nz.ac.wgtn.swen225.lc.domain.Actor;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.FreeTile;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Item;
import nz.ac.wgtn.swen225.lc.domain.Key;
import nz.ac.wgtn.swen225.lc.domain.KeyTile;
import nz.ac.wgtn.swen225.lc.domain.LockedDoorTile;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.TreasureTile;
import nz.ac.wgtn.swen225.lc.persistency.Room.Direction;


/**
 * Used to build level2.json file 
 * @author titheradam	300652933
 */
public class level2 {
	
	/**
	 * Creates / modifies level2.json in /saves folder
	 * @param args : Not used
	 */
	public static void main(String args[]) {
		
		
		Room chapRoom = new Room();
		//chapRoom.setTile(Direction.Right, new LockedDoorTile("Red")); // Locked door to right
		chapRoom.setTile(chapRoom.right, new LockedDoorTile("Red")); // Locked door to right
		chapRoom.setTile(new Coord(1,1), new TreasureTile());
		

		Key redKey = new Key("Red");
		KeyTile redTile = new KeyTile(redKey);
		//chapRoom.setTile(Direction.Left, new FreeTile());
		chapRoom.setTile(chapRoom.left, new FreeTile());
		chapRoom.setTile(chapRoom.top, new LockedDoorTile("Blue"));
		Key blueKey = new Key("Blue");
		KeyTile blueTile =  new KeyTile(blueKey);
		
		Room waterRoom = new WaterRoom();
		waterRoom.setTile(waterRoom.centre, blueTile);
		waterRoom.setTile(new Coord(4,4), new TreasureTile());
		
		Room leftRoom = new Room();
		leftRoom.setTile(leftRoom.centre, redTile);
		
		Room exitRoom = new ExitRoom();

		
		Builder build = new Builder();
		build.addRoom(new Coord(1, 0), leftRoom);
		build.addRoom(new Coord(1, 1), chapRoom);
		build.addRoom(new Coord(1, 2), waterRoom);
		build.addRoom(new Coord(2, 1), exitRoom);
		
		
		// ************** CURRENTLY WORING ************************
		Coord entryPortal = new Coord(0, 1);
		Coord destPortal = new Coord(2, 2);
		PortalRoom portal1 = new PortalRoom(entryPortal, destPortal);
		PortalRoom portal2 = new PortalRoom(destPortal, entryPortal);
		portal2.setTile(portal2.left, new FreeTile());
		
		build.addRoom(entryPortal, portal1);
		build.addRoom(destPortal, portal2);
		
		Maze maze = build.build();
		maze.printMaze();
		
		ArrayList<Actor> enemies = new ArrayList<Actor>();
		Coord leftRoomCentre = Builder.mazeLocation(new Coord(1, 0), leftRoom.centre);
		enemies.add(new Actor(leftRoomCentre.row()-1, leftRoomCentre.col()-1));
		Coord portalRoomCentre = Builder.mazeLocation(entryPortal, portal1.centre);
		enemies.add(new Actor(portalRoomCentre.row()-1, portalRoomCentre.col()-1));
		Chap chap = new Chap(10, 10, new ArrayList<Item>());
		GameState gs = new GameState(maze, chap, 2, 0, new HashMap<Key, String>() , 60, new MockAppNotifier(), enemies, 2);
		GameStateController gsc = new GameStateController(gs);
		
		boolean saved = SaveFile.saveGame("level2", gsc);
		
	}
}

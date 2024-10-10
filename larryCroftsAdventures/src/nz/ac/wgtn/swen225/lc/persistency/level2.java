package nz.ac.wgtn.swen225.lc.persistency;

import java.util.ArrayList;

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
import nz.ac.wgtn.swen225.lc.persistency.Room.Direction;



public class level2 {
	
	
	public static void main(String args[]) {
		
		
		Room chapRoom = new Room();
		//chapRoom.setTile(Direction.Right, new LockedDoorTile("Red")); // Locked door to right
		chapRoom.setTile(chapRoom.right, new LockedDoorTile("Red")); // Locked door to right

		Key redKey = new Key("Red");
		KeyTile redTile = new KeyTile(redKey);
		//chapRoom.setTile(Direction.Left, new FreeTile());
		chapRoom.setTile(chapRoom.left, new FreeTile());
		chapRoom.setTile(chapRoom.top, new LockedDoorTile("Blue"));
		Key blueKey = new Key("Blue");
		KeyTile blueTile =  new KeyTile(blueKey);
		
		Room waterRoom = new WaterRoom();
		waterRoom.setTile(waterRoom.centre, blueTile);
		
		Room leftRoom = new Room();
		leftRoom.setTile(leftRoom.centre, redTile);
		
		Builder build = new Builder();
		build.addRoom(new Coord(0, 0), leftRoom);
		build.addRoom(new Coord(0, 1), chapRoom);
		build.addRoom(new Coord(0, 2), waterRoom);
		
		Maze maze = build.build();
		maze.printMaze();
		
		
		Chap chap = new Chap(3, 10, new ArrayList<Item>());
		GameState gs = new GameState(maze, chap, 2, new MockAppNotifier());
		GameStateController gsc = new GameStateController(gs);
		
		boolean saved = SaveFile.saveGame("level2", gsc);
		
	}
}

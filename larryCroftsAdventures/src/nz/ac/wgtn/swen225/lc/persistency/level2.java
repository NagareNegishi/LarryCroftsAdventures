package nz.ac.wgtn.swen225.lc.persistency;

import java.util.ArrayList;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Item;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Room;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public class level2 {
	
	
	public static void main(String args[]) {
		
		Room chapRoom = new Room();
		Room simple = chapRoom.addSimpleRoom(Room.Direction.Right);
		
		Maze maze = simple.buildMaze();
		
		Chap chap = new Chap(3, 3, new ArrayList<Item>());
		GameState gs = new GameState(maze, chap, 2, new MockAppNotifier());
		GameStateController gsc = new GameStateController(gs);
		
		boolean saved = SaveFile.saveGame("level2", gsc);
		
	}
	
}

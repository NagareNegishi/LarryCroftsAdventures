package nz.ac.wgtn.swen225.lc.persistency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Item;
import nz.ac.wgtn.swen225.lc.domain.Key;
import nz.ac.wgtn.swen225.lc.domain.KeyTile;
import nz.ac.wgtn.swen225.lc.domain.Maze;

/**
 * Used to build level1.json file
 * @author titheradam	300652933
 */
public class Level1 {
	
	public static void main(String args[]) {
		int rows = 20;
		int cols = 20;
		
		Maze maze = Maze.createLevel1();		
		Chap chap = new Chap(13, 9, new ArrayList<Item>());
		GameState gs = new GameState(maze, chap, 4, 0, new HashMap<Key, String>(), 60, new MockAppNotifier(), new ArrayList<>(), 1);
		GameStateController gsc = new GameStateController(gs);

		Boolean saved = SaveFile.saveGame("level1", gsc);
		assert saved;
	}
}
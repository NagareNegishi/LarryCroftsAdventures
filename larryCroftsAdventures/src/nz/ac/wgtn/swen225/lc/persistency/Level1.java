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

public class Level1 {
	
	public static void main(String args[]) {
		int rows = 20;
		int cols = 20;
		
		Maze maze = Maze.createLevel1();
		
		//assert maze.getTile(4, 3) instanceof KeyTile : "Not keytile";
		
		Chap chap = new Chap(13, 9, new ArrayList<Item>());
		GameState gs = new GameState(maze, chap, 4, new HashMap<Key, String>(), 60, new MockAppNotifier(), new ArrayList<>(), 1);
		
		GameStateController gsc = new GameStateController(gs);


		Boolean saved = SaveFile.saveGame("level1", gsc);
		assert saved;
	}
}
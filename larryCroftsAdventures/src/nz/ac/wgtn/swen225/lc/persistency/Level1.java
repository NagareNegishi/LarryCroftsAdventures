package nz.ac.wgtn.swen225.lc.persistency;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.util.List;
>>>>>>> 9161806e1cc337cf9eec8c07e69eaa6f5d017820

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
		
		
<<<<<<< HEAD
		Chap chap = new Chap(2, 2, new ArrayList<Item>());
		GameState gs = new GameState(maze, chap, 2);
		
		GameStateController gsc = new GameStateController(gs);
=======
			
		Chap chap = new Chap(2, 2, null);
		GameState gs = new GameState(maze, chap, 2);
		
		GameStateController gsc = new GameStateController(gs);
		
>>>>>>> 9161806e1cc337cf9eec8c07e69eaa6f5d017820

		Boolean saved = SaveFile.saveGame("level1NEWTEST", gsc);
		assert saved;
	}
}  
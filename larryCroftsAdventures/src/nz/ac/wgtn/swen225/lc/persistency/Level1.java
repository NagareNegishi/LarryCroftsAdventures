package nz.ac.wgtn.swen225.lc.persistency;

import java.util.List;

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
		
		Maze maze = Maze.createCustomMaze();
		
		
		Item item = maze.getTile(4, 3).getItem();
		assert item != null : "isnull";
		//assert maze.getTile(4, 3) instanceof KeyTile : "Not keytile";
		
		
			
		Chap chap = new Chap(2, 2, null);
		GameState gs = new GameState(maze, chap, 2);
		
		GameStateController gsc = new GameStateController(gs);
		

		Boolean saved = SaveFile.saveGame("IntegrationEx", gsc);
		assert saved;
	}
}  
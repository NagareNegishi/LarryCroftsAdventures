package test.nz.ac.wgtn.swen225.lc.fuzz;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.app.MockController;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.ExitLockTile;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.LockedDoorTile;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.WallTile;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;
import test.nz.ac.wgtn.swen225.lc.domain.Tests;

/**
 * The fuzz testing class randomly generates input in order to "play" the game,
 * therefore allowing us to brute force many different game situations
 * It is enhanced with some intelligence - it will choose any tiles next to it that have not been
 * yet visited and keep track of all visited tiles
 * 
 * @author wenanth 300653874
 * 
 */

public class Fuzz {
	private static Random random;
	
	public Fuzz() {
		random = new Random(); 
	}
	
	public static void main(String[] args) {
		Fuzz fuzz = new Fuzz();
		fuzz.Test1();
		fuzz.Test2();
		
		
	}
	//test for level1
	@Test 
	public void Test1() {
		runTest("level1");
	}
	//test for level2
	@Test 
	public void Test2() {
		runTest("level2");
	}
	
	/**
	 * the main method which runs the fuzz test
	 * 
	 * @param a string which is used by LoadFile to make the level as a GameStateController
	 * 
	 */
	public void runTest(String levelName) {
		
		//definining variables for access later
		Set<Tile> visitedTiles = new HashSet<Tile>();
		List<Exception> exceptions = new ArrayList<Exception>();
		List<Tile> allTiles = new ArrayList<Tile>();
		//create the level
		GameStateController level = LoadFile.loadLevel(levelName).orElseThrow(IllegalArgumentException::new);
		MockController mockController = new MockController(level);
		Chap chap = mockController.stateController.getChap();
		Maze maze = mockController.stateController.getMaze();
		
		
		//timer, currently set to 30 seconds
		//note: System time was used, because Timeout annotation was not working in eclipse.
		long startTime = System.currentTimeMillis();
		long duration = 3000;
		
		//add the current tile as a base
		visitedTiles.add(mockController.stateController.getTileAtChapPosition());
		for (int i =0; i < maze.getRows(); i++) {
			for(int j=0; j<maze.getCols(); j++) {
				allTiles.add(maze.getTile(i, j));
			}
		}
		allTiles.stream()
		.filter(t->(t instanceof LockedDoorTile || t instanceof ExitLockTile))
		.forEach(t->visitedTiles.add(t));
		
		mockController.stateController.getMaze().printMaze();
		
		//iterate until timer reached
		while(System.currentTimeMillis() - startTime < duration) {
			//two maps - allowing us to convert from tile to direction and backwards
			Map<Tile, Chap.Direction> adjacent = Map.of(
					maze.getTile(chap.getRow(), chap.getCol() - 1), Chap.Direction.Left,
					maze.getTile(chap.getRow() -1, chap.getCol()), Chap.Direction.Up,
					maze.getTile(chap.getRow(), chap.getCol() + 1), Chap.Direction.Right,
					maze.getTile(chap.getRow() + 1, chap.getCol()), Chap.Direction.Down
					);
			Map<Chap.Direction, Tile> reverse = Map.of(
					Chap.Direction.Left, maze.getTile(chap.getRow(), chap.getCol() - 1),
					Chap.Direction.Up, maze.getTile(chap.getRow() -1, chap.getCol()),
					Chap.Direction.Right, maze.getTile(chap.getRow(), chap.getCol() + 1),
					Chap.Direction.Down, maze.getTile(chap.getRow() + 1, chap.getCol())
					);
			//filter the list to not yet visited directions
			List<Chap.Direction> notVisitedDirections =
					adjacent.entrySet().stream()
					.filter(t-> !visitedTiles.contains(t.getKey()))
					.map(t-> t.getValue())
					.toList();
			//if there are not yet visited directions, then move one of them
			if (notVisitedDirections.size() > 0) {
				Chap.Direction moveDirection = notVisitedDirections.get(random.nextInt(notVisitedDirections.size()));
				try {
					mockController.stateController.moveChap(moveDirection);
					visitedTiles.add(mockController.stateController.getTileAtChapPosition());
				} catch (IllegalArgumentException e) {
					visitedTiles.add(reverse.get(moveDirection));
					if (e.getMessage().contains("Cannot move")) {
					} else {
						System.out.println(e);
						System.out.println(" Current Pos:" + chap.getRow() + "," + chap.getCol());
					}
					
				} catch (Exception e) {
					exceptions.add(e);
				}
			//otherwise move randomly
			} else {
				Chap.Direction moveDirection = List.of(Chap.Direction.Left,
						Chap.Direction.Up,
						Chap.Direction.Right,
						Chap.Direction.Down
						).get(random.nextInt(4));
				try {
					mockController.stateController.moveChap(moveDirection);
				} catch (IllegalArgumentException e) {
					if (e.getMessage().contains("Cannot move")) {

					} else {
						System.out.println(e);
						System.out.println(" Current Pos:" + chap.getRow() + "," + chap.getCol());
					}
					
					
				} catch (Exception e) {
					exceptions.add(e);
				}
			}
			mockController.stateController.moveActor();
			
		}
		//print a message to console, indicating the tiles visited.
		System.out.println("Testing done for " + levelName +"!");
		System.out.println("Detected exceptions:");
		exceptions.stream().forEach(e-> System.out.println(e));
		if (exceptions.size() == 0) {
			System.out.println("NONE DETECTED!!! :)))");
		}
	}
}

package test.nz.ac.wgtn.swen225.lc.fuzz;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.app.MockController;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.ExitLockTile;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.LockedDoorTile;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.persistency.LoadFile;

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
	public static List<String> events = new ArrayList<String>();
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
		Chap chap = mockController.getChap();
		Maze maze = mockController.getMaze();
		
		
		//timer, currently set to 30 seconds
		//note: System time was used, because Timeout annotation was not working in eclipse.
		long startTime = System.currentTimeMillis();
		long duration = 30000;
		
		//add the current tile as a base
		visitedTiles.add(mockController.getTileAtChapPosition());
		//fill the list with all tiles in the maze
		allTiles.addAll(
			    IntStream.range(0, maze.getRows())
			        .boxed()
			        .flatMap(i -> IntStream.range(0, maze.getCols())
			            .mapToObj(j -> maze.getTile(i, j)))
			        .collect(Collectors.toList())
			);
		//now stream those tiles, and visit the locked ones. this ensures that chap will not get 
		//stuck trying to move to a locked tile that he cannot visit
		allTiles.stream()
		.filter(t->(t instanceof LockedDoorTile || t instanceof ExitLockTile))
		.forEach(t->visitedTiles.add(t));
	
		
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
					mockController.moveChap(moveDirection);
					visitedTiles.add(mockController.getTileAtChapPosition());
				} catch (IllegalArgumentException e) {
					visitedTiles.add(reverse.get(moveDirection));
					if (e.getMessage().contains("Cannot move")) {
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
					mockController.moveChap(moveDirection);
				} catch (IllegalArgumentException e) {
					if (e.getMessage().contains("Cannot move")) {

					}
				} catch (Exception e) {
					exceptions.add(e);
				}
			}
			
			//move the actors
			mockController.moveActor();
			
		}
		//print a message to console
		System.out.println("Testing done for " + levelName +"!");
		eventCounter("chap unlocked the door");
		eventCounter("chap tried the door");
		eventCounter("chap unlocked the exit");
		eventCounter("chap tried the exit");
		eventCounter("chap opened info ");
		eventCounter("chap exited, win");
		eventCounter("chap fell into water, lose");
		eventCounter("chap got teleported");
		eventCounter("chap got owned by enemy, lose");
		System.out.println("");
	}
	public void eventCounter(String event) {
		System.out.println(event + " count: " + events.stream().filter(s->s.contains(event)).count());

	}
}

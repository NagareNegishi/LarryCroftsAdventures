package test.nz.ac.wgtn.swen225.lc.fuzz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import nz.ac.wgtn.swen225.lc.app.MockController;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.Tile;


public class Fuzz {
	private static Random random;
	public Fuzz() {
		random = new Random(); 
	}
	
	public static void main(String[] args) {
		Fuzz fuzz = new Fuzz();
		fuzz.Test1();
	}
	@Test 
	public void Test1() {
		
		//definining variables for access later
		List<Tile> visitedTiles = new ArrayList<Tile>();
		//create the level
		GameStateController level = new GameStateController(7,7,2,2,0);
		MockController mockController = new MockController(level);
		Chap chap = mockController.update.getChap();
		Maze maze = mockController.update.getMaze();
		
		//timer, currently set to 3000ms or 3 seconds
		//note: System time was used, because Timeout annotation was not working in eclipse.
		long startTime = System.currentTimeMillis();
		long duration = 3000;
		
		//add the current tile as a base
		visitedTiles.add(mockController.update.getTileAtChapPosition());
		
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
					chap.move(moveDirection, maze);
					visitedTiles.add(mockController.update.getTileAtChapPosition());
					System.out.println("Chap moved " + moveDirection.name() + " Current Pos:" + chap.getPosition());
				} catch (IllegalArgumentException e) {
					visitedTiles.add(reverse.get(moveDirection));
					System.out.println("Chap could not move " + moveDirection.name());
				}
			//otherwise move randomly
			} else {
				Chap.Direction moveDirection = List.of(Chap.Direction.Left,
						Chap.Direction.Up,
						Chap.Direction.Right,
						Chap.Direction.Down
						).get(random.nextInt(4));
				try {
					chap.move(moveDirection, maze);
					System.out.println("Chap moved " + moveDirection.name() + " Current Pos:" + chap.getPosition());
				} catch (IllegalArgumentException e) {
					System.out.println("Chap could not move " + moveDirection.name());
				}
				
				
			}
			
		}
		//print a message to console, indicating the tiles visited.
		System.out.println("Testing done!");
		System.out.println(visitedTiles.size() + "/" + ((maze.getCols() * maze.getCols())-4) + "visitable tiles visited");
	}
	
}

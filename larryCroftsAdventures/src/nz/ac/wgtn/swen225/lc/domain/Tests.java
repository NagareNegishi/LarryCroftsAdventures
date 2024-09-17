/**package nz.ac.wgtn.swen225.lc.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class Tests {
	
	// test maze is of correct size that is intialised
		@Test
		public void testMazeSize() {
			Maze maze = new Maze(5,5);
			assertEquals(5, maze.getRows());
			assertEquals(5, maze.getCols());
		}
		
	// test Chap is on correct row and col at start of game
		@Test
		public void testChapStartingPosition() {
			Maze maze = new Maze(5,5);
			Chap chap = new Chap(3,3);
			assertEquals(3,chap.getRow());
			assertEquals(3,chap.getCol());
		}
		
	// test Chap has empty inventory when game begins
		@Test
		public void testChapStartingInventory() {
			Maze maze = new Maze(5,5);
			Chap chap = new Chap(3,3);
			assertTrue(chap.inventory().isEmpty());
		}
		
	// test Chap moves correctly one space over
		@Test
		public void testChapMoveIsCorrect() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.move(3, 3, maze);
			assertEquals(3,chap.getRow());
			assertEquals(3,chap.getCol());
		}
		
	// test Chap cannot move to WallTile
		@Test
		public void testChapCantMoveToWallTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(3,3);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(4,4, maze);
			});
		}
		
	// test Chap correctly moves up
		@Test
		public void testChapMoveUp() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.moveUp(maze);
			assertEquals(1,chap.getRow());
			assertEquals(2,chap.getCol());
		}
		
	// test Chap correctly moves down
		@Test
		public void testChapMoveDown() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.moveDown(maze);
			assertEquals(3,chap.getRow());
			assertEquals(2,chap.getCol());
		}
		
	// test Chap correctly moves left
		@Test
		public void testChapMoveLeft() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.moveLeft(maze);
			assertEquals(2,chap.getRow());
			assertEquals(1,chap.getCol());
		}
		
	// test Chap correctly moves right
		@Test
		public void testChapMoveRight() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.moveRight(maze);
			assertEquals(2,chap.getRow());
			assertEquals(3,chap.getCol());
		}
	
	// test Chap cannot move up into a WallTile
		@Test
		public void testChapCantMoveUpToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			//System.out.println("Tile at 0,0 is: " + maze.getTile(0, 0).tileType());
			Chap chap = new Chap(1,0);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.moveUp(maze);
			});
		}
		
	// test Chap cannot move down into a WallTile
		@Test
		public void testChapCantMoveDownToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(3,0);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.moveDown(maze);
			});
		}
		
	// test Chap cannot move left into a WallTile
		@Test
		public void testChapCantMoveLeftToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(0,1);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.moveLeft(maze);
			});
		}
		
	// test Chap cannot move right into a WallTile
		@Test
		public void testChapCantMoveRightToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(0,3);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.moveRight(maze);
			});
		}
		
	// test Chap picks up treasure and treasure is in inventory
	
	// test Chap picks up key and key is in inventory of correct colour
	
	// test Chap picks up a key AND a treasure and both are in iventory and provide descriptions
	
	// test KeyTile turns into FreeTile when Chap picks up the key
	
	// test once Chap picks up Treasure tile doesn't contain a treasure anymore
}
*/
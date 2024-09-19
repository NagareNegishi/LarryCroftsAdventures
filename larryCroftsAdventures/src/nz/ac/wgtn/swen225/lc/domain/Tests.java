package nz.ac.wgtn.swen225.lc.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

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
			chap.moveTo(3, 3, maze);
			assertEquals(3,chap.getRow());
			assertEquals(3,chap.getCol());
		}
		
	// test Chap cannot move to WallTile
		@Test
		public void testChapCantMoveToWallTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(3,3);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.moveTo(4,4, maze);
			});
		}
		
	// test Chap correctly moves up
		@Test
		public void testChapMoveUp() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.move(Direction.Up,maze);
			assertEquals(1,chap.getRow());
			assertEquals(2,chap.getCol());
		}
		
	// test Chap correctly moves down
		@Test
		public void testChapMoveDown() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.move(Direction.Down,maze);
			assertEquals(3,chap.getRow());
			assertEquals(2,chap.getCol());
		}
		
	// test Chap correctly moves left
		@Test
		public void testChapMoveLeft() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.move(Direction.Left,maze);
			assertEquals(2,chap.getRow());
			assertEquals(1,chap.getCol());
		}
		
	// test Chap correctly moves right
		@Test
		public void testChapMoveRight() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			chap.move(Direction.Right,maze);
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
				chap.move(Direction.Up,maze);
			});
		}
		
	// test Chap cannot move down into a WallTile
		@Test
		public void testChapCantMoveDownToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(3,0);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Down,maze);
			});
		}
		
	// test Chap cannot move left into a WallTile
		@Test
		public void testChapCantMoveLeftToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(0,1);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Left,maze);
			});
		}
		
	// test Chap cannot move right into a WallTile
		@Test
		public void testChapCantMoveRightToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(0,3);
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Right,maze);
			});
		}
		
	// test Chap picks up treasure and treasure is in inventory and treasure collected increases by one
		@Test
		public void testChapPicksUpTreasure() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new TreasureTile());
			test.moveChap(Direction.Right);
			assert test.getTreasuresCollected() == 1;
		    assert chap.inventory().get(0) instanceof Treasure;
		}
		
	// test treasure tile turns into free tile when treasure is picked up
		@Test
		public void testTreasureTileTurnsIntoFreeTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new TreasureTile());
			test.moveChap(Direction.Right);
			assert maze.getTile(2, 3) instanceof FreeTile;
		}

	// test true when treasures collected is same as total treasures
		@Test
		public void testAllTreasuresCollected() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new TreasureTile());
			test.moveChap(Direction.Right);
			assert test.allTreasureCollected() == true;
			}

	// test Chap picks up key and key is in inventory of correct colour
		@Test
		public void testChapCanPickUpKey() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			test.moveChap(Direction.Right);
			assert chap.inventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Blue");
			}
	// test Chap picks up a key AND a treasure and both are in inventory 
		@Test
		public void testChapCanPickUpKeyAndTreasure() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			maze.setTile(3, 3, new TreasureTile());
			test.moveChap(Direction.Right);
			test.moveChap(Direction.Down);
			assert chap.inventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Blue");
			assert chap.inventory().get(1) instanceof Treasure;
			assert test.getTreasuresCollected() == 1;
			}
		
	// test KeyTile turns into FreeTile when Chap picks up the key
		@Test
		public void testKeyTileTurnsIntoFreeTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			test.moveChap(Direction.Right);
			assert maze.getTile(2, 3) instanceof FreeTile;
		}
		
	// test Chap can unlock a LockedDoorTile when he has the correct colour Key, move to it and turn it into a FreeTile
		@Test
		public void testLockedDoorTileUnlocksAndTurnsIntoFreeTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			maze.setTile(3, 3, new LockedDoorTile("Blue"));
			test.moveChap(Direction.Right);
			assert chap.inventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Blue");
			assert maze.getTile(3, 3) instanceof LockedDoorTile;
			test.moveChap(Direction.Down);
			assert maze.getTile(3, 3) instanceof FreeTile;
			
		}
	// test Chap cannot move to a LockedDoorTile if he doesn't have a Key
		@Test
		public void testChapCantMoveToLockedDoorWithoutKey() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new LockedDoorTile("Blue"));
			assert maze.getTile(2, 3) instanceof LockedDoorTile;
			test.moveChap(Direction.Right);
			assert maze.getTile(2, 3) instanceof LockedDoorTile;
			assertEquals(2,chap.getRow());
			assertEquals(2,chap.getCol());

			
		}
	// test Chap cannot move to a LockedDoorTile if he has a key of a different colour
		@Test
		public void testChapCantMoveToLockedDoorWithWrongKey() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2);
			GameState test = new GameState(maze,chap,1);
			maze.setTile(2, 3, new KeyTile(new Key("Red")));
			maze.setTile(3, 3, new LockedDoorTile("Blue"));
			test.moveChap(Direction.Right);
			assert chap.inventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Red");
			assert maze.getTile(3, 3) instanceof LockedDoorTile;
			test.moveChap(Direction.Down);
			assert maze.getTile(3, 3) instanceof LockedDoorTile;
			assertEquals(2,chap.getRow());
			assertEquals(3,chap.getCol());
			
		}
}

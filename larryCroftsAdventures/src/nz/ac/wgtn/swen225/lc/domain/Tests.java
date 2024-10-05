package nz.ac.wgtn.swen225.lc.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;

/**
 * @author fergusbenj1 300656321
 */

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
			Chap chap = new Chap(3,3, new ArrayList<>());
			assertEquals(3,chap.getRow());
			assertEquals(3,chap.getCol());
		}
		
	// test Chap has empty inventory when game begins
		@Test
		public void testChapStartingInventory() {
			Maze maze = new Maze(5,5);
			Chap chap = new Chap(3,3, new ArrayList<>());
			assertTrue(chap.inventory().isEmpty());
		}
		
	// test Chap moves correctly one space over
		@Test
		public void testChapMoveIsCorrect() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			chap.moveTo(3, 3, maze);
			assertEquals(3,chap.getRow());
			assertEquals(3,chap.getCol());
		}
		
	// test Chap cannot move to WallTile
		@Test
		public void testChapCantMoveToWallTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(3,3, new ArrayList<>());
			assertThrows(IllegalArgumentException.class, () -> {
				chap.moveTo(4,4, maze);
			});
		}
		
	// test Chap correctly moves up
		@Test
		public void testChapMoveUp() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			chap.move(Direction.Up,maze);
			assertEquals(1,chap.getRow());
			assertEquals(2,chap.getCol());
		}
		
	// test Chap correctly moves down
		@Test
		public void testChapMoveDown() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			chap.move(Direction.Down,maze);
			assertEquals(3,chap.getRow());
			assertEquals(2,chap.getCol());
		}
		
	// test Chap correctly moves left
		@Test
		public void testChapMoveLeft() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			chap.move(Direction.Left,maze);
			assertEquals(2,chap.getRow());
			assertEquals(1,chap.getCol());
		}
		
	// test Chap correctly moves right
		@Test
		public void testChapMoveRight() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			chap.move(Direction.Right,maze);
			assertEquals(2,chap.getRow());
			assertEquals(3,chap.getCol());
		}
	
	// test Chap cannot move up into a WallTile
		@Test
		public void testChapCantMoveUpToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(1,0, new ArrayList<>());
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Up,maze);
			});
		}
		
	// test Chap cannot move down into a WallTile
		@Test
		public void testChapCantMoveDownToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(3,0, new ArrayList<>());
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Down,maze);
			});
		}
		
	// test Chap cannot move left into a WallTile
		@Test
		public void testChapCantMoveLeftToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(0,1, new ArrayList<>());
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Left,maze);
			});
		}
		
	// test Chap cannot move right into a WallTile
		@Test
		public void testChapCantMoveRightToWall() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(0,3, new ArrayList<>());
			assertThrows(IllegalArgumentException.class, () -> {
				chap.move(Direction.Right,maze);
			});
		}
		
	// test Chap picks up treasure and treasure is in inventory and treasure collected increases by one
		@Test
		public void testChapPicksUpTreasure() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			assert testController.getTreasuresCollected() == 1;
		}
		
	// test treasure tile turns into free tile when treasure is picked up
		@Test
		public void testTreasureTileTurnsIntoFreeTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			assert maze.getTile(2, 3) instanceof FreeTile;
		}

	// test true when treasures collected is same as total treasures
		@Test
		public void testAllTreasuresCollected() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			assert testController.isAllTreasureCollected() == true;
			}

	// test Chap picks up key and key is in inventory of correct colour
		@Test
		public void testChapCanPickUpKey() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new KeyTile(new Key("Blue")));
			assert maze.getTile(2,3) instanceof KeyTile;
			testController.moveChap(Direction.Right);
			assert testController.getChapInventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Blue");
			}
		
	// test Chap picks up a key AND a treasure and both are in inventory 
		@Test
		public void testChapCanPickUpKeyAndTreasure() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			maze.setTile(3, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			testController.moveChap(Direction.Down);
			assert testController.getChapInventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Blue");
			assert testController.getTreasuresCollected() == 1;
			}
		
	// test KeyTile turns into FreeTile when Chap picks up the key
		@Test
		public void testKeyTileTurnsIntoFreeTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			testController.moveChap(Direction.Right);
			assert maze.getTile(2, 3) instanceof FreeTile;
		}
		
	// test Chap can unlock a LockedDoorTile when he has the correct colour Key, move to it and turn it into a FreeTile
		@Test
		public void testLockedDoorTileUnlocksAndTurnsIntoFreeTile() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new KeyTile(new Key("Blue")));
			maze.setTile(3, 3, new LockedDoorTile("Blue"));
			testController.moveChap(Direction.Right);
			assert chap.inventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Blue");
			assert maze.getTile(3, 3) instanceof LockedDoorTile;
			testController.moveChap(Direction.Down);
			assert maze.getTile(3, 3) instanceof FreeTile;
			
		}
		
	// test Chap cannot move to a LockedDoorTile if he doesn't have a Key
		@Test
		public void testChapCantMoveToLockedDoorWithoutKey() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new LockedDoorTile("Blue"));
			assert maze.getTile(2, 3) instanceof LockedDoorTile;
			testController.moveChap(Direction.Right);
			assert maze.getTile(2, 3) instanceof LockedDoorTile;
			assertEquals(2,testController.getChap().getRow());
			assertEquals(2,testController.getChap().getCol());

			
		}
		
	// test Chap cannot move to a LockedDoorTile if he has a key of a different colour
		@Test
		public void testChapCantMoveToLockedDoorWithWrongKey() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new KeyTile(new Key("Red")));
			maze.setTile(3, 3, new LockedDoorTile("Blue"));
			test.moveChap(Direction.Right);
			assert chap.inventory().get(0) instanceof Key;
			Key key = (Key) chap.inventory().get(0);
			assert key.colour().equals("Red");
			assert maze.getTile(3, 3) instanceof LockedDoorTile;
			testController.moveChap(Direction.Down);
			assert maze.getTile(3, 3) instanceof LockedDoorTile;
			assertEquals(2,testController.getChap().getRow());
			assertEquals(3,testController.getChap().getCol());
			
		}
		
	// test Chap cannot move to ExitLockTile with treasure still remaining
		@Test
		public void testChapCantMoveToExitLockWithTreasuresRemaining() {
			Maze maze = Maze.createBasicMaze(5, 5);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new ExitLockTile());
			testController.moveChap(Direction.Right);
			assertEquals(2,testController.getChap().getRow());
			assertEquals(2,testController.getChap().getCol());
		}
		
	// test Chap can move to ExitLockTile once all treasures are collected
		@Test
		public void testChapCanMoveToExitLockWithAllTreasures() {
			Maze maze = Maze.createBasicMaze(6, 6);
			Chap chap = new Chap(2,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			maze.setTile(2, 3, new TreasureTile());
			maze.setTile(2, 4, new ExitLockTile());
			testController.moveChap(Direction.Right);
			assert test.allTreasureCollected();
			testController.moveChap(Direction.Right);
			assertEquals(2,testController.getChap().getRow());
			assertEquals(4,testController.getChap().getCol());
		}
		
	// test Actor moves in intended way
		@Test
		public void testActorMoves() {
			Maze maze = Maze.createBasicMaze(7, 7);
			// actor starts at (2,2) should circle back after moving in a square
			Actor actor = new Actor(2,2);
			actor.move(maze);
			assertEquals(2,actor.getRow());
			assertEquals(3,actor.getCol());
			actor.move(maze);
			assertEquals(2,actor.getRow());
			assertEquals(4,actor.getCol());
			actor.move(maze);
			assertEquals(3,actor.getRow());
			assertEquals(4,actor.getCol());
			actor.move(maze);
			assertEquals(4,actor.getRow());
			assertEquals(4,actor.getCol());
			actor.move(maze);
			assertEquals(4,actor.getRow());
			assertEquals(3,actor.getCol());
			actor.move(maze);
			assertEquals(4,actor.getRow());
			assertEquals(2,actor.getCol());
			actor.move(maze);
			assertEquals(3,actor.getRow());
			assertEquals(2,actor.getCol());
			actor.move(maze);
			assertEquals(2,actor.getRow());
			assertEquals(2,actor.getCol());
		}
		
	/* test when Actor touches Chap game ends
		@Test
		public void testGameEndWhenActorTouchesChap() {
			Maze maze = Maze.createBasicMaze(7, 7);
			Chap chap = new Chap(1,2);
			GameState test = new GameState(maze,chap,0, List.of(new Actor(1,3)));
			// add actor to 1,3
			// move chap right
			test.moveChap(Direction.Right);
			assertEquals(true, test.checkForEnemy());
		} */
		
	// helper method to print maze REMOVE THESE
		@Test
		public void testPrintMaze() {
			Maze maze = Maze.createLevel1();
			maze.printMaze();
		}
		
		@Test
		public void testPrintMaze1() {
			Maze maze = Maze.createCustomMaze();
			maze.printMaze();
		}
		
		@Test
		public void testPrintMaze2() {
			Maze maze = Maze.createCustomMaze2();
			maze.printMaze();
		}
	
		
	// test TeleportTiles work
		@Test
		public void testTeleportTiles() {
			Maze maze = Maze.createCustomMaze2();
			Chap chap = new Chap(1,2, new ArrayList<>());
			GameState test = new GameState(maze,chap,1);
			GameStateController testController = new GameStateController(test);
			testController.moveChap(Direction.Left);
			assertEquals(1,chap.getRow());
			assertEquals(4,chap.getCol());
		}
		
	// tests to check initialising objects incorrectly are caught correctly
		@Test
		public void testIncorrectChap() {
			assertThrows(IllegalArgumentException.class, () -> {
				Chap chap = new Chap(-1, -1, null);
			});
		}
		
		@Test
		public void testIncorrectMaze() {
			assertThrows(IllegalArgumentException.class, () -> {
				Maze maze = new Maze(-1, -1);
			});
		}
		
		@Test
		public void testIncorrectGameState() {
			assertThrows(IllegalArgumentException.class, () -> {
				GameState test = new GameState(null, null, -1, null, -1, null);
			});
		}
		@Test
		public void testIncorrectGameStateController() {
			assertThrows(IllegalArgumentException.class, () -> {
				GameStateController test = new GameStateController(null);
			});
		}
		
	// tests for checking tiles can be moved to or not initially
		@Test
		public void testTilesMoveBoolean() {
			Exit exit = new Exit();
			ExitLockTile exitLocktile = new ExitLockTile();
			FreeTile freeTile = new FreeTile();
			InfoFieldTile infoFieldTile = new InfoFieldTile("Test");
			KeyTile keyTile = new KeyTile(new Key("Blue"));
			LockedDoorTile lockedDoorTile = new LockedDoorTile("Red");
			TeleportTile teleportTile = new TeleportTile();
			TreasureTile treasureTile = new TreasureTile();
			WallTile wallTile = new WallTile();
			WaterTile waterTile = new WaterTile();
			
			assertEquals(true, exit.canMoveTo());
			assertEquals(false, exitLocktile.canMoveTo());
			assertEquals(true, freeTile.canMoveTo());
			assertEquals(true, infoFieldTile.canMoveTo());
			assertEquals(true, keyTile.canMoveTo());
			assertEquals(true, lockedDoorTile.canMoveTo());
			assertEquals(true, teleportTile.canMoveTo());
			assertEquals(true, treasureTile.canMoveTo());
			assertEquals(false, wallTile.canMoveTo());
			assertEquals(true, waterTile.canMoveTo());
		}
		
		@Test
		public void testTileTypes() {
			Exit exit = new Exit();
			ExitLockTile exitLocktile = new ExitLockTile();
			FreeTile freeTile = new FreeTile();
			InfoFieldTile infoFieldTile = new InfoFieldTile("Test");
			KeyTile keyTile = new KeyTile(new Key("Blue"));
			LockedDoorTile lockedDoorTile = new LockedDoorTile("Red");
			TeleportTile teleportTile = new TeleportTile();
			TreasureTile treasureTile = new TreasureTile();
			WallTile wallTile = new WallTile();
			WaterTile waterTile = new WaterTile();
			
			assertEquals("Exit", exit.tileType());
			assertEquals("Exit-Lock Tile, locked = true", exitLocktile.tileType());
			assertEquals("Free Tile", freeTile.tileType());
			assertEquals("Info-Field Tile with helpText: Test", infoFieldTile.tileType());
			assertEquals("Key Tile, contains a Blue Key", keyTile.tileType());
			assertEquals("Locked Door: Colour = Red. Locked = true", lockedDoorTile.tileType());
			assertEquals("Teleport Tile", teleportTile.tileType());
			assertEquals("Treasure Tile, has treasure = true", treasureTile.tileType());
			assertEquals("Wall Tile", wallTile.tileType());
			assertEquals("Water Tile", waterTile.tileType());
		}
		
}

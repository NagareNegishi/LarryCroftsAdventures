package test.nz.ac.wgtn.swen225.lc.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.domain.Actor;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.domain.TeleportTile;
import nz.ac.wgtn.swen225.lc.domain.TreasureTile;
import nz.ac.wgtn.swen225.lc.domain.WallTile;
import nz.ac.wgtn.swen225.lc.domain.WaterTile;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.domain.Exit;
import nz.ac.wgtn.swen225.lc.domain.ExitLockTile;
import nz.ac.wgtn.swen225.lc.domain.FreeTile;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.InfoFieldTile;
import nz.ac.wgtn.swen225.lc.domain.Key;
import nz.ac.wgtn.swen225.lc.domain.KeyTile;
import nz.ac.wgtn.swen225.lc.domain.LockedDoorTile;


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
			Chap chap = new Chap(3,3, new ArrayList<>());
			assertEquals(3,chap.getRow());
			assertEquals(3,chap.getCol());
		}
		
	// test Chap has empty inventory when game begins
		@Test
		public void testChapStartingInventory() {
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
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			assert testController.getTreasuresCollected() == 1;
		}
		
	// test treasure tile turns into free tile when treasure is picked up
		@Test
		public void testTreasureTileTurnsIntoFreeTile() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			assert testController.getMaze().getTile(2, 3) instanceof FreeTile;
		}

	// test true when treasures collected is same as total treasures
		@Test
		public void testAllTreasuresCollected() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			assert testController.isAllTreasureCollected() == true;
			}

	// test Chap picks up key and key is in inventory of correct colour
		@Test
		public void testChapCanPickUpKey() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new KeyTile(new Key("Blue")));
			assert testController.getMaze().getTile(2,3) instanceof KeyTile;
			testController.moveChap(Direction.Right);
			assert testController.getChapInventory().get(0) instanceof Key;
			Key key = (Key) testController.getChap().inventory().get(0);
			assert key.colour().equals("Blue");
			}
		
	// test Chap picks up a key AND a treasure and both are in inventory 
		@Test
		public void testChapCanPickUpKeyAndTreasure() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new KeyTile(new Key("Blue")));
			testController.getMaze().setTile(3, 3, new TreasureTile());
			testController.moveChap(Direction.Right);
			testController.moveChap(Direction.Down);
			assert testController.getChapInventory().get(0) instanceof Key;
			Key key = (Key) testController.getChap().inventory().get(0);
			assert key.colour().equals("Blue");
			assert testController.getTreasuresCollected() == 1;
			}
		
	// test KeyTile turns into FreeTile when Chap picks up the key
		@Test
		public void testKeyTileTurnsIntoFreeTile() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new KeyTile(new Key("Blue")));
			testController.moveChap(Direction.Right);
			assert testController.getMaze().getTile(2, 3) instanceof FreeTile;
		}
		
	// test Chap can unlock a LockedDoorTile when he has the correct colour Key, move to it and turn it into a FreeTile
		@Test
		public void testLockedDoorTileUnlocksAndTurnsIntoFreeTile() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new KeyTile(new Key("Blue")));
			testController.getMaze().setTile(3, 3, new LockedDoorTile("Blue"));
			testController.moveChap(Direction.Right);
			assert testController.getChap().inventory().get(0) instanceof Key;
			Key key = (Key) testController.getChap().inventory().get(0);
			assert key.colour().equals("Blue");
			assert testController.getMaze().getTile(3, 3) instanceof LockedDoorTile;
			testController.moveChap(Direction.Down);
			assert testController.getMaze().getTile(3, 3) instanceof FreeTile;
			
		}
		
	// test Chap cannot move to a LockedDoorTile if he doesn't have a Key
		@Test
		public void testChapCantMoveToLockedDoorWithoutKey() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new LockedDoorTile("Blue"));
			assert testController.getMaze().getTile(2, 3) instanceof LockedDoorTile;
			testController.moveChap(Direction.Right);
			assert testController.getMaze().getTile(2, 3) instanceof LockedDoorTile;
			assertEquals(2,testController.getChap().getRow());
			assertEquals(2,testController.getChap().getCol());

			
		}
		
	// test Chap cannot move to a LockedDoorTile if he has a key of a different colour
		@Test
		public void testChapCantMoveToLockedDoorWithWrongKey() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new KeyTile(new Key("Red")));
			testController.getMaze().setTile(3, 3, new LockedDoorTile("Blue"));
			testController.moveChap(Direction.Right);
			assert testController.getChap().inventory().get(0) instanceof Key;
			Key key = (Key) testController.getChap().inventory().get(0);
			assert key.colour().equals("Red");
			assert testController.getMaze().getTile(3, 3) instanceof LockedDoorTile;
			testController.moveChap(Direction.Down);
			assert testController.getMaze().getTile(3, 3) instanceof LockedDoorTile;
			assertEquals(2,testController.getChap().getRow());
			assertEquals(3,testController.getChap().getCol());
			
		}
		
	// test Chap cannot move to ExitLockTile with treasure still remaining
		@Test
		public void testChapCantMoveToExitLockWithTreasuresRemaining() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.getMaze().setTile(2, 3, new ExitLockTile());
			testController.moveChap(Direction.Right);
			assertEquals(2,testController.getChap().getRow());
			assertEquals(2,testController.getChap().getCol());
		}
		
	// test Chap can move to ExitLockTile once all treasures are collected
		@Test
		public void testChapCanMoveToExitLockWithAllTreasures() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			assertEquals(1,testController.getTotalTreasures());
			testController.getMaze().setTile(2, 3, new TreasureTile());
			testController.getMaze().setTile(2, 4, new ExitLockTile());
			testController.moveChap(Direction.Right);
			assert testController.isAllTreasureCollected();
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
		
	// test TeleportTiles work
		@Test
		public void testTeleportTiles() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			TeleportTile teleportTile = new TeleportTile(1,2,3,3);
			testController.getMaze().setTile(1, 2, teleportTile);
			testController.moveChap(Direction.Up);
			assertEquals(1, teleportTile.row());
			assertEquals(2, teleportTile.col());
			assertEquals(3, teleportTile.teleportRow());
			assertEquals(3, teleportTile.teleportCol());
			assertEquals(3,testController.getChap().getRow());
			assertEquals(3,testController.getChap().getCol());
		}
		
	// tests to check initialising objects incorrectly are caught correctly
		@Test
		public void testIncorrectChap() {
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
				Chap chap = new Chap(-1, -1, null);
			});
		}
		
		@Test
		public void testIncorrectMaze() {
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
				Maze maze = new Maze(-1, -1);
			});
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
				Maze maze = new Maze(null,-1, -1);
			});
		}
		
		@Test
		public void testIncorrectGameState() {
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
				GameState test = new GameState(null, null, -1, -1, null, -1, null,null,-1);
			});
		}
		@Test
		public void testIncorrectGameStateController() {
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
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
			TeleportTile teleportTile = new TeleportTile(4,3,5,5);
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
		
	// test tile types are accurate
		@Test
		public void testTileTypes() {
			Exit exit = new Exit();
			ExitLockTile exitLocktile = new ExitLockTile(false,true);
			FreeTile freeTile = new FreeTile();
			InfoFieldTile infoFieldTile = new InfoFieldTile("Test");
			KeyTile keyTile = new KeyTile(new Key("Blue"));
			LockedDoorTile lockedDoorTile = new LockedDoorTile("Red");
			TeleportTile teleportTile = new TeleportTile(4,3,5,5);
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
		
	// test game time can be set and returns accurately
		@Test
		public void testTime() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			testController.setTime(10);
			assertEquals(10,testController.getTime());
		}
	
	// check level 1 has proper design
		@Test
		public void testPrintLevelOne() {
			Maze level1 = Maze.createLevel1();
			level1.printMaze();
		}
		
	// check infoFieldTile prints its message
		@Test
		public void testInfoFieldPrints() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			InfoFieldTile infoFieldTile = new InfoFieldTile("Test");
			testController.getMaze().setTile(2, 3, infoFieldTile);
			testController.moveChap(Direction.Right);
			assertEquals("Test", infoFieldTile.displayText());
		}
		
	// check level returns accurately
		@Test
		public void testLevelIsAccurate() {
			GameState test = GameState.mockGameState();
			GameStateController testController = new GameStateController(test);
			assertEquals(1,testController.getLevel());
		}
		
	// check tiles correctly return boolean for holding item or not
		@Test
		public void testHasItemBooleans() {
			Exit exit = new Exit();
			ExitLockTile exitLocktile = new ExitLockTile();
			FreeTile freeTile = new FreeTile();
			InfoFieldTile infoFieldTile = new InfoFieldTile("Test");
			KeyTile keyTile = new KeyTile(new Key("Blue"));
			LockedDoorTile lockedDoorTile = new LockedDoorTile("Red");
			TeleportTile teleportTile = new TeleportTile(4,3,5,5);
			TreasureTile treasureTile = new TreasureTile();
			WallTile wallTile = new WallTile();
			WaterTile waterTile = new WaterTile();
			
			assertEquals(false, exit.hasItem());
			assertEquals(false, exitLocktile.hasItem());
			assertEquals(false, freeTile.hasItem());
			assertEquals(false, infoFieldTile.hasItem());
			assertEquals(true, keyTile.hasItem());
			assertEquals(false, lockedDoorTile.hasItem());
			assertEquals(false, teleportTile.hasItem());
			assertEquals(true, treasureTile.hasItem());
			assertEquals(false, wallTile.hasItem());
			assertEquals(false, waterTile.hasItem());
		}
	
}

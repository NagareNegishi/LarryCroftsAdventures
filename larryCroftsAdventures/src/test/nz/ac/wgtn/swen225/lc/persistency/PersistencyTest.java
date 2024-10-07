package test.nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.persistency.*;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Domain imports

public class PersistencyTest {

	private GameStateController genGsc() {
		Maze maze = Maze.createCustomMaze();
		Chap chap = new Chap(2, 2, new ArrayList<Item>());
		MockAppNotifier notif = new MockAppNotifier();
		GameState gs = new GameState(maze, chap, 2, notif);
		GameStateController gsc = new GameStateController(gs);
		return gsc;
	}
	
	
	/**
	 * Reflection of SaveFile.saveObj for testing purposes
	 * @param <T>
	 * @param fileName
	 * @param obj
	 * @return
	 */
	private <T> boolean saveObj(String fileName, T obj) {
		try {
	        Class<?>[] argClasses = new Class<?>[]{String.class, Object.class};
        	Method saveObj = SaveFile.class.getDeclaredMethod("saveObj", argClasses);
        	saveObj.setAccessible(true);
        	
        	return (Boolean)saveObj.invoke(null, fileName, obj);
    
        } catch(Exception e) {
        	e.printStackTrace();
        }
		return false;
	}
	
	
    // Test nested object mockGameState into JSON
//    @Test
//    public void gameStateToJson(){
//        GameController gm = new MockController(2, 2, 2, 1, 1);
//        assert SaveFile.saveGame("Test1", gm);
//    }
	
	/**
	 * Tests whether level1 is loadable from /levels
	 */
	@Test
    public void level1Level() {
    	Optional<GameStateController> gscOptionLevel = LoadFile.loadLevel(Paths.level1);
    	assert gscOptionLevel.isPresent();
    	GameStateController gscLevel = gscOptionLevel.get();
    }
	
	/**
	 * Tests whether level1 is loadable from /saves
	 */
	@Test
	public void level1Save() {
		Optional<GameStateController> gscOption = LoadFile.loadSave("level1");
		assert gscOption.isPresent();
    	GameStateController gsc = gscOption.get();	
	}

	/**
	 * Test for Juint
	 */
    @Test
    public void test() {
        Integer i = 0;
        assert i != null;
    }
    
    
    /**
     * Test for saveObj
     * Uses reflection as saveObj is private
     */
    @Test public void objTest() {
    	Seeds s = new Seeds(1000);

    	Canary c = new Canary("ObjBird", 1000, s);
        Class<?>[] argClasses = new Class<?>[]{String.class, Object.class};
    	
        try {
        	Method saveObj = SaveFile.class.getDeclaredMethod("saveObj", argClasses);
        	saveObj.setAccessible(true);
        	
        	assert (Boolean)saveObj.invoke(null, "ObjectTest", c);
    
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
   
    // Tests serialisation of Maze
    @Test public void mazeSave() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	assert saveObj("MazeTest", maze);
    	Maze maze2 = LoadFile.loadObj("MazeTest", Maze.class).get();
    }
    
    // Tests serialisation / deserialisation of Chap
    @Test public void ChapLoad() {
    	Chap chap = new Chap(2, 2, new ArrayList<Item>());
    	assert chap.getCol() == 2;
    	assert chap.getRow() == 2;
    	assert saveObj("ChapText", chap);
    	Optional<Chap> chapOption = LoadFile.loadObj("ChapText", Chap.class);
    	assert chapOption.isPresent();
    	Chap chapDeserial = chapOption.get();
    	assert chapDeserial.getCol() == chap.getCol();
    	assert chapDeserial.getRow() == chap.getRow();
    }
    
    
    @Test public void chapInventory() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	Chap chap = new Chap(2, 2, new ArrayList<Item>());
    	
    	Item redKey = new Key("red");
    	chap.pickUpItem(redKey);
    	MockAppNotifier notif = new MockAppNotifier();
		GameState gs = new GameState(maze, chap, 2, notif);
    	GameStateController gsc = new GameStateController(gs);

    	
    	
    	assert saveObj("inventoryTest", gsc);
    	Optional<GameStateController> gsOption = LoadFile.loadObj("inventoryTest", GameStateController.class);
    	assert gsOption.isPresent();
    	GameStateController gsDeserial = gsOption.get();
    	
    	List<Item> inventory = gsDeserial.getChapInventory();
    	assert inventory != null;
    	
    	for(Item item : inventory) {  System.out.println(item.description());}
    }
    
    
    // Tests serialisation of GameState
    @Test public void gameStateLoad() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	Chap chap = new Chap(2, 2, new ArrayList<Item>());
    	MockAppNotifier notif = new MockAppNotifier();
		GameState gs = new GameState(maze, chap, 2, notif);
    	assert saveObj("GameStateTest", gs);
    	Optional<GameState> gsOption = LoadFile.loadObj("GameStateTest", GameState.class);
    	assert gsOption.isPresent();
    	GameState gsDeserial = gsOption.get();
    	assert gsDeserial.totalTreasures() == gs.totalTreasures();
    }
    
    
    /**
     * Test serialisation of GameStateController
     */
//    @Test public void gameControlTest() {
//    	GameStateController gsc = new GameStateController(5, 5, 2, 2, 1);
//    	assert SaveFile.saveGame("GameSaveTest", gsc);
//    	
//    	Optional<GameStateController> gsOp = LoadFile.loadSave("GameSaveTest");
//    	assert gsOp.isPresent();
//    	GameStateController gscDeserial = gsOp.get();
//    }
    
     // Tests de-serialisation of GameStateController
//    @Test public void gameSaveLoadTest() {
//    	Maze maze = Maze.createBasicMaze(5, 5);
//    	Chap chap = new Chap(2, 2, new ArrayList<Item>());
//    	GameState gs = new GameState(maze, chap, 2);
//    	GameStateController gsc = new GameStateController(gs);
//    	
//    	assert SaveFile.saveObj("GameSaveLoadTest", gsc);
//    	
//    	Optional<GameStateController> gscOption = LoadFile.loadObj("GameSaveLoadTest", GameStateController.class);
//    	assert gscOption.isPresent();
//    	GameStateController gscDeserial = gscOption.get();
//    	
//    	//assert gscDeserial.getChapPosition().equals(chap.getPosition());
//    	
//    	// checking if same type of tile as hashcode prevents .equals directly
//    	assert gscDeserial.getTileAtChapPosition().getClass().equals(maze.getTile(2, 2).getClass());    	
//    }
    
    @Test 
    public void integrationTestInit() {
		
		Maze maze = Maze.createCustomMaze();
		Chap chap = new Chap(2, 2, new ArrayList<Item>());
		MockAppNotifier notif = new MockAppNotifier();
		GameState gs = new GameState(maze, chap, 2, notif);		
		GameStateController gsc = new GameStateController(gs);
		
		Boolean saved = SaveFile.saveGame("level1", gsc);
		assert saved;
		
		Optional<GameStateController> gscOption = LoadFile.loadSave("level1");
		assert gscOption.isPresent();
		GameStateController gscDeserial = gscOption.get();
    }
    
    
    @Test
    public void newTest() {
    	Maze maze = Maze.createCustomMaze();
    	Chap chap = new Chap(2,2, new ArrayList<Item>());
    	MockAppNotifier notif = new MockAppNotifier();
		GameState gs = new GameState(maze, chap, 2, notif);    	GameStateController gsc = new GameStateController(gs);
		
		Boolean saved = SaveFile.saveGame("IntegrationEx", gsc);
		assert saved;   
		
    }
    
    @Test
    public void pathTest() {
    	assert false : Paths.level1.getAbsolutePath();
    	
    }
    
    @Test
    public void workingController() {
    	
    GameStateController gsc = genGsc();
    
    assert SaveFile.saveGame("controlTest", gsc);
    
    Optional<GameStateController> gscO = LoadFile.loadSave("controlTest");
    assert gscO.isPresent();
    GameStateController gscD = gscO.get();
    
    //System.out.println(gscD.getChapPosition());
    gscD.moveChap(Direction.Right);
    //System.out.println(gscD.getChapPosition());
    gscD.moveChap(Direction.Left);
    //System.out.println(gscD.getChapPosition());
    gscD.getChap().moveTo(3, 3, gscD.getMaze());
    
    }
}
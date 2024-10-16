package test.nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.Chap.Direction;
import nz.ac.wgtn.swen225.lc.persistency.*;


import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// Domain imports

public class PersistencyTest {
	
	// Facory methods for common objects
	private Chap genChap() {
		return new Chap(2, 2, new ArrayList<Item>());
	}
	
	private GameState genGameState() {
		return new GameState(Maze.createCustomMaze(),
						genChap(),
						2,
						new HashMap<Key, String>(),
						60,
						new MockAppNotifier(),
						new ArrayList<Actor>(),
						0);
	}
	
	private GameStateController genGsc() {
		return new GameStateController(genGameState());
	}
	

	// Reflection of private method SaveFile.saveObj for testing purposes
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
	
	// Reflection of private method LoadFile.loadObj for testing purposes
	@SuppressWarnings("unchecked")
	private <T> Optional<T> loadObjFile(File file, Class<T> classType) {
		try {
	        Class<?>[] argClasses = new Class<?>[]{File.class, Class.class};
        	Method loadObj = LoadFile.class.getDeclaredMethod("loadObj", argClasses);
        	loadObj.setAccessible(true);
        	
        	return (Optional<T>)loadObj.invoke(null, file, classType);
    
        } catch(Exception e) {
        	e.printStackTrace();
        }
		return Optional.empty();
	}
	
	@SuppressWarnings("unchecked")
	private <T> Optional<T> loadObjString(String path, Class<T> classType){
		try{
			Class<?>[] argClasses = new Class<?>[] {String.class, Class.class};
			Method loadObj = LoadFile.class.getDeclaredMethod("loadObj", argClasses);
			loadObj.setAccessible(true);
			return (Optional<T>)loadObj.invoke(null, path, classType);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	
	// Test if JUnit is working as expected
    @Test
    public void test() {
        Integer i = 0;
        assert i != null;
    }
	
	
	 

    
	// Test for saveObj
    @Test 
    public void saveObjTest() {
    	Seeds s = new Seeds(1000);

    	Canary c = new Canary("ObjBird", 1000, s);
    	
    	assert saveObj("saveObj", c);
    }
    
    // Tests expected failure of saveObj
    @Test 
    public void saveObjFail() {
    	assert !saveObj("saveObj", null);
    }
    
    @Test 
    public void saveObjFail_2() {
    	Seeds s = new Seeds(1000);
    	Canary c = new Canary("ObjBird", 1000, s);
    	assert !saveObj("", c);
    }
    
    @Test 
    public void saveObjFail_3() {
    	assert saveObj("Fail", (Function<Integer, Integer>)((i)-> 1));
    }
    
    @Test public void saveObjFail_4() {
    	Seeds s = new Seeds(1000);
    	Canary c = new Canary("ObjBird", 1000, s);
    	assert !saveObj("/n", c);
    }
    
    @Test 
    public void saveGameFail() {
    	GameStateController gsc = genGsc();
    	assertThrows(IllegalArgumentException.class,()-> SaveFile.saveGame("", gsc));
    }
    
    @Test 
    public void saveGameFail_2() {
    	assertThrows(IllegalArgumentException.class, ()-> SaveFile.saveGame(null, genGsc()));	
    }
    
    @Test 
    public void saveGameFail_3() {
    	assertThrows(IllegalArgumentException.class, ()-> SaveFile.saveGame("NullGsc", null));
    }
    
    
    @Test
    public void loadObjStringTest() {
    	
    }
    
    
    // Tests whether level1 is loadable from /levels
 	@Test
     public void level1Level() {
     	Optional<GameStateController> gscOptionLevel = LoadFile.loadLevel(Paths.level1);
     	assert gscOptionLevel.isPresent();
     	GameStateController gscLevel = gscOptionLevel.get();
     }
 	
// 	@Test 
//    public void loadLevelTest() {
//    	
//    }
 	
 	
 	
 	// Tests whether level1 is loadable from /saves
 	@Test
 	public void level1Save() {
 		Optional<GameStateController> gscOption = LoadFile.loadSave("level1");
 		assert gscOption.isPresent();
     	GameStateController gsc = gscOption.get();	
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
		GameState gs = new GameState(maze, chap, 2, new HashMap<Key, String>(), 60, notif, new ArrayList<Actor>(), 0);
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
		GameState gs = new GameState(maze, chap, 2, new HashMap<Key, String>(), 60, notif, new ArrayList<Actor>(), 0);
    	assert saveObj("GameStateTest", gs);
    	Optional<GameState> gsOption = LoadFile.loadObj("GameStateTest", GameState.class);
    	assert gsOption.isPresent();
    	GameState gsDeserial = gsOption.get();
    	assert gsDeserial.totalTreasures() == gs.totalTreasures();
    }
    
    
    
    @Test 
    public void integrationTestInit() {
		
		Maze maze = Maze.createCustomMaze();
		Chap chap = new Chap(2, 2, new ArrayList<Item>());
		MockAppNotifier notif = new MockAppNotifier();
		GameState gs = new GameState(maze, chap, 2, new HashMap<Key, String>(), 60, notif, new ArrayList<Actor>(), 0);
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
		GameState gs = new GameState(maze, chap, 2, new HashMap<Key, String>(), 60, notif, new ArrayList<Actor>(), 0);
		GameStateController gsc = new GameStateController(gs);
		Boolean saved = SaveFile.saveGame("IntegrationEx", gsc);
		assert saved;   
		
    }
    
//    @Test
//    public void pathTest() {
//    	assert false : Paths.level1.getAbsolutePath();
//    }
    
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
    

    
    @Test
    public void runnableTest() {
		MockAppNotifier notif = new MockAppNotifier();
		notif.run();
		ArrayList<String> checkLog = new ArrayList<String>();
    	assert saveObj("runnableTest", notif);
    	Optional<MockAppNotifier> notifO = LoadFile.loadObj("runnableTest", MockAppNotifier.class);
    	assert notifO.isPresent();
    	MockAppNotifier notifD = notifO.get();
    	notifD.run();
    }
    
   
    
    
    @Test
    public void runnableTest2() {
		MockAppNotifier notif = new MockAppNotifier();
		notif.log = new ArrayList<String>();
		assert notif.log.equals(new ArrayList<String>());
		System.out.println(notif.log);
		notif.run();
		ArrayList<String> checkLog = new ArrayList<String>();
		checkLog.add("win");
		assert notif.log.equals(checkLog);
    	assert saveObj("runnableTest", notif);
    	Optional<MockAppNotifier> notifO = LoadFile.loadObj("runnableTest", MockAppNotifier.class);
    	assert notifO.isPresent();
    	MockAppNotifier notifD = notifO.get();
    }
    
    @Test
    public void roomTest() {
    	
    	
    	
    }
    
    @Test
    public void saveNullField() {
    	
    	
    }
    
    @Test
    public void keyColourTest() {
    	Maze maze = Maze.createLevel1();
				
		Chap chap = new Chap(13, 9, new ArrayList<Item>());
		Key keyBlue =  new Key("Blue");
		Key keyRed = new Key("Red");
		chap.pickUpItem(keyRed);
		chap.pickUpItem(keyBlue);
		GameState gs = new GameState(maze, chap, 2, new HashMap<Key, String>(), 60, new MockAppNotifier(), new ArrayList<Actor>(), 0);
		maze.printMaze();
		System.out.println("");
		GameStateController gsc = new GameStateController(gs);
    	gsc.moveChap(Direction.Right);
    	gsc.moveChap(Direction.Right);
    	
    	assert chap.getRow() != 9 || chap.getCol() != 13;
    	maze.printMaze();
    }
    
    @Test
    public void keySerial() {
    	Key key = new Key("red");
    	assert saveObj("keyTest", key);
    	Optional<Key> keyO = LoadFile.loadObj("keyTest", Key.class);
    	assert keyO.isPresent();
    	Key keyD = keyO.get();
    	assert keyD.colour().equals("red");
    }
    
    @Test
    public void keyInventoryTest() {
    	Maze maze = Maze.createLevel1();
		Chap chap = new Chap(13, 9, new ArrayList<Item>());
		Key keyBlue =  new Key("Blue");
		Key keyRed = new Key("Red");
		chap.pickUpItem(keyRed);
		chap.pickUpItem(keyBlue);
    	assert chap.inventory().contains(keyRed);
    	
    	assert saveObj("inventoryTest", chap);
    	Optional<Chap> chapO = LoadFile.loadObj("inventoryTest", Chap.class);
    	assert chapO.isPresent();
    	Chap chapD = chapO.get();
    	((Key)chapD.inventory().get(0)).colour().equals("Blue");
    	
    	
    }
}
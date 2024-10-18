package test.nz.ac.
wgtn.swen225.lc.persistency;

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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/**
 * Tests for Persistency module
 * @author titheradam	300652933
 */


public class PersistencyTest {
	
	// Facory methods for common objects
	private Chap genChap() {
		return new Chap(2, 2, new ArrayList<Item>());
	}
	
	private Maze genMaze() {
		
		Builder build = new Builder();
		Room room = new Room();
		build.addRoom(new Coord(0, 0), new Room());
		build.addRoom(new Coord(0, 1), new Room());
		return build.build();	
	}
	
	private GameState genGameState() {
		return new GameState(genMaze(),
						genChap(),
						2,
						0, new HashMap<Key, String>(),
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
	
	// Reflection of private method loadObj for testing purposes
	@SuppressWarnings("unchecked")
	private <T> Optional<T> loadObj(File file, Class<T> classType) {
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
	
	// Reflection of private method mazeLocation
	private Coord mazeLocation(Coord roomLoc, Coord insideLoc) throws Throwable {
		try {
			Class<?>[] argClasses = new Class<?>[] {Coord.class, Coord.class};
			Method mazeLocation = Builder.class.getDeclaredMethod("mazeLocation", argClasses);
			mazeLocation.setAccessible(true);
			return (Coord) mazeLocation.invoke(null , roomLoc, insideLoc);
		} catch(InvocationTargetException e) {
			e.printStackTrace();
			
			throw (IllegalArgumentException) e.getCause();	
		}
	}
	
	
	//			*** Tests Below ***
	
	// Test if JUnit is working as expected
    @Test
    public void test() {
        Integer i = 0;
        assert i != null;
    }
	
	
    // Test default methods of Loader
    @Test
    public void loaderTest() {
    	//Loader loader = new Loader() {};
    	assertThrows(IOException.class, ()-> Loader.loadLevel("level1"));
    	//assertThrows(IOException.class, ()-> Loader.loadSave)
    }
    
    
    @Test
    public void saverTest() {
    	assertThrows (IOException.class, ()-> Saver.saveGame("level1", genGsc()));
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
    public void loadObjFail() {
    	assertEquals(loadObj(null, Canary.class), Optional.empty());
    }
    
    
    @Test
    public void loadObjFail_2() {
    	assertEquals(loadObj(Paths.level1, null), Optional.empty());
    }
    
    
    @Test
    public void loadObjFail_3() {
    	assertEquals(loadObj(Paths.level1, Canary.class), Optional.empty());
    }
    
    
    // Tests whether level1 is loadable from /levels
 	@Test
     public void level1Test() {
 		Level1.main(null);
     	Optional<GameStateController> gscOptionLevel = LoadFile.loadLevel(Paths.level1);
     	assert gscOptionLevel.isPresent();
     	}
 	
 	
 	@Test
 	public void level2Test() {
 		
 		level2.main(null);
     	Optional<GameStateController> gscOptionLevel = LoadFile.loadLevel(Paths.level2);
     	assert gscOptionLevel.isPresent();
 	}

 	
 	@Test
 	public void builderTest() {
 		Builder build = new Builder();
 		assertThrows(IllegalArgumentException.class, ()-> build.addRoom(new Coord(-1, 2), new Room()));
 		assertThrows(IllegalArgumentException.class, ()-> build.addRoom(new Coord(3, -1), new Room()));
 	}
 	
 	
 	@Test
 	public void builderBotRightTest() {
 		
 		try {
 			Builder build = new Builder();
 	 		build.addRoom(new Coord(1, 3) , new Room());
 	 		Class<?> builderClass = build.getClass(); 
			Field botRightField = builderClass.getDeclaredField("botRight");
			botRightField.setAccessible(true);
			Coord botRight = (Coord) botRightField.get(build);
			assert botRight.row() == 1 && botRight.col() == 3;
			// Should not shift botRight
			build.addRoom(new Coord(0, 2), new Room());
			assert botRight.row() == 1 && botRight.col() == 3;
			
		} catch (NoSuchFieldException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
 	}
 	
 	
 	@Test
 	public void builderBuildTest() throws Throwable {
 		Builder build = new Builder();
 		Maze maze = build.build();
 		maze.printMaze();
 		Room room = new Room();
 		Coord roomLoc = new Coord(2, 2);
 		build.addRoom(new Coord(2, 2), room);
 		Maze maze2 = build.build();
 		maze2.printMaze();
 		
 		try {
 	 		assert mazeLocation(roomLoc, room.centre).equals(new Coord(15,15));
 	 		assertThrows(IllegalArgumentException.class, ()-> mazeLocation(new Coord(2, -1), room.centre));
 	 		assertThrows(IllegalArgumentException.class, ()-> mazeLocation(new Coord(-1, 2), room.centre));
 		} catch(Exception e) {}
 	}
 	
 	
 	@Test
 	public void builderInnerTile() throws Throwable {
 		try {
 			Builder build = new Builder();
 	 		Room room = new Room();
 	 		room.setTile(room.centre, new WallTile());
 	 		Coord roomLoc = new Coord(2,2);
 	 		build.addRoom(roomLoc, room);
 	 		Maze maze = build.build();
 	 		Coord tileLoc = mazeLocation(roomLoc, room.centre);
 	 		assert maze.getTile(tileLoc.row(), tileLoc.col()) instanceof WallTile;
 		} catch(Exception e) {}
 		
 	}
 	
 	
 	@Test
 	public void saveAndQUit() {
 		Optional<GameStateController> level1 = LoadFile.loadLevel(Paths.level1);
 		assert level1.isPresent();
 		assert SaveFile.saveAndQuit(level1.get());
 	}
 	
 	@Test
 	public void saveAndQuitFail() {
 		assertThrows(IllegalArgumentException.class ,()-> SaveFile.saveAndQuit(null));
 	}
 	
 	
 	@Test 
    public void loadLevelStringTest() {
    	assert LoadFile.loadLevel("level1").isPresent();
    }
 	
 	
 	@Test
 	public void loadLevelStringFail() {
 		assertThrows(IllegalArgumentException.class, ()-> LoadFile.loadLevel(""));
 		assertThrows(IllegalArgumentException.class, ()-> LoadFile.loadLevel((String)null));
 	}
 	
 	
 	@Test
 	public void loadLevelFile() {
 		assert LoadFile.loadLevel(Paths.level1).isPresent();
 	}
 	
 	
 	@Test
 	public void loadLevelFileFail() {
 		assertThrows(IllegalArgumentException.class, ()-> LoadFile.loadLevel((File)null));
 	}
 	
 	
 	// Tests whether level1 is loadable from /saves
 	@Test
 	public void level1Save() {
 		Optional<GameStateController> gscOption = LoadFile.loadLevel(Paths.level1);
 		assert gscOption.isPresent();
     	GameStateController gsc = gscOption.get();	
 	}
    

    // Tests serialisation of Maze
    @Test public void mazeSave() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	assert saveObj("MazeTest", maze);
    	Maze maze2 = loadObj(new File(Paths.root,"MazeTest.json"), Maze.class).get();
    }
    
    
    // Tests serialisation / deserialisation of Chap
    @Test public void ChapLoad() {
    	Chap chap = new Chap(2, 2, new ArrayList<Item>());
    	assert chap.getCol() == 2;
    	assert chap.getRow() == 2;
    	assert saveObj("ChapText", chap);
    	File chapText = new File(Paths.root, "ChapText.json");
    	Optional<Chap> chapOption = loadObj(chapText, Chap.class);
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
		GameState gs = genGameState();
    	GameStateController gsc = new GameStateController(gs);

    	assert saveObj("inventoryTest", gsc);
    	Optional<GameStateController> gsOption = loadObj(new File(Paths.root,"inventoryTest.json"), GameStateController.class);
    	assert gsOption.isPresent();
    	GameStateController gsDeserial = gsOption.get();
    	
    	List<Item> inventory = gsDeserial.getChapInventory();
    	assert inventory != null;
    	
    	for(Item item : inventory) {  System.out.println(item.description());}
    }
    
    
    // Tests serialisation of GameState
    @Test public void gameStateLoad() {
		GameState gs = genGameState();
    	assert saveObj("GameStateTest", gs);
    	Optional<GameState> gsOption = loadObj(new File(Paths.root,"GameStateTest.json"), GameState.class);
    	assert gsOption.isPresent();
    	GameState gsDeserial = gsOption.get();
    	assert gsDeserial.totalTreasures() == gs.totalTreasures();
    }
    
    
    @Test 
    public void integrationTestInit() {
		GameState gs = genGameState();
		GameStateController gsc = new GameStateController(gs);
		
		Boolean saved = SaveFile.saveGame("integrationTest", gsc);
		assert saved;
		
		Optional<GameStateController> gscOption = LoadFile.loadLevel(new File(Paths.savePath, "integrationTest.json"));
		assert gscOption.isPresent();
		GameStateController gscDeserial = gscOption.get();
    }
    
    
    @Test
    public void loadStringTest() {
		GameState gs = genGameState();
		GameStateController gsc = new GameStateController(gs);
		Boolean saved = SaveFile.saveGame("IntegrationEx", gsc);
		assert saved;
    }

    
    @Test
    public void workingController() {
    	
		GameStateController gsc = genGsc();
		
		assert SaveFile.saveGame("controlTest", gsc);
		
		Optional<GameStateController> gscO = LoadFile.loadLevel(new File(Paths.savePath ,"controlTest.json"));
		assert gscO.isPresent();
		GameStateController gscD = gscO.get();
		
		gscD.moveChap(Direction.Right);
		gscD.moveChap(Direction.Left);
		gscD.getChap().moveTo(3, 3, gscD.getMaze());
    }
   
    
    @Test
    public void runnableTest() {
		MockAppNotifier notif = new MockAppNotifier();
		notif.run();
		ArrayList<String> checkLog = new ArrayList<String>();
    	assert saveObj("runnableTest", notif);
    	Optional<MockAppNotifier> notifO = loadObj(new File(Paths.root,"runnableTest.json"), MockAppNotifier.class);
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
    	Optional<MockAppNotifier> notifO = loadObj(new File(Paths.root,"runnableTest.json"), MockAppNotifier.class);
    	assert notifO.isPresent();
    	MockAppNotifier notifD = notifO.get();
    }
    
    
    @Test
    public void keySerialTest() {
    	Key key = new Key("red");
    	assert saveObj("keyTest", key);
    	Optional<Key> keyO = loadObj(new File(Paths.root,"keyTest.json"), Key.class);
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
    	Optional<Chap> chapO = loadObj(new File(Paths.root,"inventoryTest.json"), Chap.class);
    	assert chapO.isPresent();
    	Chap chapD = chapO.get();
    	((Key)chapD.inventory().get(0)).colour().equals("Blue");
    }
    
    
    @Test
    public void coordTest() {
    	Coord coord = new Coord(1, 2);
    	assert coord.row() == 1 && coord.col() == 2;
    	assertThrows(IllegalArgumentException.class, ()-> new Coord(-2, 1));
    	assertThrows(IllegalArgumentException.class, ()-> new Coord(2, -4));
    	coord = new Coord(-1, -1);
    	assert coord.row() == -1 && coord.col() == -1;
    }
}
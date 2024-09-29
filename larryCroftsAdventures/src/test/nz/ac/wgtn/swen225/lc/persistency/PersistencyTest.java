package test.nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.persistency.*;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

// Domain imports


public class PersistencyTest {

    // Test nested object mockGameState into JSON
//    @Test
//    public void gameStateToJson(){
//        GameController gm = new MockController(2, 2, 2, 1, 1);
//        assert SaveFile.saveGame("Test1", gm);
//    }
	
	@Test
    public void level1Level() {
    	Optional<GameStateController> gscOptionLevel = LoadFile.loadLevel("level1");
    	assert gscOptionLevel.isPresent();
    	GameStateController gscLevel = gscOptionLevel.get();
    }
	
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
     * Test for ObjectMaper
     */
    @Test public void objTest() {
    	Seeds s = new Seeds(1000);

    	Canary c = new Canary("ObjBird", 1000, s);
    	
    	assert SaveFile.saveObj("ObjectTest", c);
    	
    	
    }
    
   
    @Test public void mazeSave() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	assert SaveFile.saveObj("MazeTest", maze);
    	Maze maze2 = LoadFile.loadObj("MazeTest", Maze.class).get();
    }
    
    // Tests serialisation / deserialisation of Chap
    @Test public void ChapLoad() {
    	Chap chap = new Chap(2, 2);
    	assert chap.getPosition().equals("Chap is at row: 2, column: 2");
    	assert SaveFile.saveObj("ChapText", chap);
    	Optional<Chap> chapOption = LoadFile.loadObj("ChapText", Chap.class);
    	assert chapOption.isPresent();
    	Chap chapDeserial = chapOption.get();
    	assert chap.getPosition().equals(chapDeserial.getPosition());
    	
    	
    	
    }
    
    @Test public void chapInventory() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	Chap chap = new Chap(2, 2);
    	
    	Item redKey = new Key("red");
    	chap.pickUpItem(redKey);
    	GameState gs = new GameState(maze, chap, 2);
    	GameStateController gsc = new GameStateController(maze, chap, gs);

    	
    	
    	assert SaveFile.saveObj("inventoryTest", gsc);
    	Optional<GameStateController> gsOption = LoadFile.loadObj("inventoryTest", GameStateController.class);
    	assert gsOption.isPresent();
    	GameStateController gsDeserial = gsOption.get();
    	
    	List<Item> inventory = gsDeserial.getChapInventory();
    	assert inventory != null;
    	
    	for(Item item : inventory) {  System.out.println(item.description());}
    }
    
    
    @Test public void gameStateLoad() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	Chap chap = new Chap(2, 2);
    	GameState gs = new GameState(maze, chap, 2);
    	assert SaveFile.saveObj("GameStateTest", gs);
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
    @Test public void gameSaveLoadTest() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	Chap chap = new Chap(2, 2);
    	GameState gs = new GameState(maze, chap, 2);
    	GameStateController gsc = new GameStateController(maze, chap, gs);
    	
    	assert SaveFile.saveObj("GameSaveLoadTest", gsc);
    	
    	Optional<GameStateController> gscOption = LoadFile.loadObj("GameSaveLoadTest", GameStateController.class);
    	assert gscOption.isPresent();
    	GameStateController gscDeserial = gscOption.get();
    	
    	assert gscDeserial.getChapPosition().equals(chap.getPosition());
    	
    	// checking if same type of tile as hashcode prevents .equals directly
    	assert gscDeserial.getTileAtChapPosition().getClass().equals(maze.getTile(2, 2).getClass());    	
    }
    
    @Test 
    public void integrationTestInit() {
		
		Maze maze = Maze.createCustomMaze();
		Chap chap = new Chap(2, 2);
		GameState gs = new GameState(maze, chap, 2);
		
		GameStateController gsc = new GameStateController(maze, chap, gs);
		
		Boolean saved = SaveFile.saveGame("level1", gsc);
		assert saved;
		
		Optional<GameStateController> gscOption = LoadFile.loadSave("level1");
		assert gscOption.isPresent();
		GameStateController gscDeserial = gscOption.get();
    }
    
    
    @Test
    public void newTest() {
    	Maze maze = Maze.createCustomMaze();
    	Chap chap = new Chap(2,2);
    	GameState gs = new GameState(maze, chap, 2);
    	GameStateController gsc = new GameStateController(maze, chap, gs);
		
		Boolean saved = SaveFile.saveGame("IntegrationEx", gsc);
		assert saved;   
		
    }
    
    
    
    
    
}
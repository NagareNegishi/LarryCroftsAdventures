package test.nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameStateControllerInterface;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.GameState;

import nz.ac.wgtn.swen225.lc.persistency.*;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Optional;

// Domain imports


public class PersistencyTest {

    // Test nested object mockGameState into JSON
//    @Test
//    public void gameStateToJson(){
//        GameController gm = new MockController(2, 2, 2, 1, 1);
//        assert SaveFile.saveGame("Test1", gm);
//    }

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
    
    // TODO
    @Test public void gameStateTest() {
    	Maze maze = Maze.createBasicMaze(5, 5);
		Chap chap = new Chap(2,2);
		GameStateInterface test = new GameState(maze,chap,1);
		//assert SaveFile.save
    }
    
    /**
     * Test serialisation of GameState
     */
    @Test public void gameControlTest() {
    	GameStateControllerInterface gs = new GameStateController(5, 5, 2, 2, 1);
    	assert SaveFile.saveGame("GameSaveTest", gs);
    }
    
    
    
    @Test public void mazeSave() {
    	Maze maze = Maze.createBasicMaze(5, 5);
    	assert SaveFile.saveObj("MazeTest", maze);
    	Maze maze2 = LoadFile.loadMaze("MazeTest").get();
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
    
    
     //TODO
    @Test public void GameLoadTest() {
    	Optional<GameStateControllerInterface> gsOp = LoadFile.loadSave("GameSaveTest");
    	GameStateControllerInterface gs = gsOp.get();
    }
    
    
    
    
}
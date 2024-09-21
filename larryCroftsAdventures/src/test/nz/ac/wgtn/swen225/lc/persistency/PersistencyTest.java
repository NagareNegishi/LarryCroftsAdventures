package test.nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameStateControllerInterface;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;

import nz.ac.wgtn.swen225.lc.persistency.*;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    @Test public void GameStateTest() {
    	Maze maze = Maze.createBasicMaze(5, 5);
		Chap chap = new Chap(2,2);
		GameStateInterface test = new GameState(maze,chap,1);
		//assert SaveFile.save
    }
    
    /**
     * Test serialisation of GameState
     */
    @Test public void GameControlTest() {
    	GameStateControllerInterface gs = new GameStateController(5, 5, 2, 2, 1);
    	assert SaveFile.saveGame("GameSaveTest", gs);
    }
    
    @Test public void GameLoad() {
    	Optional<GameStateControllerInterface> gsOp = LoadFile.loadSave("GameSaveTest");
    	GameStateControllerInterface gs = gsOp.get();

    }
    
    
}
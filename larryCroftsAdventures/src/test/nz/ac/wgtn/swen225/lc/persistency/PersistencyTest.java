package test.nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameStateControllerInterface;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;

import nz.ac.wgtn.swen225.lc.persistency.*;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Domain imports


public class PersistencyTest {

    // Test nested object mockGameState into JSON
//    @Test
//    public void gameStateToJson(){
//        GameController gm = new MockController(2, 2, 2, 1, 1);
//        assert SaveFile.saveGame("Test1", gm);
//    }

    @Test
    public void test() {
        Integer i = 0;
        assert i != null;
    }
    
    @Test
    public void testSave() {
    	String info = "Hello did this work?";
    	assert SaveFile.saveString("testSave", info);
    }
    
    
    /**
     * Check how Canary serialises into JSON
     */
    @Test public void canaryTest() {
    	Canary c = new Canary("Lary", 500);
    	assert SaveFile.saveCanary("BestLarry", c);
    }
    
    @Test public void objTest() {
    	Canary c = new Canary("ObjBird", 1000);
    	assert SaveFile.saveObj("ObjectTest", c);
    	
    }
    
    @Test public void GameStateTest() {
    	Maze maze = Maze.createBasicMaze(5, 5);
		Chap chap = new Chap(2,2);
		GameStateInterface test = new GameState(maze,chap,1);
		//assert SaveFile.save
    	
    }
    
    @Test public void GameControlTest() {
    	GameStateControllerInterface gs = new GameStateController(5, 5, 2, 2, 1);
    	assert SaveFile.saveGame("GameSaveTest", gs);
    }
    
    
}
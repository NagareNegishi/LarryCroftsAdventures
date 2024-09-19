package test.nz.ac.wgtn.swen225.lc.persistency;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import nz.ac.wgtn.swen225.lc.persistency.*;

// Domain imports
//import nz.ac.wgtn.swen225.lc.domain.GameController;



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
    
    
}
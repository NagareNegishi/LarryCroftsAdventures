package nz.ac.wgtn.swen225.lc.persistency;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;

import test.nz.ac.wgtn.swen225.lc.persistency.*;
import nz.ac.wgtn.swen225.lc.domain.*;


import java.io.File;
import java.io.IOException;


public class SaveFile implements Saver{
	/**
	 * Saves GameStateController as .json file in /saves directory
	 * @param fileName : String, filename of new .json 
	 * @param gameControl : GameStateController, model of game state
	 * @return boolean, True if successfully saved
	 */
    public static boolean saveGame(String fileName, GameStateController gameControl){
        if(fileName == null || fileName.isEmpty() || gameControl == null) {
        	throw new IllegalArgumentException("Null or empty filename");
        }
    	//System.out.println(fileName.substring(fileName.length()-5, fileName.length()));

        // removes .json from filename if included
        if(fileName.substring(fileName.length()-5, fileName.length()).equals(".json")) {
        	fileName = fileName.substring(0, fileName.length()-5);
        	//System.out.println(fileName);
        }
        gameControl = removeAppNotifier(gameControl);
        
        return saveObj(Paths.savePath + fileName, gameControl);
        
//        ObjectMapper mapper = new ObjectMapper();
//        
//        // Map obj to JSON file
//        try{
//            mapper.writeValue( new File(Paths.savePath + fileName + ".json"), gameControl );
//            return true;
//        } catch(IOException e){
//            e.printStackTrace();
//        }
        // Failed to send
        //return false;
    }
    
    /**
     * Used to set the saveAndQuit file
     * @param gsc
     * @return
     */
    public static boolean saveAndQuit(GameStateController gsc) {
    	if(gsc == null) {
    		throw new IllegalArgumentException("Null GameStateController");
    	}
    	assert gsc != null;
    	assert gsc instanceof GameStateController;
    	return saveObj(Paths.saveAndQuit + "", gsc);
    }
    
    
    
    /**
     * Generic method to save JSON of any object
     * @param <T>,  Generic object to serialise
     * @param fileName
     * @param obj
     * @return boolean: true if successful
     */
    private static <T> boolean saveObj(String fileName, T obj) {
    	assert obj != null;
    	assert !fileName.isEmpty();
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try{
            mapper.writeValue( new FileOutputStream(fileName + ".json"), obj);
            return true;
        } catch(IOException e){
            e.printStackTrace();
        }
        // Failed to send
        return false;
    }
    
    /**
     * Removes non-serialisable appNotifier before serialisation
     * @return
     */
    private static GameStateController removeAppNotifier(GameStateController gsc) {
    	GameState gs = gsc.getGameState();
    	gs.setAppNotifier(new MockAppNotifier());
    	return gsc;
    }
}


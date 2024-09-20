package nz.ac.wgtn.swen225.lc.persistency;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;

import test.nz.ac.wgtn.swen225.lc.persistency.*;
import nz.ac.wgtn.swen225.lc.domain.*;


import java.io.File;
import java.io.IOException;

// TODO
public class SaveFile implements Saver{

    public static boolean saveGame(String fileName, GameStateControllerInterface gameControl){

        assert gameControl != null;
        assert fileName != null && !fileName.isEmpty();

        ObjectMapper mapper = new ObjectMapper();

        final String pathPrefix = "saves/";
        
        // Map obj to JSON file
        try{
            mapper.writeValue( new File(pathPrefix + fileName + ".json"), gameControl );
            return true;
        } catch(IOException e){
            e.printStackTrace();
        }
        // Failed to send
        return false;
    }
    
    
    
    /**
     * Generic method to save JSON of any object
     * @param <T>,  Generic object to serialise
     * @param fileName
     * @param obj
     * @return
     */
    public static <T> boolean saveObj(String fileName, T obj) {
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
    
    
    
    
}


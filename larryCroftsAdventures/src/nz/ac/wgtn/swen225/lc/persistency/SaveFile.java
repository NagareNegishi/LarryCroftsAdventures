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

        //final String  pathPrefix = "../../levels/";
        final String pathPrefix = "";
        
        // Map obj to JSON file
        try{
            mapper.writeValue( new File(pathPrefix + fileName), gameControl );
            return true;
        } catch(IOException e){
            e.printStackTrace();
        }
        // Failed to send
        return false;
    }
    
    
    /**
     * Test for objectmapper
     * @param fileName
     * @param info
     * @return
     */
    public static boolean saveString(String fileName, String info) {
    	
    	assert info != null && !info.isEmpty();
    	assert fileName != null && !fileName.isEmpty();
    	
    	ObjectMapper mapper = new ObjectMapper();
    	try{
            mapper.writeValue( new FileOutputStream(fileName), info);
            return true;
        } catch(IOException e){
            e.printStackTrace();
        }
        // Failed to send
        return false;
    	
    }
    
    
    
    
    
    /**
     * Generic save for all objects. DOES NOT WORK
     * @param <T>
     * @param fileName
     * @param obj
     * @return
     */
    public static boolean saveCanary(String fileName, Canary obj) {
    	
    	assert obj != null;
    	
    	ObjectMapper mapper = new ObjectMapper();
    	try{
            mapper.writeValue( new FileOutputStream(fileName), obj);
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
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try{
            mapper.writeValue( new FileOutputStream(fileName), obj);
            return true;
        } catch(IOException e){
            e.printStackTrace();
        }
        // Failed to send
        return false;
    }
    
    
    
    
}


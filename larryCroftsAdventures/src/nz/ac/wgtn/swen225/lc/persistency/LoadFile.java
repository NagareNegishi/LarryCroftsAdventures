package nz.ac.wgtn.swen225.lc.persistency;


import nz.ac.wgtn.swen225.lc.domain.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Optional;


import java.io.IOException;

/**
 * De-serialises .json files into levels
 * @author titheradam	300652933
 */
public class LoadFile implements Loader {
	
	/**
	 * Persistency entry point
	 * Used to de-serialise valid .json file into a GameStateController
	 * @param levelName , String : filename to be used for .json file
	 * @return Optional of GameStateController that may be empty if non valid file
	 */
	public static Optional<GameStateController> loadLevel(String levelName) {
		if(levelName == null || levelName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		assert levelName != null;
		assert !levelName.isEmpty();
		//final String pathPrefix = "levels/";
		//String path = pathPrefix + levelName;
		return loadObj(new File(Paths.levelsDir, levelName + ".json") , GameStateController.class);
	}
	
    
    /**
     * Overload for file
     * @param file : .json file containing serialised GameStateControllerInterface
     * @return de-serialsed GameStateControllerInterface
     */
    public static Optional<GameStateController> loadLevel(File file) {
		if(file == null) {
			throw new IllegalArgumentException("Null file");
		}
    	assert file != null;
    	return loadObj(file, GameStateController.class);		
	}
    
    
    /**
     * Overloaded to accept files directly
     * @param <T>
     * @param file
     * @param classType
     * @return de-serialised object of type T
     */
    private static <T> Optional<T> loadObj(File file, Class<T> classType){
    	try {
    		ObjectMapper mapper = new ObjectMapper();
        	T loadedObject = mapper.readValue(file, classType);
        	return Optional.of(loadedObject);
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.out.println("IoException in loadObj");
    	}
    	return Optional.empty();
    }
}

package nz.ac.wgtn.swen225.lc.persistency;


import nz.ac.wgtn.swen225.lc.domain.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Optional;


import java.io.IOException;


public class LoadFile implements Loader {
	
	public static Optional<GameStateController> loadLevel(String levelName) {
		assert levelName != null;
		assert !levelName.isEmpty();
		final String pathPrefix = "levels/";
		String path = pathPrefix + levelName;
		return loadObj(path, GameStateController.class);
	}

	public static Optional<GameStateController> loadSave(String saveName){
		assert saveName != null;
		assert !saveName.isEmpty();
		final String pathPrefix = "saves/";
		String path = pathPrefix + saveName;
		return loadObj(path, GameStateController.class);
	}
	
	
	/**
     * Generic method to load a file using ObjectMapper
     * @param path, String : file path
     * @param valueType : the class type to deserialize to
     * @return an Optional containing the deserialized object
     */
    public static <T> Optional<T> loadObj(String path, Class<T> classType) {
        try {
            File file = new File(path + ".json");
            ObjectMapper mapper = new ObjectMapper();
            T loadedObject = mapper.readValue(file, classType);
            return Optional.of(loadedObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    /**
     * Overload for file
     * @param file : .json file containing serialised GameStateControllerInterface
     * @return de-serialsed GameStateControllerInterface
     */
    public static Optional<GameStateControllerInterface> loadLevel(File file) {
		assert file != null;
		return loadObj(file, GameStateControllerInterface.class);
	}
    
    /**
     * Overload for file
     * @param file : .json file containing serialised GameStateControllerInterface
     * @return de-serialsed GameStateControllerInterface
     */
    public static Optional<GameStateController> loadSave(File file){
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
    public static <T> Optional<T> loadObj(File file, Class<T> classType){
    	try {
    		ObjectMapper mapper = new ObjectMapper();
        	T loadedObject = mapper.readValue(file, classType);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return Optional.empty();
    }
}





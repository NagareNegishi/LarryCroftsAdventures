package nz.ac.wgtn.swen225.lc.persistency;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.domain.GameStateControllerInterface;


public class LoadFile implements Loader {

    ///////////////////////////////
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    ///////////////////////

	
	public static Optional<GameStateController> loadLevel(String levelName) {
		assert levelName != null;
		assert !levelName.isEmpty();
///////////////////////////////////////nagi delete me
        //final String pathPrefix = "levels" + File.separator;
        Path path = Paths.get(PROJECT_ROOT, "levels", levelName + ".json");
        System.out.println("Loading level from: " + path);
/////////////////////////////////////
		//final String pathPrefix = "levels/";
		//String path = pathPrefix + levelName;

        return loadObj(path.toString(), GameStateController.class);
		//return loadObj(path, GameStateController.class);
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
            //File file = new File(path + ".json");
            //////////////////////////////////
            File file = new File(path);
            System.out.println("Attempting to load file: " + file.getAbsolutePath());
            if (!file.exists()) {
                System.out.println("File does not exist: " + file.getAbsolutePath());
                return Optional.empty();
            }
    
            if (!file.canRead()) {
                System.out.println("Cannot read file: " + file.getAbsolutePath());
                return Optional.empty();
            }
            /////////////////////////////////
            ObjectMapper mapper = new ObjectMapper();
            T loadedObject = mapper.readValue(file, classType);

            /////////////////////////////////
            System.out.println("Successfully loaded file: " + file.getAbsolutePath());
            System.out.println("Loaded object contents: " + loadedObject.toString());
            /////////////////////////////////
            return Optional.of(loadedObject);
        } catch (IOException e) {

            ////////////////////////////////
            System.out.println("Error loading file: " + path);
            System.out.println("Error message: " + e.getMessage());
            ////////////////////////////////
            e.printStackTrace();
        }
//////////////////////////////////////////
        System.out.println("Failed to load file: " + path);
/////////////////////
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





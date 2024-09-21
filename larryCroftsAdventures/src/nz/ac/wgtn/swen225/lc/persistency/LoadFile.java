package nz.ac.wgtn.swen225.lc.persistency;


import nz.ac.wgtn.swen225.lc.domain.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Optional;


import java.io.IOException;


public class LoadFile implements Loader {
	
	public static Optional<GameStateControllerInterface> loadLevel(String levelName) {
		assert levelName != null;
		assert !levelName.isEmpty();
		final String pathPrefix = "levels/";
		String path = pathPrefix + levelName + ".json";
		return load(path);
	}

	public static Optional<GameStateControllerInterface> loadSave(String saveName){
		assert saveName != null;
		assert !saveName.isEmpty();
		final String pathPrefix = "saves/";
		String path = pathPrefix + saveName + ".json";
		return load(path);
	}

	/**
	 * Loads file using ObjectMapper
	 * @param path, String : file path
	 * @return representation of game state
	 */
	private static Optional<GameStateControllerInterface> load(String path){
		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File(path);
			GameStateController loadedGame = mapper.readValue(file, GameStateController.class);
			return Optional.of(loadedGame);
		} catch(IOException e) { e.printStackTrace(); }
		return Optional.empty();
	}
	
	
	/**
	 * Used to test Maze de-serialisation
	 * @param path
	 * @return
	 */
	public static Optional<Maze> loadMaze(String path){
		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new File(path + ".json");
			Maze loadedMaze = mapper.readValue(file, Maze.class);
			return Optional.of(loadedMaze);
		}catch (IOException e) { e.printStackTrace();}
		return Optional.empty();
	}
	
	/**
     * Generic method to load a file using ObjectMapper
     * @param path, String : file path
     * @param valueType : the class type to deserialize to
     * @return an Optional containing the deserialized object
     */
    public static <T> Optional<T> loadObj(String path, Class<T> valueType) {
        try {
            File file = new File(path + ".json");
            ObjectMapper mapper = new ObjectMapper();
            T loadedObject = mapper.readValue(file, valueType);
            return Optional.of(loadedObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
	
	
}





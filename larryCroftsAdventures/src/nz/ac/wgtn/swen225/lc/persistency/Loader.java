package nz.ac.wgtn.swen225.lc.persistency;

import java.util.Optional;


import java.io.IOException;


import nz.ac.wgtn.swen225.lc.domain.*;

public interface Loader {
	
	
	
	/**
	 * Loads level from JSON
	 * @param String level : name of level file excluding .json
	 * @return Optional<GameStateControllerInterface> 
	 * @throws IOException
	 */
	public static Optional<GameStateController> loadLevel(String level) throws IOException {
		throw new IOException("Default method");
	}
	
	
	/**
	 * 
	 * Used to load game from saves folder
	 * @param fileName
	 * @return
	 */
	public static Optional<GameStateController> loadSave(String fileName) throws IOException {
		throw new IOException("Default method");
	}
}

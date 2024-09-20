package nz.ac.wgtn.swen225.lc.persistency;

import java.io.IOException;

import nz.ac.wgtn.swen225.lc.domain.*;

public interface Loader {
	
	
	
	
	public static GameStateControllerInterface loadLevel(String level) throws IOException {
		throw new IOException("Default method");
	}
	
	
	/**
	 * Used to load game from saves folder
	 * @param fileName
	 * @return
	 */
	public static boolean loadSave(String fileName) throws IOException {
		throw new IOException("Default method");
	}
}

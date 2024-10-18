package nz.ac.wgtn.swen225.lc.persistency;

import java.util.Optional;


import java.io.IOException;


import nz.ac.wgtn.swen225.lc.domain.*;

/**
 * Enrty point for Persistency module 
 * De-serialises .json files into levels 
 * @author titheradam	300652933
 */
public interface Loader {
	
	/**
	 * Loads level from JSON
	 * @param String level : name of level file excluding .json
	 * @return Optional of Obj that holds full context / state of game
	 * @throws IOException
	 */
	public static Optional<GameStateController> loadLevel(String level) throws IOException {
		throw new IOException("Default method");
	}
}

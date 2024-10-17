package nz.ac.wgtn.swen225.lc.persistency;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;


import java.io.File;
import java.io.IOException;

import nz.ac.wgtn.swen225.lc.domain.*;



/**
 * Entry point to persistency module
 * Used to serialise level state
 * @author titheradam	300652933
 */
public interface Saver {

    /**
     * Saves game state as JSON file
     * @param fileName: String, name for new JSON file
     * @param gameState: GameState, instance describing all game objects
     * @return boolean, success or failure of save
     * @throws IOException 
     */
    public static boolean saveGame(String fileName, GameStateController gameStateControl) throws IOException{
        throw new IOException();
    };
}
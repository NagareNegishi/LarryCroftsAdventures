package nz.ac.wgtn.swen225.lc.persistency;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;

import nz.ac.wgtn.swen225.lc.domain.GameController;

import java.io.File;
import java.io.IOException;


/**
 * Used to save game state
 * Entry point to persistency module
 */
public interface Saver {

    /**
     * Saves game state as JSON file
     * @param fileName: String, name for new JSON file
     * @param gameState: GameState, instance describing all game objects
     * @return boolean, success or failure of save
     */
    public static boolean saveGame(String fileName, GameController gameControl){
        return false;
    };
}


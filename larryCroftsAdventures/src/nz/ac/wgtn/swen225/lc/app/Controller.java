package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;


/**
 * Controller class that extends Keys class
 * 
 * will be public after the merge, but need to be private before submission
 * both class and constructor
 * 
 */
class Controller extends Keys{
/**
   * Constructor for the Controller class
   * @param c MockCamera object expecting "Chap" object
   */
    Controller(Chap chap, Maze maze, Map<String, Runnable> actionBindings){
        setAction(KeyEvent.VK_UP, 0,() -> chap.move(Chap.Direction.Up, maze), () -> {});
        setAction(KeyEvent.VK_DOWN, 0,() -> chap.move(Chap.Direction.Down, maze), () -> {});
        setAction(KeyEvent.VK_LEFT, 0,() -> chap.move(Chap.Direction.Left, maze), () -> {});
        setAction(KeyEvent.VK_RIGHT, 0,() -> chap.move(Chap.Direction.Right, maze), () -> {});

        // Ctrl key combinations
        // InputEvent since getKeyStroke() is expecting InputEvent
        setAction(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK, actionBindings.get("exitWithoutSaving"), () -> {});
        setAction(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, actionBindings.get("exitAndSave"), () -> {});
        setAction(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, actionBindings.get("resumeSavedGame"), () -> {});
        setAction(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK, actionBindings.get("startNewGame1"), () -> {});
        setAction(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK, actionBindings.get("startNewGame2"), () -> {});

        // Other keys
        setAction(KeyEvent.VK_SPACE, 0, actionBindings.get("pause"), () -> {});
        setAction(KeyEvent.VK_ESCAPE, 0, actionBindings.get("unpause"), () -> {});
    }
}
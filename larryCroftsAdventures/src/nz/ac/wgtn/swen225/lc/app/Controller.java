package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;


/**
 * Controller class that extends Keys class
 */
<<<<<<< HEAD
public class Controller extends Keys{
    private final Chap chap;
    private final Maze maze;

=======
class Controller extends Keys{
>>>>>>> origin/main
/**
   * Constructor for the Controller class
   * @param c MockCamera object expecting "Chap" object
   */
<<<<<<< HEAD
    public Controller(Chap chap, Maze maze, Map<String, Runnable> actionBindings){
        this.chap = chap;
        this.maze = maze;
        setAction(KeyEvent.VK_UP, 0, () -> {chap.move(Chap.Direction.Up,maze); System.out.println("up");}, () -> {});
        setAction(KeyEvent.VK_DOWN, 0, () -> chap.move(Chap.Direction.Down,maze), () -> {});
        setAction(KeyEvent.VK_LEFT, 0, () -> chap.move(Chap.Direction.Left,maze), () -> {});
        setAction(KeyEvent.VK_RIGHT, 0, () -> chap.move(Chap.Direction.Right,maze), () -> {});
=======
    Controller(Chap chap, Maze maze, Map<String, Runnable> actionBindings){
        setAction(KeyEvent.VK_UP, 0,() -> chap.move(Chap.Direction.Up, maze), () -> {});
        setAction(KeyEvent.VK_DOWN, 0,() -> chap.move(Chap.Direction.Down, maze), () -> {});
        setAction(KeyEvent.VK_LEFT, 0,() -> chap.move(Chap.Direction.Left, maze), () -> {});
        setAction(KeyEvent.VK_RIGHT, 0,() -> chap.move(Chap.Direction.Right, maze), () -> {});
>>>>>>> origin/main

        // Ctrl key combinations
        // InputEvent since getKeyStroke() is expecting InputEvent
        
        //TEMPORARY ANTHONY EDIT:
        /*setAction(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK, actionBindings.get("exitWithoutSaving"), () -> {});
        setAction(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, actionBindings.get("exitAndSave"), () -> {});
        setAction(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, actionBindings.get("resumeSavedGame"), () -> {});
        setAction(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK, actionBindings.get("startNewGame1"), () -> {});
        setAction(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK, actionBindings.get("startNewGame2"), () -> {});

        // Other keys
        setAction(KeyEvent.VK_SPACE, 0, actionBindings.get("pause"), () -> {});
        setAction(KeyEvent.VK_ESCAPE, 0, actionBindings.get("unpause"), () -> {});*/
    }
}
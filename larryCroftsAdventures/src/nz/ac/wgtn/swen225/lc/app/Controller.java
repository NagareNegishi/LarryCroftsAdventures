package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;


/**
 * Controller class that extends Keys class
 */
class Controller extends Keys{
    private final Chap chap;
    private final Maze maze;

/**
   * Constructor for the Controller class
   * @param c MockCamera object expecting "Chap" object
   */
    Controller(Chap chap, Maze maze, Runnable pause,Runnable unpause){
        this.chap = chap;
        this.maze = maze;
        setAction(KeyEvent.VK_UP, () -> {chap.moveUp(maze); System.out.println("up");}, () -> {});
        setAction(KeyEvent.VK_DOWN, () -> chap.moveDown(maze), () -> {});
        setAction(KeyEvent.VK_LEFT, () -> chap.moveLeft(maze), () -> {});
        setAction(KeyEvent.VK_RIGHT, () -> chap.moveRight(maze), () -> {});

/**
 * it should work after next domain merge
 *      setAction(KeyEvent.VK_UP, () -> chap.move(Chap.Direction.Up, maze), () -> {});
        setAction(KeyEvent.VK_DOWN, () -> chap.move(Chap.Direction.Down, maze), () -> {});
        setAction(KeyEvent.VK_LEFT, () -> chap.move(Chap.Direction.Left, maze), () -> {});
        setAction(KeyEvent.VK_RIGHT, () -> chap.move(Chap.Direction.Right, maze), () -> {});
 */



        /**
        // Ctrl key combinations
        setAction(KeyEvent.VK_X | KeyEvent.CTRL_DOWN_MASK, this::exitGameWithoutSaving, () -> {});
        setAction(KeyEvent.VK_S | KeyEvent.CTRL_DOWN_MASK, this::exitGameAndSave, () -> {});
        setAction(KeyEvent.VK_R | KeyEvent.CTRL_DOWN_MASK, this::resumeSavedGame, () -> {});
        setAction(KeyEvent.VK_1 | KeyEvent.CTRL_DOWN_MASK, () -> startNewGame(1), () -> {});
        setAction(KeyEvent.VK_2 | KeyEvent.CTRL_DOWN_MASK, () -> startNewGame(2), () -> {});
         */

        // Other keys
        setAction(KeyEvent.VK_SPACE, pause, () -> {});
        setAction(KeyEvent.VK_ESCAPE, unpause, () -> {});
}


/**
 * i want to set controller in constructor
 * but many method are defined in App.
 * i dont want to pass App to Controller, controller shouldnt access App so no static too
 * maybe just make collection of methods in App and pass that to Controller?
 * its not elegant though.....
 */


}


    //original plan for setting keys for directions
    /**setAction(KeyEvent.VK_UP, c.set(Direction::up), c.set(Direction::none));
    setAction(KeyEvent.VK_DOWN, c.set(Direction::down), c.set(Direction::none));
    setAction(KeyEvent.VK_LEFT, c.set(Direction::left), c.set(Direction::none));
    setAction(KeyEvent.VK_RIGHT, c.set(Direction::right), c.set(Direction::none));*/
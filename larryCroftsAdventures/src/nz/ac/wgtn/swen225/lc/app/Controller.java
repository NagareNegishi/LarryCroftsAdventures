package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;


/**
 * Controller class that extends Keys class
 * 
 * will be public after the merge, but need to be private before submission
 * both class and constructor
 * 
 */
class Controller extends Keys{

    private int time = 0;//testing purposes
    private Recorder recorder;
    private Chap chap;
    private Maze maze;


    /**
    * Constructor for the Controller class
    */
	Controller(Chap c, Maze m, Map<String, Runnable> actionBindings){
        chap = c;
        maze = m;
        setAction(KeyEvent.VK_UP, 0,() -> {
            chap.move(Chap.Direction.Up, maze);
            recorder.ping(Chap.Direction.Up, time);
            System.out.println("UP");
            }, () -> {});
        setAction(KeyEvent.VK_DOWN, 0,() -> {
            chap.move(Chap.Direction.Down, maze);
            recorder.ping(Chap.Direction.Down, time);
            System.out.println("DOWN");
            }, () -> {});
        setAction(KeyEvent.VK_LEFT, 0,() -> {
            chap.move(Chap.Direction.Left, maze);
            recorder.ping(Chap.Direction.Left, time);
            System.out.println("LEFT");
            }, () -> {});
        setAction(KeyEvent.VK_RIGHT, 0,() -> {
            chap.move(Chap.Direction.Right, maze);
            recorder.ping(Chap.Direction.Right, time);
            System.out.println("RIGHT");
            }, () -> {});

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

    /**
     * I will keep it
     * likely i will rollback to this version later
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
        */
    }

    public void setRecorder(Recorder r){
        recorder = r;
    }

    public void setChap(Chap c){
        chap = c;
    }

    public void setMaze(Maze m){
        maze = m;
    }

    public void updatetime(int t){
        time = t;
    }

}
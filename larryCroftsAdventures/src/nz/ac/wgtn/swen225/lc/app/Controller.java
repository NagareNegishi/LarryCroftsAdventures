package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.GameStateController;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;


/**
 * Controller class that extends Keys class.
 * This class is responsible for handling key events,
 * and performing actions based on the key events.
 * Gamestatecontroller is passed to this class to perform actions on the game state.
 * Recorder, so Recorder module can track the change of chap.
 */
class Controller extends Keys{

    private int time = 0;//testing purposes
    private Recorder recorder;
    private GameStateController update;

    /**
     * Constructor for Controller class
     * @param StateController contains the methods defined in GameStateController class
     * @param actionBindings contains the methods defined in App class
     */
    Controller(GameStateController StateController , Map<String, Runnable> actionBindings){
        update = StateController;
        setAction(KeyEvent.VK_UP, 0,() -> {
            update.moveChap(Chap.Direction.Up);
            recorder.ping(Chap.Direction.Up, time);
            System.out.println("UP");
            });
        setAction(KeyEvent.VK_DOWN, 0,() -> {
            update.moveChap(Chap.Direction.Down);
            recorder.ping(Chap.Direction.Down, time);
            System.out.println("DOWN");
            });
        setAction(KeyEvent.VK_LEFT, 0,() -> {
            update.moveChap(Chap.Direction.Left);
            recorder.ping(Chap.Direction.Left, time);
            System.out.println("LEFT");
            });
        setAction(KeyEvent.VK_RIGHT, 0,() -> {
            update.moveChap(Chap.Direction.Right);
            recorder.ping(Chap.Direction.Right, time);
            System.out.println("RIGHT");
            });

        // Ctrl key combinations
        // InputEvent since getKeyStroke() is expecting InputEvent
        setAction(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK, actionBindings.get("exitWithoutSaving"));
        setAction(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, actionBindings.get("exitAndSave"));
        setAction(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, actionBindings.get("resumeSavedGame"));
        setAction(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK, actionBindings.get("startNewGame1"));
        setAction(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK, actionBindings.get("startNewGame2"));

        // Other keys
        setAction(KeyEvent.VK_SPACE, 0, actionBindings.get("pause"));
        setAction(KeyEvent.VK_ESCAPE, 0, actionBindings.get("unpause"));
    }


    public void setRecorder(Recorder r){
        recorder = r;
    }


    public void updatetime(int t){
        time = t;
    }


    public void setGameStateController(GameStateController gsc){
        update = gsc;
    }

}


/**
 *     Controller(GameStateController StateController , Map<String, Runnable> actionBindings){
        update = StateController;
        setAction(KeyEvent.VK_UP, 0,() -> {
            update.moveChap(Chap.Direction.Up);
            recorder.ping(Chap.Direction.Up, time);
            System.out.println("UP");
            }, () -> {});
        setAction(KeyEvent.VK_DOWN, 0,() -> {
            update.moveChap(Chap.Direction.Down);
            recorder.ping(Chap.Direction.Down, time);
            System.out.println("DOWN");
            }, () -> {});
        setAction(KeyEvent.VK_LEFT, 0,() -> {
            update.moveChap(Chap.Direction.Left);
            recorder.ping(Chap.Direction.Left, time);
            System.out.println("LEFT");
            }, () -> {});
        setAction(KeyEvent.VK_RIGHT, 0,() -> {
            update.moveChap(Chap.Direction.Right);
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
    }
 * 
 * 
 */
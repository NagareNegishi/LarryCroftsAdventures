package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * Keys class to handle key events
 */
class Keys implements KeyListener {
    private final Map<Integer,Runnable> actionsPressed= new HashMap<>();
    private final Map<Integer,Runnable> actionsReleased= new HashMap<>();
    public void setAction(int keyCode, Runnable onPressed, Runnable onReleased){
        actionsPressed.put(keyCode, onPressed);
        actionsReleased.put(keyCode, onReleased);
    }
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        actionsPressed.getOrDefault(e.getKeyCode(), ()->{}).run();
    }
    public void keyReleased(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        actionsReleased.getOrDefault(e.getKeyCode(), ()->{}).run();
    }
}
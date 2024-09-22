package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Keys class to handle key events
 */
class Keys implements KeyListener {
    private final Map<KeyStroke,Runnable> actionsPressed= new HashMap<>();// need to check Ctrl(modifiers) key
    private final Map<KeyStroke,Runnable> actionsReleased= new HashMap<>();// so keyevent is not enough
/**
 * note for us:
 * swing can not take key combinations like Ctrl+X as a single key event
 * so we need to use KeyStroke class to handle it
 * keystroke is a combination of key code and modifiers
 * which is part of swing library(but came from awt)
 */


    public void setAction(int keyCode, int modifiers, Runnable onPressed, Runnable onReleased){
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        actionsPressed.put(keyStroke, onPressed);
        actionsReleased.put(keyStroke, onReleased);
    }
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        actionsPressed.getOrDefault(keyStroke, ()->{}).run();
    }
    public void keyReleased(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        actionsReleased.getOrDefault(keyStroke, ()->{}).run();
    }
}
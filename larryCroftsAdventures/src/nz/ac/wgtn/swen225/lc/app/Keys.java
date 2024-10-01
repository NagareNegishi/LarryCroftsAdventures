package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Keys class to handle key events
 * This class was adopted from the swen 225 assignment 1
 * with some modifications to handle key combinations
 *
 * note for us:
 * Swing cannot handle key combinations like Ctrl+X as a single key event,
 * so we need to use KeyStroke class to handle it.
 * keystroke is a combination of key code and modifiers,
 * which is part of swing library (but came from awt).
 */
class Keys implements KeyListener {
    private final Map<KeyStroke,Runnable> actionsPressed= new HashMap<>();// need to check Ctrl(modifiers) key
    private final Map<KeyStroke,Runnable> actionsReleased= new HashMap<>();// so keyevent is not enough



    /**
     * Set the action for the key event or key combination
     * @param keyCode key code
     * @param modifiers modifiers (0 for no modifiers)
     * @param onPressed action to be performed when the key is pressed
     * @param onReleased action to be performed when the key is released
     */
    public void setAction(int keyCode, int modifiers, Runnable onPressed, Runnable onReleased){
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        actionsPressed.put(keyStroke, onPressed);
        actionsReleased.put(keyStroke, onReleased);
    }

    @Override
    public void keyTyped(KeyEvent e){} // not used

    @Override
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread(); // thread safety check
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        actionsPressed.getOrDefault(keyStroke, ()->{}).run();
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        actionsReleased.getOrDefault(keyStroke, ()->{}).run();
    }
}
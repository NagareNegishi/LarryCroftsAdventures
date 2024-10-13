package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Keys class to handle key events
 * This class was adopted from the swen 225 assignment 1
 * with some modifications to handle key combinations, and pause method.
 *
 * note for us:
 * Swing cannot handle key combinations like Ctrl+X as a single key event,
 * so we need to use KeyStroke class to handle it.
 * keystroke is a combination of key code and modifiers,
 * which is part of swing library (but came from awt).
 *
 * I could combine this class into Controller class,
 * However, separating this class from Controller class increases the readability of the code.
 * and make this class reusable in the future.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
class Keys implements KeyListener {
    private final Map<KeyStroke,Runnable> actionsPressed= new HashMap<>();
    private boolean paused = false;
    private boolean recorderMode = false;
    /*
     * Set of direction keys,
     * used to prevent the chap from moving while the game is paused.
     * This allows App class to pause the game without changing other modules.
     */
    private final Set<KeyStroke> directionKeys = Set.of(
        KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
        KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
        KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)
    );

    /**
     * Set the action for the key event or key combination
     * @param keyCode key code
     * @param modifiers modifiers (0 for no modifiers)
     * @param onPressed action to be performed when the key is pressed
     */
    public void setAction(int keyCode, int modifiers, Runnable onPressed){
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        actionsPressed.put(keyStroke, onPressed);
    }

    /**
     * Handle the key pressed event
     * @param e key event
     */
    @Override
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread(); // thread safety check
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        if (!recorderMode && (!paused || !directionKeys.contains(keyStroke))) {
            actionsPressed.getOrDefault(keyStroke, () -> {}).run();
        }
    }

    /**
     * Pause the key listener
     */
    public void pause(boolean pause){
        paused = pause;
    }

    /**
     * Set the recorder mode
     * @param mode true if the recorder mode is on
     */
    public void setRecorderMode(boolean mode){
        recorderMode = mode;
    }

    // unused methods
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){}
}
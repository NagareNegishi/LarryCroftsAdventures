package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;


/**
 * Controller class that extends Keys class
 */
class Controller extends Keys{


 
/**
   * Constructor for the Controller class
   * @param c Camera object
   *
   * @param keyBindings Map of key bindings
   */
    Controller(MockCamera c){
    setAction(KeyEvent.VK_UP, c.set(Direction::up), c.set(Direction::none));
    setAction(KeyEvent.VK_DOWN, c.set(Direction::down), c.set(Direction::none));
    setAction(KeyEvent.VK_LEFT, c.set(Direction::left), c.set(Direction::none));
    setAction(KeyEvent.VK_RIGHT, c.set(Direction::right), c.set(Direction::none));

/**
    // Ctrl key combinations
    setAction(KeyEvent.VK_X | KeyEvent.CTRL_DOWN_MASK, this::exitGameWithoutSaving, () -> {});
    setAction(KeyEvent.VK_S | KeyEvent.CTRL_DOWN_MASK, this::exitGameAndSave, () -> {});
    setAction(KeyEvent.VK_R | KeyEvent.CTRL_DOWN_MASK, this::resumeSavedGame, () -> {});
    setAction(KeyEvent.VK_1 | KeyEvent.CTRL_DOWN_MASK, () -> startNewGame(1), () -> {});
    setAction(KeyEvent.VK_2 | KeyEvent.CTRL_DOWN_MASK, () -> startNewGame(2), () -> {});

    // Other keys
    setAction(KeyEvent.VK_SPACE, this::pauseGame, () -> {});
    setAction(KeyEvent.VK_ESCAPE, this::resumeGame, () -> {});
*/



}
}

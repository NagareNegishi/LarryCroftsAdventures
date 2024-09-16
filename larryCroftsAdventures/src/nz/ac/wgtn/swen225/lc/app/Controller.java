package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;


/**
 * Controller class that extends Keys class
 */
class Controller extends Keys{


 
/**
   * Constructor for the Controller class
   * @param c MockCamera object expecting "Chap" object
   */
    Controller(MockCamera c, Runnable pause, Runnable unpause){
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
*/
    // Other keys
    setAction(KeyEvent.VK_SPACE, pause, () -> {});
    setAction(KeyEvent.VK_ESCAPE, unpause, () -> {});

/**
 * i want to set controller in constructor
 * but many method are defined in App.
 * i dont want to pass App to Controller, controller shouldnt access App so no static too
 * maybe just make collection of methods in App and pass that to Controller?
 * its not elegant though.....
 */


}
}

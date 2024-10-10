package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A panel that displays the menu options, options include:
 * - Pause/ unpause the game.
 * - Save the game.
 * - Load a saved game.
 * - Display help for the game.
 * - Exit the game.
 * - Show the recorder panel.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class MenuPanel extends JPanel{
    private static final long serialVersionUID= 1L;
    private JButton pauseButton;
    public static final String HELP = "Help:\n" +
    "Use the arrow keys to move the hero.\n" +
    "Collect all treasures to complete the level.\n" +
    "Collect keys to unlock doors.\n" +
    "Avoid enemies.\n" +
    "Press Ctrl + X to exit without saving.\n" +
    "Press Ctrl + S to exit with saving the game.\n" +
    "Press Ctrl + R to resume a saved game.\n" +
    "Press Ctrl + 1 to start a new game at level 1.\n" +
    "Press Ctrl + 2 to start a new game at level 2.\n" +
    "Press Space to pause the game.\n" +
    "Press Esc to resume the game.\n" +
    "Note: all key inputs are unavailable during the recorder mode.\n";

    /**
     * Create a new MenuPanel with the given ActionListener.
     * @param listener
     */
    MenuPanel(ActionListener listener){
        setLayout(new GridLayout(0, 1));
        pauseButton = ComponentFactory.createButton("Pause", "pause", listener);
        add(pauseButton);
        add(ComponentFactory.createButton("Save", "save", listener));
        add(ComponentFactory.createButton("Load", "load", listener));
        add(ComponentFactory.createButton("Help", "help", listener));
        add(ComponentFactory.createButton("Exit", "exit", listener));
        add(ComponentFactory.createButton(ComponentFactory.format("Show<br>Recorder"), "toggleMenu", listener));
    }

    /**
     * Set the text and action command of the pause button.
     * Used to switch the functionality of the button between pause and unpause.
     * @param text
     */
    public void setPauseButton(String text) {
        assert text.equals("Pause") || text.equals("Unpause") : "Invalid text for pause button";
        pauseButton.setText(text);
        pauseButton.setActionCommand(text.toLowerCase());
    }
}

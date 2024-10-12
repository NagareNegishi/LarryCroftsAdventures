package nz.ac.wgtn.swen225.lc.app;

import java.awt.Color;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * GameDialogs enum class that contains all the dialogs that can be shown in the game.
 * PauseDialog can not be enum singleton because it needs to extend JDialog.
 * This class must call InitializeDialogs(JFrame parent) to initialize all dialogs.
 * (This class showcases the use of enum singleton pattern and Stream.)
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public enum GameDialogs {
    PAUSE(ComponentFactory.format("Game is paused",false), Color.BLACK, new Color(150, 150, 0)),
    START(ComponentFactory.format("New Game<br>Press 'Esc' to start",false), Color.BLUE, Color.YELLOW),
    GAMEOVER(ComponentFactory.format("Game Over<br>'Esc' to retry",false), Color.RED, Color.BLACK),
    VICTORY(ComponentFactory.format("Victory<br>'Esc' to play again",false), Color.GREEN, Color.ORANGE);

    


    PauseDialog dialog;
    String message;
    Color backgroundColor;
    Color textColor;


    /**
     * Create a new GameDialogs with the given message, text color, background color, and opacity.
     * User must call InitializeDialog(JFrame parent) to initialize the dialog.
     * @param message
     * @param backgroundColor
     * @param textColor
     */
    GameDialogs(String message, Color backgroundColor, Color textColor) {
        this.message = message;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Initialize the dialog with the given parent.
     * Only this class can call this method.
     * @param parent the parent JFrame
     * @param renderer the renderer JPanel
     */
    private void initializeDialog(JFrame parent, JPanel renderer) {
        dialog = new PauseDialog(parent, renderer, message, backgroundColor, textColor);
    }

    /**
     * Initialize all dialogs with the given parent.
     * @param parent the parent JFrame
     * @param renderer the renderer JPanel
     */
    public static void initializeDialogs(JFrame parent, JPanel renderer) {
        Stream.of(GameDialogs.values()).forEach(type -> type.initializeDialog(parent, renderer));
    }

    /**
     * Show the dialog.
     */
    public void show() {
        assert dialog != null: "Dialog is not initialized";
        dialog.setVisible(true);
    }

    /**
     * Hide the dialog.
     */
    public void hide() {
        assert dialog != null: "Dialog is not initialized";
        dialog.setVisible(false);
    }

    /**
     * Hide all dialogs.
     */
    public static void hideAll() {
        Stream.of(GameDialogs.values()).forEach(type -> type.hide());
        }


}

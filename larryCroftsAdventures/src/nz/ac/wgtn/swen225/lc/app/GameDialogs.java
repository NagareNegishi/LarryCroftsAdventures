package nz.ac.wgtn.swen225.lc.app;

import java.awt.Color;
import java.util.stream.Stream;

import javax.swing.JFrame;

/**
 * GameDialogs enum class that contains all the dialogs that can be shown in the game.
 * PauseDialog can not be enum singleton because it needs to extend JDialog.
 * This class must call InitializeDialogs(JFrame parent) to initialize all dialogs.
 * (This class showcases the use of enum singleton pattern and Stream.)
 */
public enum GameDialogs {
    PAUSE("Game is paused", Color.BLACK, new Color(150, 150, 0), 0.75),
    START("Press Escape to start", Color.BLUE, Color.YELLOW, 0.75),
    GAMEOVER("Game Over 'Esc' to retry", Color.RED, Color.BLACK, 0.75),
    VICTORY("Victory 'Esc' to play again", Color.GREEN, Color.ORANGE, 0.75);

    PauseDialog dialog;
    String message;
    Color textColor;
    Color backgroundColor;
    double opacity;

    /**
     * Create a new GameDialogs with the given message, text color, background color, and opacity.
     * User must call InitializeDialog(JFrame parent) to initialize the dialog.
     * @param message
     * @param textColor
     * @param backgroundColor
     * @param opacity
     */
    GameDialogs(String message, Color textColor, Color backgroundColor, double opacity) {
        this.message = message;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.opacity = opacity;
    }

    /**
     * Initialize the dialog with the given parent.
     * Only this class can call this method.
     * @param parent the parent JFrame
     */
    private void InitializeDialog(JFrame parent) {
        dialog = new PauseDialog(parent, message, textColor, backgroundColor, opacity);
    }

    /**
     * Initialize all dialogs with the given parent.
     * @param parent the parent JFrame
     */
    public static void InitializeDialogs(JFrame parent) {
        Stream.of(GameDialogs.values()).forEach(dialog -> dialog.InitializeDialog(parent));
    }

    /**
     * Show the dialog.
     */
    public void show() {
        assert dialog != null;
        dialog.setVisible(true);
    }

    /**
     * Hide the dialog.
     */
    public void hide() {
        assert dialog != null;
        dialog.setVisible(false);
    }

    /**
     * Hide all dialogs.
     */
    public static void hideAll() {
        Stream.of(GameDialogs.values()).forEach(dialog -> dialog.hide());
        }
}

package nz.ac.wgtn.swen225.lc.app;

import java.awt.Color;
import java.util.stream.Stream;

import javax.swing.JFrame;

/**
 * i know you can be singleton/.......
 * 
 */
public enum GameDialogs {
    PAUSE("Game is paused", Color.BLACK, new Color(150, 150, 0), 0.75),
    START("Press Escape to start", Color.BLUE, Color.YELLOW, 0.75),
    GAMEOVER("Game Over\n Press Escape to retry", Color.RED, Color.BLACK, 0.75),
    VICTORY("Victory\nPress Escape to play again", Color.GREEN, Color.ORANGE, 0.75);

    PauseDialog dialog;
    String message;
    Color textColor;
    Color backgroundColor;
    double opacity;

    GameDialogs(String message, Color textColor, Color backgroundColor, double opacity) {
        this.message = message;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.opacity = opacity;
    }

    public void InitializeDialog(JFrame parent) {
        assert dialog == null: "Dialog already initialized";
        dialog = new PauseDialog(parent, message, textColor, backgroundColor, opacity);
    }

    public void InitializeDialogs(JFrame parent) {
        for (GameDialogs dialog : GameDialogs.values()) {
            dialog.InitializeDialog(parent);
        }
    }

    public void show() {
        dialog.setVisible(true);
    }

    public void hide() {
        dialog.setVisible(false);
    }

    public void hideAllDialogs() {
        Stream.of(GameDialogs.values()).forEach(dialog -> dialog.hide());
        }
}

package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel that displays the game information, including the time, level, keys, and treasures.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class GameInfoPanel extends JPanel {
    private JLabel timeTextLabel;
    private JLabel timeLabel;
    private JLabel levelLabel;
    private JLabel keysLabel;
    private JLabel treasuresLabel;
    
    /**
     * Create a new GameInfoPanel with the given width and height.
     * @param width
     * @param height
     */
    public GameInfoPanel(int width, int height) {
        setLayout(new BorderLayout(0,10));
        setPreferredSize(new Dimension(width, height)); //repetitive?? app will call it later too

        //*need better layout */
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        timerPanel.setPreferredSize(new Dimension(width, height/2));
        timeTextLabel = new JLabel("Time:    ");
        timeTextLabel.setFont(getFont().deriveFont((float)width/4));
        timeLabel = new JLabel("60");
        timeLabel.setFont(getFont().deriveFont((float)width/2));
        timerPanel.add(timeTextLabel);
        timerPanel.add(timeLabel);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        levelLabel = new JLabel("Level: 1");
        keysLabel = new JLabel("Keys: 0");
        treasuresLabel = new JLabel("Treasures left: 10");

        infoPanel.add(levelLabel);
        infoPanel.add(keysLabel);
        infoPanel.add(treasuresLabel);
        add(timerPanel,BorderLayout.NORTH);
        add(infoPanel,BorderLayout.CENTER);

    }

    /*
     * I am fully aware that the following setters can be one method that takes a string and an int
     * However, those methods are called by other modules,
     * and readability is more important when you collaborate with others.
     */

    /**
     * set the time on the timer label, and update the color of the timer
     * @param time
     */
    public void setTime(int time) {
        timeLabel.setText(String.valueOf(time));
        updateTimerColor(time);
    }

    /**
     * set the level on the level label
     * @param level
     */
    public void setLevel(int level) {
        levelLabel.setText("Level: " + level);
    }

    /**
     * set the keys on the keys label
     * @param keys
     */
    public void setKeys(int keys) {
        keysLabel.setText("Keys: " + keys);
    }

    /**
     * set the treasures on the treasures label
     * @param treasures
     */
    public void setTreasures(int treasures) {
        treasuresLabel.setText("Treasures left: " + treasures);
    }

    /**
     * update the color of the timer label based on the time
     * @param time
     */
    public void updateTimerColor(int time) {
        Color color = switch(time/10) {// I can't believe new switch can't limit the scope of the variable
            case 0  -> Color.RED;
            case 1 ->Color.ORANGE;
            default -> Color.BLACK;
        };
        timeLabel.setForeground(color);
    }
}

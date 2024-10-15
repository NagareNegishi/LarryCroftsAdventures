package nz.ac.wgtn.swen225.lc.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nz.ac.wgtn.swen225.lc.app.ComponentFactory.infoPanel;

/**
 * A panel that displays the game information, including the time, level, keys, and treasures.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class GameInfoPanel extends JPanel {
    private JLabel timeTextLabel;
    private JLabel timeLabel;
    private JLabel recorderLabel;
    private infoPanel levelPanel;
    private infoPanel keysPanel;
    private infoPanel treasuresPanel;
    private static final int baseRatio = 10;
    private static final float timeTextScale = 2.5f;
    private static final float timeScale = 6.5f;
    private static final float recorderScale = 1.6f;
    private static final float infoTitleScale = 1.2f;
    private static final float infoValueScale = 2.0f;
    private float fontSize;

    /**
     * Create a new GameInfoPanel with the given width and height.
     * @param width
     * @param height
     */
    public GameInfoPanel(int width, int height) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerPanel.setPreferredSize(new Dimension(width, height/4));
        timeTextLabel = new JLabel("Time:", JLabel.CENTER);
        timeTextLabel.setFont(getFont().deriveFont((float)width/4));
        timeTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel = new JLabel("60");
        timeLabel.setFont(getFont().deriveFont((float)(width/1.6)));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerPanel.add(timeTextLabel);
        timerPanel.add(timeLabel);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setPreferredSize(new Dimension(width, height/4*3));
        recorderLabel = new JLabel(ComponentFactory.format("Recorder<br>mode",false), JLabel.CENTER);
        recorderLabel.setForeground(Color.RED);
        recorderLabel.setVisible(false);
        levelPanel = ComponentFactory.createInfoPanel("Level");
        keysPanel = ComponentFactory.createInfoPanel("Keys");
        treasuresPanel = ComponentFactory.createInfoPanel("Treasures Left");
        infoPanel.add(recorderLabel);
        infoPanel.add(levelPanel);
        infoPanel.add(keysPanel);
        infoPanel.add(treasuresPanel);
        add(timerPanel);
        add(infoPanel);
    }

    /**
     * set the time on the timer label, and update the color of the timer
     * @param time
     */
    public void setTime(int time) {
        assert time >= 0 : "Time cannot be negative";
        timeLabel.setText(String.valueOf(time));
        updateTimerColor(time);
    }

    /**
     * set the level on the level label
     * @param level
     */
    public void setLevel(int level) {
        assert level == 1 || level == 2 : "Invalid level";// hardcoding for this project
        levelPanel.setValue(String.valueOf(level));
    }

    /**
     * set the keys on the keys label
     * @param keys
     */
    public void setKeys(Set<String> keys) {
        StringBuilder coloredKeys = new StringBuilder("<html>");
        keys.forEach(k -> {

            String color = getColorForKey(k);
            System.out.println("color is:" + color);
            // Append each key with its color
            coloredKeys.append("<span style='color: ")
            .append(color)
            .append("'> ◆ </span> ");
        });
        coloredKeys.append("</html>");

        keysPanel.setValue(coloredKeys.toString());

    }

    private String getColorForKey(String key) {
        return switch (key.toLowerCase()) {
            case "red" -> "red";
            case "blue" -> "blue";
            default -> "black";
        };
    }

    /**
     * set the treasures on the treasures label
     * @param treasures
     */
    public void setTreasures(int treasures) {
        assert treasures >= 0 : "Treasures cannot be negative";
        treasuresPanel.setValue(String.valueOf(treasures));
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

    /**
     * set the recorder mode label visible or invisible
     * @param isRecorder
     */
    public void setRecorderMode(boolean isRecorder) {
        recorderLabel.setVisible(isRecorder);
    }

    /**
     * add a resize listener to the parent frame
     * @param parent
     */
    public void addParentResizeListener(JFrame parent) {
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateFont();
            }
        });
    }

    /**
     * update the font size of the labels
     */
    public void updateFont() {
        fontSize = getWidth() / baseRatio;
        timeTextLabel.setFont(timeTextLabel.getFont().deriveFont(fontSize * timeTextScale));
        timeLabel.setFont(timeLabel.getFont().deriveFont(fontSize * timeScale));
        recorderLabel.setFont(recorderLabel.getFont().deriveFont(fontSize * recorderScale));
        levelPanel.updateFont(fontSize * infoTitleScale, fontSize * infoValueScale);
        keysPanel.updateFont(fontSize * infoTitleScale, fontSize * infoValueScale);
        treasuresPanel.updateFont(fontSize * infoTitleScale, fontSize * infoValueScale);
    }
}

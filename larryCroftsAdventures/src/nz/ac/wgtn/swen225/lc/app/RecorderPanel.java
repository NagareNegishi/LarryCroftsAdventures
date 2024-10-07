package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
/**
 * A panel that displays the recorder options, including step, auto replay, speed control, load recording, save recording, and show menu.
 * I could make generic class for this and MenuPanel,
 * but but the two classes are different enough that it would be more trouble than it's worth.
 */
public class RecorderPanel extends JPanel {
    private JButton stepButton;
    private JButton backButton;
    private JToggleButton autoReplayToggle;
    private JSlider speedControl;
    private JButton loadRecordingButton;
    private JButton saveRecordingButton;
    private JButton helpButton;
    private JButton toggleButton;
    private enum PlayType {AUTO, MANUAL}
    private PlayType current = PlayType.AUTO;
    public static final String HELP = "Help:\n" +
    "Use the arrow keys to move the hero.\n" +
    "Collect all treasures to complete the level.\n" +
    "Collect keys to unlock doors.\n" +
    "Avoid enemies.\n" +
    "Press Ctrl + X to exit without saving.\n" +
    "Press Ctrl + S to save the game.\n" +
    "Press Ctrl + R to resume a saved game.\n" +
    "Press Ctrl + 1 to start a new game at level 1.\n" +
    "Press Ctrl + 2 to start a new game at level 2.\n" +
    "Press Space to pause the game.\n" +
    "Press Esc to resume the game.\n";

    /**
     * Create a new RecorderPanel with the given ActionListener and Consumer.
     * @param listener
     * @param sliderConsumer
     */
    public RecorderPanel(ActionListener listener, Consumer<Integer> sliderConsumer) {
        setLayout(new GridLayout(0, 1));
        initializeComponents(listener, sliderConsumer);
        addComponents();
    }

    private void initializeComponents(ActionListener listener, Consumer<Integer> sliderConsumer) {
        stepButton = ComponentFactory.createButton("Step", "step", listener);
        backButton = ComponentFactory.createButton("Step Back", "back", listener);
        autoReplayToggle = ComponentFactory.createToggleButton("Auto Replay", "autoReplay", listener);
        speedControl = ComponentFactory.createSlider(1, 5, 3, sliderConsumer);
        loadRecordingButton = ComponentFactory.createButton("Load Recording", "loadRecording", listener);
        saveRecordingButton = ComponentFactory.createButton("Save Recording", "saveRecording", listener);
        helpButton = ComponentFactory.createButton("Help", "help", listener);
        toggleButton = ComponentFactory.createButton("Show Menu", "toggle", listener);
    }

    private void addComponents() {
        add(stepButton);
        add(backButton);
        add(autoReplayToggle);
        add(new JLabel("Speed:"));
        add(speedControl);
        add(loadRecordingButton);
        add(saveRecordingButton);
        add(helpButton);
        add(toggleButton);
    }



}
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
    private JButton toggleButton;
    private enum PlayType {AUTO, MANUAL}
    private PlayType current = PlayType.AUTO;

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
        add(toggleButton);
    }



}
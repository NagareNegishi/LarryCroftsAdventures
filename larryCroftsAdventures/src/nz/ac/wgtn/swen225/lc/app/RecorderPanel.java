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
 * A panel that displays the recorder options, options include:
 * - Step forward, step back one step.
 * - Auto replay.
 * - Speed control for auto replay.
 * - Load recording.
 * - Save recording.
 * - Display help for the recorder UI.
 * - Go back to the main menu.
 *
 * @author Nagare Negishi
 * @studentID 300653779
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
    private boolean isAuto;
    public static final String HELP = "Recorder UI Help:\n" +
    "- Step: Move forward one step in the recording.\n" +
    "- Step Back: Rewind one step in the recording.\n" +
    "- Auto Replay: Toggle automatic playback of the recording.\n" +
    "- Speed Slider: Adjust the speed of auto replay.\n" +
    "- Load Recording: Load a saved game recording.\n" +
    "- Save Recording: Save the current game recording.\n" +
    "- Show Menu: Switch to the main menu panel.\n\n" +
    "Note: When Auto Replay is on, only the speed slider remains active.\n" +
    "All other buttons are disabled during auto replay.\n";

    /**
     * Create a new RecorderPanel with the given ActionListener and Consumer.
     * @param listener
     * @param sliderConsumer
     */
    public RecorderPanel(ActionListener listener, Consumer<Integer> sliderConsumer) {
        setLayout(new GridLayout(0, 1));
        initializeComponents(listener, sliderConsumer);
        addComponents();
        autoMode();
    }

    /**
     * Set the text and action command/ listener for each component.
     * @param text
     */
    private void initializeComponents(ActionListener listener, Consumer<Integer> sliderConsumer) {
        stepButton = ComponentFactory.createButton("\u23E9", "step", listener);
        backButton = ComponentFactory.createButton("\u23EA", "back", listener);
        autoReplayToggle = ComponentFactory.createToggleButton(ComponentFactory.format("Auto Replay",false), "autoReplay",
            e -> {listener.actionPerformed(e);
                autoMode();
        });
        speedControl = ComponentFactory.createSlider(1, 5, 3, sliderConsumer);
        loadRecordingButton = ComponentFactory.createButton(ComponentFactory.format("Load Recording",false), "loadRecording", listener);
        saveRecordingButton = ComponentFactory.createButton(ComponentFactory.format("Save Recording",false), "saveRecording", listener);
        helpButton = ComponentFactory.createButton("Help", "helpRecorder", listener);
        toggleButton = ComponentFactory.createButton("Show Menu", "toggleRecorder", listener);
    }

    /**
     * Add the components to the panel.
     */
    private void addComponents() {
        JPanel stepPanel = new JPanel(new GridLayout(2, 1));
        stepPanel.add(new JLabel("Step:", JLabel.CENTER));
        JPanel steps = new JPanel(new GridLayout(1, 2));
        steps.add(backButton);
        steps.add(stepButton);
        stepPanel.add(steps);
        add(stepPanel);
        add(autoReplayToggle);
        JPanel speedPanel = new JPanel(new GridLayout(2,1));
        speedPanel.add(new JLabel("Speed:", JLabel.CENTER));
        speedPanel.add(speedControl);
        add(speedPanel);
        add(loadRecordingButton);
        add(saveRecordingButton);
        add(helpButton);
        add(toggleButton);
    }

    /**
     * Enable or disable components based on auto replay status.
     */
    private void autoMode() {
        isAuto = autoReplayToggle.isSelected();
        stepButton.setEnabled(!isAuto);
        backButton.setEnabled(!isAuto);
        loadRecordingButton.setEnabled(!isAuto);
        saveRecordingButton.setEnabled(!isAuto);
        helpButton.setEnabled(!isAuto);
        toggleButton.setEnabled(!isAuto);
        // Speed control remains enabled
        speedControl.setEnabled(true);
    }

}
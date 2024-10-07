package nz.ac.wgtn.swen225.lc.app;

import java.awt.FlowLayout;
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
 * Note: I could make generic class for this and MenuPanel,
 * However, the two panels are different enough to have separate classes.
 * And it increases readability.
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
        stepButton = ComponentFactory.createButton("Step", "step", listener);
        backButton = ComponentFactory.createButton("Step Back", "back", listener);
        autoReplayToggle = ComponentFactory.createToggleButton("Auto Replay", "autoReplay",
            e -> {listener.actionPerformed(e);
                autoMode();
        });
        speedControl = ComponentFactory.createSlider(1, 5, 3, sliderConsumer);
        loadRecordingButton = ComponentFactory.createButton("Load Recording", "loadRecording", listener);
        saveRecordingButton = ComponentFactory.createButton("Save Recording", "saveRecording", listener);
        helpButton = ComponentFactory.createButton("Help", "help", listener);
        toggleButton = ComponentFactory.createButton("Show Menu", "toggle", listener);
    }

    /**
     * Add the components to the panel.
     */
    private void addComponents() {
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        stepPanel.add(backButton);
        stepPanel.add(stepButton);
        add(stepPanel);
        add(autoReplayToggle);
        add(new JLabel("Speed:"));
        add(speedControl);
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
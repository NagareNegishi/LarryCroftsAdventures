package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;

public class RecorderPanel extends JPanel {
    private JButton stepButton;
    private JToggleButton autoReplayToggle;
    private JSlider speedControl;
    private JButton loadRecordingButton;
    private JButton startStopRecordingButton;
    private JButton toggleButton;

    public RecorderPanel(ActionListener listener, Consumer<Integer> sliderConsumer) {
        setLayout(new GridLayout(0, 1));
        initializeComponents(listener, sliderConsumer);
        addComponents();
    }

    private void initializeComponents(ActionListener listener, Consumer<Integer> sliderConsumer) {
        stepButton = createButton("Step", "step", listener);
        autoReplayToggle = createToggleButton("Auto Replay", "autoReplay", listener);

        speedControl = createSlider(1, 5, 3, e -> sliderConsumer.accept(getSpeed()));// so .. am just returning the value of the slider

       /** speedControl = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        speedControl.setFocusable(false);
        speedControl.addChangeListener(e -> getSpeed());//e is changeEvent ...then i want ...int?*/

        loadRecordingButton = createButton("Load Recording", "loadRecording", listener);
        startStopRecordingButton = createButton("Start Recording", "toggleRecording", listener);
        toggleButton = createButton("Show Menu", "toggle", listener);
    }


    private JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    private JToggleButton createToggleButton(String text, String actionCommand, ActionListener listener) {
        JToggleButton button = new JToggleButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    /**
     * unlike buttons, slider requires a ChangeListener instead of ActionListener
     * which has one method stateChanged(ChangeEvent e)
     * the method is called whenever the slider's value changes
     * but the source of the event is the slider itself
     * what i want is int value of the slider
     * so i can use getValue() method of the slider...mm but not here
     * 
     */
    private JSlider createSlider(int min, int max, int value, ChangeListener listener) {//ChangeListener?????
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.setFocusable(false);
        slider.addChangeListener(listener);
        return slider;
    }

    private void addComponents() {
        add(stepButton);
        add(autoReplayToggle);
        add(new JLabel("Speed:"));
        add(speedControl);
        add(loadRecordingButton);
        add(startStopRecordingButton);
        add(toggleButton);
    }

//then i shall take int
    private int getSpeed() {
        return speedControl.getValue(); //getting the value of the slider , mm chicken and egg problem...
    }


}
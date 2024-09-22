package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class RecorderPanel extends JPanel {
    private JButton stepButton;
    private JToggleButton autoReplayToggle;
    private JSlider speedControl;
    private JButton loadRecordingButton;
    private JButton saveRecordingButton;
    private JButton toggleButton;

    public RecorderPanel(ActionListener listener, Consumer<Integer> sliderConsumer) {
        setLayout(new GridLayout(0, 1));
        initializeComponents(listener, sliderConsumer);
        addComponents();
    }

    private void initializeComponents(ActionListener listener, Consumer<Integer> sliderConsumer) {
        stepButton = ComponentFactory.createButton("Step", "step", listener);
        autoReplayToggle = ComponentFactory.createToggleButton("Auto Replay", "autoReplay", listener);
        speedControl = ComponentFactory.createSlider(1, 5, 3, sliderConsumer);
        loadRecordingButton = ComponentFactory.createButton("Load Recording", "loadRecording", listener);
        saveRecordingButton = ComponentFactory.createButton("Save Recording", "saveRecording", listener);
        toggleButton = ComponentFactory.createButton("Show Menu", "toggle", listener);
    }


   

    private void addComponents() {
        add(stepButton);
        add(autoReplayToggle);
        add(new JLabel("Speed:"));
        add(speedControl);
        add(loadRecordingButton);
        add(saveRecordingButton);
        add(toggleButton);
    }



}
package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;

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
    private JButton startStopRecordingButton;
    private JButton toggleButton;

    public RecorderPanel() {
        setLayout(new GridLayout(0, 1));
        initializeComponents();
        addComponents();
    }

    private void initializeComponents() {
        stepButton = new JButton("Step");
        autoReplayToggle = new JToggleButton("Auto-Replay");
        speedControl = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        loadRecordingButton = new JButton("Load Recording");
        startStopRecordingButton = new JButton("Start Recording");
        toggleButton = new JButton("Show Menu");
        

        // Add action listeners (to be implemented later)
        stepButton.addActionListener(e -> step());
        autoReplayToggle.addActionListener(e -> toggleAutoReplay());
        speedControl.addChangeListener(e -> adjustSpeed());
        loadRecordingButton.addActionListener(e -> loadRecording());
        startStopRecordingButton.addActionListener(e -> toggleRecording());
        toggleButton.addActionListener(e -> System.out.println("Show Menu"));
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

    // Placeholder methods to be implemented later
    private void step() {
        System.out.println("Step");
    }

    private void toggleAutoReplay() {
        System.out.println("Auto-Replay: " + autoReplayToggle.isSelected());
    }

    private void adjustSpeed() {
        System.out.println("Speed: " + speedControl.getValue());
    }

    private void loadRecording() {
        System.out.println("Load Recording");
    }

    private void toggleRecording() {
        if (startStopRecordingButton.getText().equals("Start Recording")) {
            startStopRecordingButton.setText("Stop Recording");
            System.out.println("Start Recording");
        } else {
            startStopRecordingButton.setText("Start Recording");
            System.out.println("Stop Recording");
        }
    }
}
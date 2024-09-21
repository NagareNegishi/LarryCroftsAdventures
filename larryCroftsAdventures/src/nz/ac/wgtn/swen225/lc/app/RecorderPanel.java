package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

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

    public RecorderPanel(ActionListener listener) {
        setLayout(new GridLayout(0, 1));
        initializeComponents(listener);
        addComponents();
    }

    private void initializeComponents(ActionListener listener) {
        stepButton = createButton("Step", "step", listener);
        autoReplayToggle = new JToggleButton("Auto-Replay");
        autoReplayToggle.addActionListener(listener);
        autoReplayToggle.setActionCommand("autoReplay");
        autoReplayToggle.setFocusable(false);

        speedControl = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        speedControl.setFocusable(false);
        speedControl.addChangeListener(e -> adjustSpeed());

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
   /**  private void initializeComponents() {
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
    }*/

    private void addComponents() {
        add(stepButton);
        add(autoReplayToggle);
        add(new JLabel("Speed:"));
        add(speedControl);
        add(loadRecordingButton);
        add(startStopRecordingButton);
        add(toggleButton);
    }



    private void adjustSpeed() {
        System.out.println("Speed: " + speedControl.getValue());
    }


}
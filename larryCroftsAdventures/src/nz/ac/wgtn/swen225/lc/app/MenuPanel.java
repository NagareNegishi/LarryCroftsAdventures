package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
    private JButton pauseButton;
	
    private static final long serialVersionUID= 1L;
    MenuPanel(ActionListener listener){
        setLayout(new GridLayout(0, 1));
        pauseButton = createButton("Pause", "pause", listener);
        add(pauseButton);
        add(createButton("Save", "save", listener));
        add(createButton("Load", "load", listener));
        add(createButton("Help", "help", listener));
        add(createButton("Exit", "exit", listener));

        JButton toggleRecorderButton = createButton("Show Recorder", "toggle", listener);
        add(toggleRecorderButton);
    }

    /**
     * Create a button with the given text, action command, and listener.
     * @param text
     * @param actionCommand
     * @param listener
     * @return
     */
    private JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    /**
     * Set the text of the pause button.
     * @param text
     */
    public void setPauseButton(String text) {
        pauseButton.setText(text);
        pauseButton.setActionCommand(text.toLowerCase());
    }
}

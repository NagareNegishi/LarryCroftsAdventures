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
        pauseButton = ComponentFactory.createButton("Pause", "pause", listener);
        add(pauseButton);
        add(ComponentFactory.createButton("Save", "save", listener));
        add(ComponentFactory.createButton("Load", "load", listener));
        add(ComponentFactory.createButton("Help", "help", listener));
        add(ComponentFactory.createButton("Exit", "exit", listener));
        add(ComponentFactory.createButton("Show Recorder", "toggle", listener));
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

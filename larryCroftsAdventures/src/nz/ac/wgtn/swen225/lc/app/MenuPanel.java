package nz.ac.wgtn.swen225.lc.app;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{

  //if im using pause button for unpause, i can just change the text of the button
  //but then i need field for the button
	
    private static final long serialVersionUID= 1L;
    MenuPanel(ActionListener listener){
      setLayout(new GridLayout(0, 1));
      add(createButton("Pause", "pause", listener));
      add(createButton("Save", "save", listener));
      add(createButton("Load", "load", listener));
      add(createButton("Help", "help", listener));
      add(createButton("Exit", "exit", listener));
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
        return button;
    }
    
}

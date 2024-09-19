package nz.ac.wgtn.swen225.lc.app;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Jdialog takes 3 parameters, JFrame as parent, String as title
 * and boolean as modal(it will block other windows).
 */
public class PauseDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    

    public PauseDialog(JFrame parent, String text, Color backgroundColor, Color textColor) {
        super(parent, "Game Paused", false);
        initializeUI(text, backgroundColor, textColor);
    }

    private void initializeUI(String text, Color backgroundColor, Color textColor) {
        setUndecorated(true); // remove title bar
        setBackground(new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 150)); // semi-transparent
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setForeground(textColor);
        panel.add(label);
        setContentPane(panel);// takeover the dialog

        setSize(580, 360); // get size later
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // do nothing on close
        setFocusableWindowState(false); // do not allow focus
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setLocationRelativeTo(getParent());
        }
        super.setVisible(visible);
    }
}
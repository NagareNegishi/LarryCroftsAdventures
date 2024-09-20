package nz.ac.wgtn.swen225.lc.app;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
    private static final double WIDTH_PERCENTAGE = 0.8;
    

    public PauseDialog(JFrame parent, String text, Color backgroundColor, Color textColor) {
        super(parent, "Game Paused", false);
        initializeUI(text, backgroundColor, textColor);
        addParentResizeListener(parent);
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

        //setSize(580, 360); // get size later
        //setLocationRelativeTo(getParent());
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

    /**
     * Dialog want to know the parent's size change
     * So I need component listener, but i dont need all methods
     * So I use ComponentAdapter
     */
    private void addParentResizeListener(JFrame parent) {
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizeAndPosition();
            }
        });
    }

    /**
     * Update the size and position of the dialog
     */
    private void updateSizeAndPosition() {
        JFrame parent = (JFrame) getParent();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        int dialogWidth = (int) (parentWidth * WIDTH_PERCENTAGE);
        setSize(dialogWidth, parentHeight);
        setLocationRelativeTo(parent);
    }
}
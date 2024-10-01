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
 * A dialog that displays a message when the game is paused.
 * It is semi-transparent and blocks the game window.
 */
public class PauseDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final double WIDTH_PERCENTAGE; // how much of the parent it want to cover
    

    /**
     * Create a new PauseDialog with the given parent, text, background color, text color, and ratio.
     * @param parent
     * @param text
     * @param backgroundColor
     * @param textColor
     * @param ratio
     */
    public PauseDialog(JFrame parent, String text, Color backgroundColor, Color textColor, double ratio) {
        // JDialog takes 3 parameters, JFrame as parent, String as title and boolean as modal(it will block other windows)
        super(parent, "Game Paused", false);
        WIDTH_PERCENTAGE = ratio;
        initializeUI(text, backgroundColor, textColor);
        addParentResizeListener(parent); // listen to parent's resize event
    }

    /**
     * Initialize the UI of the dialog.
     * @param text
     * @param backgroundColor
     * @param textColor
     */
    private void initializeUI(String text, Color backgroundColor, Color textColor) {
        setUndecorated(true); // remove title bar
        setBackground(new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 150)); // semi-transparent
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); // make it transparent

        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setForeground(textColor);
        panel.add(label);
        setContentPane(panel);// takeover the dialog

        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // do nothing on close
        setFocusableWindowState(false); // do not allow focus
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            setLocationRelativeTo(getParent());
        }
        super.setVisible(visible);
    }

    /**
     * Dialog want to know when the parent is resized or moved
     * which requires a ComponentListener, but I don't need all methods
     * So I use ComponentAdapter
     * @param parent
     */
    private void addParentResizeListener(JFrame parent) {
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizeAndPosition();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
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
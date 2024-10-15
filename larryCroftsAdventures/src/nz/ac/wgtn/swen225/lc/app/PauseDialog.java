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
import javax.swing.SwingUtilities;

/**
 * A dialog that displays a message when the game is paused.
 * It is semi-transparent and blocks the game window (renderer).
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class PauseDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel renderer;
    private JPanel content;
    private JLabel label;
    private static final int fontRatio = 8;
    

    /**
     * Create a new PauseDialog with the given parent, renderer, text, background color, and text color.
     * @param parent
     * @param renderer
     * @param text
     * @param backgroundColor
     * @param textColor
     */
    public PauseDialog(JFrame parent, JPanel renderer, String text, Color backgroundColor, Color textColor) {
        // JDialog takes 3 parameters, JFrame as parent, String as title and boolean as modal(it will block other windows)
        super(parent, "Game Paused", false);
        this.renderer = renderer;
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
        content = new JPanel(new GridBagLayout());
        content.setOpaque(false); // make it transparent

        label = new JLabel(ComponentFactory.format(text, false), JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setForeground(textColor);
        content.add(label);
        setContentPane(content);// takeover the dialog
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // do nothing on close
        setFocusableWindowState(false); // do not allow focus on the dialog
    }

    /**
     * Show the dialog and center it relative to the parent.
     * @param visible
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updateSizeAndPosition();
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
        SwingUtilities.invokeLater(() -> {
            setLocation(renderer.getLocationOnScreen().x, renderer.getLocationOnScreen().y);
            setSize(renderer.getWidth(), renderer.getHeight());
            updateFontSize();
            revalidate();
            repaint();
        });
    }

    /**
     * Update the font size based on the size of the dialog
     */
    private void updateFontSize() {
        int size = Math.min(getWidth(), getHeight()) / fontRatio;
        Font newFont = label.getFont().deriveFont((float)size);
        label.setFont(newFont);
    }
}
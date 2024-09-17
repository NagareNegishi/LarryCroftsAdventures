package nz.ac.wgtn.swen225.lc.app;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

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

    public PauseDialog(JFrame parent) {
        super(parent, "Game Paused", false);
        initializeUI();
    }

    private void initializeUI() {
        getContentPane().setBackground(new Color(150, 0, 0));
        JPanel panel = new JPanel(new BorderLayout());
        //panel.setBackground(new Color(240, 240, 255));
        panel.setOpaque(false);

        JLabel label = new JLabel("Game is paused", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(new Color(150, 150, 0));
        panel.add(label, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        setSize(200, 100);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setFocusableWindowState(false);




        /**setLayout(new BorderLayout());
        add(new JLabel("Game is paused", JLabel.CENTER));
        setSize(200, 100);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setFocusableWindowState(false);*/
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setLocationRelativeTo(getParent());
        }
        super.setVisible(visible);
    }
}
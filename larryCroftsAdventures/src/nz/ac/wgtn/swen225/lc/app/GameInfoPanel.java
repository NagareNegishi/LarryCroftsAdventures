package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameInfoPanel extends JPanel {
    private JLabel timeTextLabel;
    private JLabel timeLabel;
    private JLabel levelLabel;
    private JLabel keysLabel;
    private JLabel treasuresLabel;
    
    public GameInfoPanel() {
        setLayout(new BorderLayout(0,10));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        timeTextLabel = new JLabel("Time:");
        timeTextLabel.setFont(getFont().deriveFont(20.0f));
        timeLabel = new JLabel("60");
        timeLabel.setFont(getFont().deriveFont(40.0f));
        timerPanel.add(timeTextLabel);
        timerPanel.add(timeLabel);
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        levelLabel = new JLabel("Level: 1");
        keysLabel = new JLabel("Keys: 0");
        treasuresLabel = new JLabel("Treasures left: 10");

        infoPanel.add(levelLabel);
        infoPanel.add(keysLabel);
        infoPanel.add(treasuresLabel);
        add(timerPanel,BorderLayout.NORTH);
        add(infoPanel,BorderLayout.CENTER);

    }

    // I could have just one method, but its more readable to have separate methods
    public void setTime(int time) {
        timeLabel.setText(String.valueOf(time));
        updateTimerColor(time);
    }
    public void setLevel(int level) {
        levelLabel.setText("Level: " + level);
    }
    public void setKeys(int keys) {
        keysLabel.setText("Keys: " + keys);
    }
    public void setTreasures(int treasures) {
        treasuresLabel.setText("Treasures left: " + treasures);
    }
    public void updateTimerColor(int time) {
        if (time < 10) {
            timeLabel.setForeground(Color.RED);
        } else if (time < 20) {
            timeLabel.setForeground(Color.ORANGE);
        }
        else {
            timeLabel.setForeground(Color.BLACK);
        }
    }



}

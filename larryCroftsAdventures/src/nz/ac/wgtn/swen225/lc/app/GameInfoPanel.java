package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
    
    public GameInfoPanel(int width, int height) {
        setLayout(new BorderLayout(0,10));
        setPreferredSize(new Dimension(width, height)); //repetitive?? app will call it later too

        //*need better layout */
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        timerPanel.setPreferredSize(new Dimension(width, height/2));
        timeTextLabel = new JLabel("Time:    ");
        timeTextLabel.setFont(getFont().deriveFont((float)width/4));
        timeLabel = new JLabel("60");
        timeLabel.setFont(getFont().deriveFont((float)width/2));
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

    /**
     * 
     * @param time
     */
    public void updateTimerColor(int time) {
        Color color = switch(time/10) {// I can't believe new switch can't limit the scope of the variable
            case 0  -> Color.RED;
            case 1 ->Color.ORANGE;
            default -> Color.BLACK;
        };
        timeLabel.setForeground(color);
    }



}

package nz.ac.wgtn.swen225.lc.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Factory class for creating Swing components with common/ custom settings.
 * It has 5 static methods to create JButton, JToggleButton, JSlider, JLabel and JFileChooser.
 * It also has methods to format the text.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class ComponentFactory {

    private static Color BUTTON_BACKGROUND = new Color(100, 100, 100);
    private static Color BUTTON_FOREGROUND = Color.YELLOW;
    private static Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final String prefix = "<html><div style='text-align: center;'>";
    private static final String suffix = "</div></html>";

    /**
     * Create a JButton with the given text, action command and listener.
     * @param text
     * @param actionCommand
     * @param listener
     * @return JButton
     */
    public static JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        //styleButton(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    /**
     * Create a JToggleButton with the given text, action command and listener.
     * @param text
     * @param actionCommand
     * @param listener
     * @return JToggleButton
     */
    public static JToggleButton createToggleButton(String text, String actionCommand, ActionListener listener) {
        JToggleButton button = new JToggleButton(text);
        //styleButton(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    /**
     * Create a JSlider with the given min, max, value and sliderConsumer.
     *
     * note:
     * unlike buttons, slider requires a ChangeListener instead of ActionListener,
     * which has one method stateChanged(ChangeEvent e).
     * the method is called whenever the slider's value changes,
     * but the source of the event is the slider itself.
     * this method is called whenever the slider's value changes
     * and triggers the sliderConsumer with the new value in App
     *
     * @param min minimum value of the slider
     * @param max maximum value of the slider
     * @param value initial value of the slider
     * @param sliderConsumer consumer to be called when the slider value changes
     * @return JSlider
     */
    public static JSlider createSlider(int min, int max, int value, Consumer<Integer> sliderConsumer) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.setFocusable(false);
        
        //this can be lambda expression, but prioritizing readability
        //as lambda expression it will be: slider.addChangeListener(e -> sliderConsumer.accept(slider.getValue()));
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderConsumer.accept(slider.getValue());
            }
        };
        slider.addChangeListener(changeListener);
        return slider;
    }

    /**
     * Custom JPanel with title and value pair.
     */
    static class infoPanel extends JPanel {
        private JLabel titleLabel;
        private JLabel valueLabel;

        /**
         * constructor to create a panel with title and value pair
         * @param title
         */
        public infoPanel(String title) {
            setLayout(new GridBagLayout());
            titleLabel = new JLabel(format(title,true), JLabel.CENTER);
            valueLabel = new JLabel("", JLabel.CENTER);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 0;
            gbc.weightx = 1;

            gbc.gridy = 0;
            gbc.weighty = 0.1;
            add(titleLabel, gbc);
            gbc.gridy = 1;
            gbc.weighty = 0.9;
            add(valueLabel, gbc);
        }

        /**
         * set the value of the label
         * @param value
         */
        public void setValue(String value) {
            valueLabel.setText(value);
        }

        /**
         * update the font size of the labels
         * @param size
         */
        public void updateFont(float title, float value) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(title));
            valueLabel.setFont(valueLabel.getFont().deriveFont(value));
        }
    }

    /**
     * Create a custom JPanel with title and value pair.
     * @param title
     * @return
     */
    public static infoPanel createInfoPanel(String title) {
        return new infoPanel(title);
    }

    /**
     * Create custom file chooser with given directory, title and description
     * User can not change the directory and can only select json files
     * @param dir
     * @param title
     * @param description
     * @return JFileChooser
     */
    public static JFileChooser customFileChooser(File dir, String title, String description) {
    JFileChooser fileChooser = new JFileChooser(dir) {
        /**
         * Overriding setCurrentDirectory to prevent user from changing directory
         */
        @Override
        public void setCurrentDirectory(File directory) {
            super.setCurrentDirectory(dir);
        }
    };
    fileChooser.setDialogTitle(title);
    FileNameExtensionFilter filter = new FileNameExtensionFilter(description, "json");
    fileChooser.setFileFilter(filter);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    return fileChooser;
    }

    /**
     * Format the message to center align.
     * @param message
     * @return
     */
    public static String format(String message, boolean underline) {
        if(underline) {
            message = underline(message);
        }
        return prefix + message + suffix;
    }
    public static String underline(String message) {
        return "<u>" + message + "</u>";
    }

    /*
     * Note:
     * Methods below are not used in the current version of the game,
     * but can be used to style the buttons.
     */
    /**
     * Style the button with custom settings.
     * @param button
     */
    private static void styleButton(AbstractButton button) {
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(BUTTON_FOREGROUND);
        button.setFont(BUTTON_FONT);
        //button.setFocusPainted(false);
        //button.setBorderPainted(false);
        //button.setOpaque(true);
        button.setFocusable(false);
    }

    /**
     * Set the background color of the buttons.
     * @param color
     */
    public static void setButtonBackground(Color color) {
        BUTTON_BACKGROUND = color;
    }

    /**
     * Set the foreground color of the buttons.
     * @param color
     */
    public static void setButtonForeground(Color color) {
        BUTTON_FOREGROUND = color;
    }

    /**
     * Set the font of the buttons.
     * @param font
     */
    public static void setButtonFont(Font font) {
        BUTTON_FONT = font;
    }
}

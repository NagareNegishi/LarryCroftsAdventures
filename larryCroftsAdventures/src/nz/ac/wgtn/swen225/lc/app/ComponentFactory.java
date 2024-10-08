package nz.ac.wgtn.swen225.lc.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Factory class for creating Swing components with common settings.
 * It has 3 static methods to create JButton, JToggleButton, and JSlider.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class ComponentFactory {

    private static Color BUTTON_BACKGROUND = new Color(0, 0, 100);
    private static Color BUTTON_FOREGROUND = Color.WHITE;
    private static Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    /**
     * Create a JButton with the given text, action command and listener.
     * @param text
     * @param actionCommand
     * @param listener
     * @return JButton
     */
    public static JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        styleButton(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        //button.setFocusable(false); // remove focus from buttons
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
        styleButton(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        //button.setFocusable(false); // remove focus from buttons
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

    private static void styleButton(AbstractButton button) {
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(BUTTON_FOREGROUND);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(new RoundedBorder(10));
        button.setFocusable(false);
    }

    public static void setButtonBackground(Color color) {
        BUTTON_BACKGROUND = color;
    }

    public static void setButtonForeground(Color color) {
        BUTTON_FOREGROUND = color;
    }

    public static void setButtonFont(Font font) {
        BUTTON_FONT = font;
    }




    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BUTTON_BACKGROUND);
            g2.fillRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
    }









}

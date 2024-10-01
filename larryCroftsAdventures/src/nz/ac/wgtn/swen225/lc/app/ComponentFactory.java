package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Factory class for creating Swing components with common settings.
 */
public class ComponentFactory {

    /**
     * Create a JButton with the given text, action command and listener.
     * @param text
     * @param actionCommand
     * @param listener
     * @return JButton
     */
    public static JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
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
        /**
         * this can be lambda expression, but lets prioritize readability for now
         */
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderConsumer.accept(slider.getValue());
            }
        };
        slider.addChangeListener(changeListener);
        return slider;
    }
}

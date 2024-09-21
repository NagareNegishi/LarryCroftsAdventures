package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ComponentFactory {

    public static JButton createButton(String text, String actionCommand, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    public static JToggleButton createToggleButton(String text, String actionCommand, ActionListener listener) {
        JToggleButton button = new JToggleButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.setFocusable(false); // remove focus from buttons
        return button;
    }

    /**
     * unlike buttons, slider requires a ChangeListener instead of ActionListener
     * which has one method stateChanged(ChangeEvent e)
     * the method is called whenever the slider's value changes
     * but the source of the event is the slider itself
     * this method is called whenever the slider's value changes
     * and triggers the sliderConsumer with the new value in App
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

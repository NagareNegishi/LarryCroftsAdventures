package nz.ac.wgtn.swen225.lc.app;

import java.awt.BorderLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

/**
 * SidePanel class manages the display of MenuPanel and RecorderPanel.
 * To avoid having too many buttons on the screen at once, only one panel is displayed at a time.
 *
 * Note: After many refactor to make the code in App short,
 * This class became a little bit hard to understand at first glance.
 * So, here is a brief explanation of the class.
 *
 *  Action Listeners:
 *    - Jbutton need action listener
 *    - ActionListener is a functional interface with: actionPerformed(ActionEvent e).
 *    - When a button is clicked, this method is called with an ActionEvent.
 *
 *  Action Commands:
 *    - Each button can be assigned an "action command", String that identifies the action.
 *    - in MenuPanel/RecorderPanel , I set action command for each button/ action event
 *      so I can use getActionCommand() to get the action command.
 *
 *  In App class, I have a methods handle****Action:
 *    - Take String for the action command
 *    - Trigger the appropriate action based on the command.
 *
 *     menuPanel = new MenuPanel(e -> handleMenuAction(e.getActionCommand()));
 *     recorderPanel = new RecorderPanel(e -> handleRecorderAction(e.getActionCommand())
 *     ,slider -> handleSliderChange(slider));
 *
 *  Using Consumers:
 *    - However, this class can't call methods in App directly.
 *    - So, I'm using Consumer to pass the action command to App.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class SidePanel extends JPanel {
    private MenuPanel menuPanel;
    private RecorderPanel recorderPanel;
    private enum Mode {MENU, RECORDER}
    private Mode current = Mode.MENU;
    
    /**
     * Create a new SidePanel with the given width, height, menu, recorder, and slider.
     * @param width
     * @param height
     * @param menu Consumer for menu action command
     * @param recorder Consumer for recorder action command
     * @param slider Consumer for slider value
     */
    public SidePanel(int width, int height, Consumer<String> menu, Consumer<String> recorder, Consumer<Integer> slider) {
        setLayout(new BorderLayout());
        menuPanel = new MenuPanel(e -> menu.accept(e.getActionCommand()));
        recorderPanel = new RecorderPanel(e -> recorder.accept(e.getActionCommand()),
        value -> slider.accept(value)); //this is equivalent to slider::accept, but it is more readable.
        add(menuPanel, BorderLayout.CENTER);
    }

    /**
     * Toggle between menu and recorder panel.
     */
    public void togglePanel() {
        removeAll();
        current = switch(current) {
            case MENU -> {
                add(recorderPanel);
                yield Mode.RECORDER;
            }
            case RECORDER -> {
                add(menuPanel);
                yield Mode.MENU;
            }
        };
        revalidate();
        repaint();
    }

    /**
     * Set the text and action command of the pause button.
     * @param text
     */
    public void setPauseButtonText(String text) {
        menuPanel.setPauseButton(text);
    }

    public boolean isRecorder() {
        return current == Mode.RECORDER;
    }
}

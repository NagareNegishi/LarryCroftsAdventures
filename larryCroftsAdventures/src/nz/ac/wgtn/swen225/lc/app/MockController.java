package nz.ac.wgtn.swen225.lc.app;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.GameStateController;

/**
 * Mock Controller for fuzz testing.
 * Initially created by Fuzz developer, and I asked him to move it to App package.
 * As App shouldn't allow other class to modify the controller,
 * but Fuzz need to test the game logic.
 *
 * @author Nagare Negishi
 * @studentID 300653779
 */
public class MockController extends Controller{
	public GameStateController stateController;

	/**
	 * Constructor for MockController class
	 * @param stateController contains the methods defined in GameStateController class
	 */
	public MockController(GameStateController stateController){
		super(stateController, createMockActionBindings());
		this.stateController = stateController;
	}

	/**
	 * Mocking the action bindings
	 * while showcasing the Null Object Pattern
	 *
	 * @return a map of action bindings
	 */
	private static Map<String, Runnable> createMockActionBindings() {
		Map<String, Runnable> mockBindings = new HashMap<>();
        mockBindings.put("exitWithoutSaving", () -> {});
        mockBindings.put("exitAndSave", () -> {});
        mockBindings.put("resumeSavedGame", () -> {});
        mockBindings.put("startNewGame1", () -> {});
        mockBindings.put("startNewGame2", () -> {});
        mockBindings.put("pause", () -> {});
        mockBindings.put("unpause", () -> {});
        return mockBindings;
	}
}

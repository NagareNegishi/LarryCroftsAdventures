package nz.ac.wgtn.swen225.lc.app;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.GameStateController;

/**
 * Mock Controller for fuzz testing.
 * Initially created by Fuzz developer, and I asked him to move it to App package.
 * As App shouldn't allow other class to modify the controller,
 * but Fuzz need to test the game logic.
 */
public class MockController extends Controller{

	/*
	## this need to go as we only use GameStateController to instantiate the controller

	public Chap chap;
	public Maze maze;
	public MockController(Chap chap, Maze maze) {
		super(chap,maze,createMockActionBindings());
		this.chap = chap;
		this.maze = maze;
	}
	public Chap getChap() {
		return chap;
	}
	public Maze getMaze() {
		return maze;
	}
	*/



	public GameStateController update;
	public MockController(GameStateController stateController){
		super(stateController, createMockActionBindings());
		update = stateController;
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

package nz.ac.wgtn.swen225.lc.app;

import java.util.HashMap;
import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;

public class MockController extends Controller{
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

	/**
	 * mocking the action bindings
	 * with Null Object Pattern
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

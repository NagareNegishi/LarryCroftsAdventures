package nz.ac.wgtn.swen225.lc.app;

import java.util.Map;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;

public class MockController extends Controller{
	public Chap chap;
	public Maze maze;
	public Map<String, Runnable> actionBindings;
	public MockController(Chap chap, Maze maze, Map<String, Runnable> actionBindings) {
		super(chap,maze,actionBindings);
		this.chap = chap;
		this.maze = maze;
	}
	public Chap getChap() {
		return chap;
	}
	public Maze getMaze() {
		return maze;
	}

}

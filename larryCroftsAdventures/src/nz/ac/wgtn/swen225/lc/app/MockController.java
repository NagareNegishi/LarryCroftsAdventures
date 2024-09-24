package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;

public class MockController extends Controller{
	public Chap chap;
	public Maze maze;
	public MockController(Chap chap, Maze maze) {
		super(chap,maze,null);
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

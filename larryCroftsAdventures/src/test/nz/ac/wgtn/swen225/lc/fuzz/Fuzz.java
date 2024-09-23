package test.nz.ac.wgtn.swen225.lc.fuzz;

import java.util.Random;

import javax.swing.SwingUtilities;

import org.junit.Test;

import nz.ac.wgtn.swen225.lc.app.MockController;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Maze;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Fuzz {
	private static Random random;
	//number of times to run the test
	private static int iterations = 100;
	public Fuzz() {
		random = new Random();
	}
	
	public static void main(String[] args) {
		Fuzz fuzz = new Fuzz();
		fuzz.Test1();
	}
	@Test public void Test1() {
		//randomly moves chap in a direction.
		MockController mockController = new MockController(new Chap(2,2), Maze.createBasicMaze(5,5));
		for (int i = 0; i < Fuzz.iterations; i++){
			int next = random.nextInt(3);
			if (next == 0) {
				mockController.chap.move(Chap.Direction.Up, mockController.maze);
				System.out.println("moved Chap up! Current Pos: " + mockController.chap.getPosition());
			}
			if (next == 1) {
				mockController.chap.move(Chap.Direction.Down, mockController.maze);
				System.out.println("moved Chap down! Current Pos: " + mockController.chap.getPosition());
			}
			if (next == 2) {
				mockController.chap.move(Chap.Direction.Left, mockController.maze);
				System.out.println("moved Chap left! Current Pos: " + mockController.chap.getPosition());
			}
			if (next == 3) {
				mockController.chap.move(Chap.Direction.Right, mockController.maze);
				System.out.println("moved Chap right! Current Pos: " + mockController.chap.getPosition());
			}
		}
		
		
	}
	
}

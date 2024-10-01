package nz.ac.wgtn.swen225.lc.app;

import javax.swing.SwingUtilities;

/**
 * Main class to start the game
 * 
 * This class is the entry point for the game. It creates a new instance of the App class.
 * 
 * @see App
 */
public class Main {
	/**
	 * Main method to start the game
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(App::new);
	}
}
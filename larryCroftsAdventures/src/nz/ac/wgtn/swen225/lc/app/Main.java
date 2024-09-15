package nz.ac.wgtn.swen225.lc.app;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		System.out.println("Java Version: " + System.getProperty("java.version"));
		SwingUtilities.invokeLater(App::new);
	}
}

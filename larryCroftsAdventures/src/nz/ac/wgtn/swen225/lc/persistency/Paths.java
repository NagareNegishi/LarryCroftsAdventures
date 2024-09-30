package nz.ac.wgtn.swen225.lc.persistency;

import java.io.File;

/**
 * Contains all the relevant File paths for persistency
 */
public class Paths {
	
	public static final File root = new File(System.getProperty("user.dir"));
	
	public static final File levelsDir = new File(root, "levels");
	public static final File savesDir = new File(root, "saves");
	
	public static final String levelPath = "levels/";
	public static final String savePath = "saves/";
}
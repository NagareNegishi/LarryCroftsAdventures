package nz.ac.wgtn.swen225.lc.persistency;

import java.io.File;

/**
 * Contains all the relevant File paths for persistency
 * @author titheradam	300652933
 */
public class Paths {
	
	public static final File root = new File(System.getProperty("user.dir"));
	
	public static final File levelsDir = new File(root, "levels");
	public static final File savesDir = new File(root, "saves");
	
	public static final File level1 = new File(levelsDir, "level1.json");
	public static final File level2 = new File(levelsDir, "level2.json");

	// Path for ctr-s game exit
	public static final File saveAndQuit = new File(levelsDir, "saveAndQuit.json");
	
	public static final String levelPath = "levels/";
	public static final String savePath = "saves/";
}
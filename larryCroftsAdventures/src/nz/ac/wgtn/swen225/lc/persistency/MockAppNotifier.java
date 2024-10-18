package nz.ac.wgtn.swen225.lc.persistency;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;

/**
 * Mock class for AppNotifier
 * Used to test SerialRunnable
 * @author titheradam	300652933
 */
public class MockAppNotifier implements AppNotifier, Serializable{

	@JsonProperty
	public static List<String> log = new ArrayList<String>();
	
	@JsonProperty
	private SerialRunnable win = () -> log.add("win");
	
	public void run() {
		win.run();
	}
	
	@JsonCreator
	public MockAppNotifier() {}
	
	
	@Override
	public void onGameWin() {}

	@Override
	public void onGameLose() {}

	
	@Override
	public void onKeyPickup(String keyName) {}


	@Override
	public void onTreasurePickup(int treasureCount) {}

}

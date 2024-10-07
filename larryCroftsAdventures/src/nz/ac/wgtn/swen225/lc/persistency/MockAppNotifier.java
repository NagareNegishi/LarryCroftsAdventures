package nz.ac.wgtn.swen225.lc.persistency;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;
import nz.ac.wgtn.swen225.lc.domain.Chap;
import nz.ac.wgtn.swen225.lc.domain.Item;

public class MockAppNotifier implements AppNotifier, Serializable{

	@JsonProperty
	public static List<String> log = new ArrayList<String>();
	
	@JsonProperty
	private SerialRunnable win = () -> log.add("win");
//	@JsonProperty
//	private SerialRunnable win = () -> System.out.println("win");
	
	public void run() {
		win.run();
	}
	
	@JsonCreator
	public MockAppNotifier() {}
	
	//public MockAppNotifier() {}
	
	@Override
	public void onGameWin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameLose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyPickup(int keyCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTreasurePickup(int treasureCount) {
		// TODO Auto-generated method stub
		
	}

}

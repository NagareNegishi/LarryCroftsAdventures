package nz.ac.wgtn.swen225.lc.persistency;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import nz.ac.wgtn.swen225.lc.app.AppNotifier;

public class MockAppNotifier implements AppNotifier{

	@JsonProperty
	private SerialRunnable win = () -> System.out.println("You win!");
	
	
	@JsonCreator
	public MockAppNotifier() {}
	
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

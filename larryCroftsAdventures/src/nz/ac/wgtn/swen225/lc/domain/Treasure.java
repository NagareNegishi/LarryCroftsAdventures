package nz.ac.wgtn.swen225.lc.domain;

public class Treasure implements Item{
	
	boolean collected = false;
	
	public Treasure() {this.collected = false;} 
	
	public boolean collected() {return collected;}
	
	public void collect() {this.collected = true;}

	@Override
	public String description() {
		return "Treasure, been collected = " + collected;
	}

}

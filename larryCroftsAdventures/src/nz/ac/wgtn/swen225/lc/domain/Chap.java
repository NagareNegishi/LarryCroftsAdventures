package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chap {
	
	private int row;
	private int col;
	@JsonProperty
	private List<Item> inventory;
	
	public Chap(int startRow, int startCol) {
		if(startRow < 0 || startCol < 0) {throw new IllegalArgumentException("Chap must be in bounds of the maze");}
		this.row = startRow;
		this.col = startCol;
		this.inventory = new ArrayList<>();
		assert this.row == startRow && this.col == startCol;
		assert this.inventory.size() == 0;
		}
	
	/**
	 * Constructor specifically for Jackson reconstruction
	 * @param startRow
	 * @param startCol
	 * @param inventory
	 */
	@JsonCreator
	public Chap(@JsonProperty("startRow") int startRow,
				@JsonProperty("startCol") int startCol,
				@JsonProperty("inventory") ArrayList<Item> inventory) {
		if(startRow < 0 || startCol < 0) {throw new IllegalArgumentException("Chap must be in bounds of the maze");}
		this.row = startRow;
		this.col = startCol;
		this.inventory = inventory;
		assert this.row == startRow && this.col == startCol;
		assert this.inventory.size() == 0;
	}
	
	public int getRow() {return row;}
	public int getCol() {return col;}
	
	// debugging method for position of Chap
	public String getPosition() {return "Chap is at row: " + row + ", column: " + col;}
	
	// helper method to move chap anywhere in the maze, good for debugging
	public void moveTo(int newRow, int newCol, Maze maze) {
		if(!maze.validMove(newRow, newCol)) {throw new IllegalArgumentException("Invalid move");}
		this.row = newRow;
		this.col = newCol;
		assert this.row == newRow && this.col == newCol : "Move failed";
	}
	
	public void move(Direction direction, Maze maze) {
		if(maze.equals(null) || direction.equals(null)) {throw new IllegalArgumentException("Maze or Direction is null cannot move Chap");}
	   
		int newRow = this.row;
	    int newCol = this.col;
	    
	    switch (direction) {
	        case Up:
	            newRow--;
	            break;
	        case Down:
	            newRow++;
	            break;
	        case Left:
	            newCol--;
	            break;
	        case Right:
	            newCol++;
	            break;
	    }
	    
	    if (!maze.validMove(newRow, newCol)) {
	        throw new IllegalArgumentException(
	        		"Invalid move: Cannot move " + direction.name() + ".");
	    }
	  
	    this.row = newRow;
	    this.col = newCol;
	    
	    assert this.row == newRow && this.col == newCol : "Move failed";
	}

	// enum to store the directions Chap can move as well as functionality to see what direction Chap will move next
	public enum Direction { 
		Up(-1,0), 
		Down(1,0), 
		Left(0,-1), 
		Right(0,1);
		
		private int rowDir;
		private int colDir;
		
		Direction(int row, int col){
			this.rowDir = row;
			this.colDir = col;
		}
		public int rowDirection() {return rowDir;}
		public int colDirection() {return colDir;}
	}
	
	public void pickUpItem(Item item) {
		if(item.equals(null)) {throw new IllegalArgumentException("Cannot pick up item because it is null");}
		//System.out.println("I picked up: " + item.description() + ";)");
		if(item instanceof Key) {
		inventory.add(item);
		assert inventory.contains(item);
		}
	}	
	
	public List<Item> inventory(){return inventory;}
	
	// helpful method for laying out chaps inventory
	public void inventoryDescription() {
		System.out.println("Chap's inventory contains: "); 
		inventory.forEach(s -> System.out.println(s.description()));
		}
	
	
}

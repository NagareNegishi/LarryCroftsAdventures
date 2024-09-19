package nz.ac.wgtn.swen225.lc.domain;

import java.util.ArrayList;
import java.util.List;

public class Chap {
	
	private int row;
	private int col;
	private List<Item> inventory;
	
	public Chap(int startRow, int startCol) {
		this.row = startRow;
		this.col = startCol;
		this.inventory = new ArrayList<>();
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
	        throw new IllegalArgumentException("Invalid move: Cannot move " + direction.name() + ".");
	    }
	  
	    this.row = newRow;
	    this.col = newCol;
	    assert this.row == newRow && this.col == newCol : "Move failed";
	}

	public enum Direction { Up, Down, Left, Right;}
	
	// picks up an item and if it is a treasure marks it as collected
	public void pickUpItem(Item item) {
		inventory.add(item);
		// may need to move this to another class dealing with game state
		if(item instanceof Treasure) {((Treasure) item).collect();}	
	}
	
	public List<Item> inventory(){return inventory;}
	
	// helpful method for laying out chaps inventory
	public void inventoryDescription() {
		System.out.println("Chap's inventory contains: "); 
		inventory.forEach(s -> System.out.println(s.description()));
		}
}

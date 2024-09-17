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
	public void move(int newRow, int newCol, Maze maze) {
		if(!maze.validMove(newRow, newCol)) {throw new IllegalArgumentException("Invalid move");}
		this.row = newRow;
		this.col = newCol;
		assert this.row == newRow && this.col == newCol : "Move failed";
	}
	
	// movement methods for each direction, could change later to not include a maze parameter for ease of use
	public void moveUp(Maze maze) {
		int newRow = row - 1;
		if (!maze.validMove(newRow, col)) {
			throw new IllegalArgumentException("Invalid move: Cannot move up.");
		}
		this.row = newRow;
		assert this.row == newRow: "Move up failed.";
		}
	
	public void moveDown(Maze maze) {
		int newRow = row + 1;
        if (!maze.validMove(newRow, col)) {
            throw new IllegalArgumentException("Invalid move: Cannot move down.");
        }
        this.row = newRow;
        assert this.row == newRow: "Move down failed.";
	}
	
	public void moveLeft(Maze maze) {
		int newCol = col - 1;
        if (!maze.validMove(row, newCol)) {
            throw new IllegalArgumentException("Invalid move: Cannot move left.");
        }
        this.col = newCol;
        assert this.col == newCol : "Move left failed.";
	}

	public void moveRight(Maze maze) {
		int newCol = col + 1;
        if (!maze.validMove(row, newCol)) {
            throw new IllegalArgumentException("Invalid move: Cannot move right.");
        }
        this.col = newCol;
        assert this.col == newCol : "Move right failed.";
	}

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
	
	// can add methods to directly check tile next to chap in each direction
}

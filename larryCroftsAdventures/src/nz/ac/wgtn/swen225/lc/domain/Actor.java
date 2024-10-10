package nz.ac.wgtn.swen225.lc.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Actor class representing a moving entity for Level 2 of the game, an Actor moves in a square pattern and
 * if an Actor comes into contact with Chap, the game is over and the level will restart.
 * 
 * @author fergusbenj1 300656321
 * 
 */


public class Actor {

	@JsonProperty
	private int row;
	@JsonProperty
	private int col;
	private Direction currentDirection;
	private int steps;
	
	@JsonCreator
	public Actor(@JsonProperty("row") int startRow,
				@JsonProperty("col") int startCol) {
		if(startRow < 0 || startCol < 0) {throw new IllegalArgumentException("Actor must be in bounds of the maze");}
		
		this.row = startRow;
		this.col = startCol;
		this.currentDirection = Direction.Right;
		
		assert this.row == startRow && this.col == startCol;
	}
	
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
	
	public int getRow() {return row;}
	public int getCol() {return col;}
	
	public void move(Maze maze) {
		int newRow = row + currentDirection.rowDirection();
        int newCol = col + currentDirection.colDirection();
		
        if (maze.getTile(newRow, newCol).canMoveTo()) {
            row = newRow;
            col = newCol;
            steps++;
        }
        
        if (steps >= 2) {
            changeDirection();
            steps = 0; 
        }
	}

	public  void changeDirection() {
		 switch (currentDirection) {
         case Right:
             currentDirection = Direction.Down;
             break;
         case Down:
             currentDirection = Direction.Left;
             break;
         case Left:
             currentDirection = Direction.Up;
             break;
         case Up:
             currentDirection = Direction.Right;
             break;
			 } 
	}
}

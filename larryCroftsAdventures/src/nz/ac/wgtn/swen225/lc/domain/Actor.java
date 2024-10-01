package nz.ac.wgtn.swen225.lc.domain;

// Actor for level 2 that should move in a pattern (in a square) 
public class Actor {

	private int row;
	private int col;
	private Direction currentDirection;
	
	// field to store number of step in a direction actor has taken
	private int steps;
	
	public Actor(int startRow, int startCol) {
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
	
	public String getPosition() {return "Actor is at row: " + row + ", column: " + col;}
		
	// movement method for actor (make sure cant move to wall etc.)	
	public void move(Maze maze) {
		int newRow = row + currentDirection.rowDirection();
        int newCol = col + currentDirection.colDirection();
		
        if (maze.getTile(newRow, newCol).canMoveTo()) {
            row = newRow;
            col = newCol;
            steps++;
        }
        // making max steps 2 in this case for now
        if (steps >= 2) {
            changeDirection(); 
            steps = 0; 
        }
	}
	
	// method to change actors direction in pattern if it hits 3 steps
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
	
	// method for if Actor touches Chap (may just need to be moved to the move method, also add to Chap)
	public void onTouch(Chap chap) {
		if(this.row == chap.getRow() && this.col == chap.getCol()) {
			// action for when Chap and Actor are touching (freezing Chap? end level?)
			
		}
	}
}

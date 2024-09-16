package nz.ac.wgtn.swen225.lc.app;

public enum Direction {
    NONE(0, 0),
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    Direction up(){ return UP; }
    Direction right(){ return RIGHT; }
    Direction down(){ return DOWN; }
    Direction left(){ return LEFT; }
    Direction none(){ return NONE; }
    
}

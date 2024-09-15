package nz.ac.wgtn.swen225.lc.app;

public enum Direction {
    NONE(0, 0),
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() { return dx; }
    public int getDy() { return dy; }
    Direction up(){ return UP; }
    Direction right(){ return RIGHT; }
    Direction down(){ return DOWN; }
    Direction left(){ return LEFT; }
    Direction none(){ return NONE; }
    
}

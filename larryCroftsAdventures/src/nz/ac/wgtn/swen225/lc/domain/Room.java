package nz.ac.wgtn.swen225.lc.domain;

import java.util.List;

public record Room(int row, int col, int width, int height, List<Door> doors) {
	
}

record Door(int row, int col) {}


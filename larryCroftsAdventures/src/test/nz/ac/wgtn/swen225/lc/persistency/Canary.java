package test.nz.ac.wgtn.swen225.lc.persistency;


/**
 * Test record to test Jackson 
 */
public record Canary(String name, int weight, Seeds seeds) {}

record Seeds(int num) {}
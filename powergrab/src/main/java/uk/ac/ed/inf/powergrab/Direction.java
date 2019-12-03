package uk.ac.ed.inf.powergrab;

import java.util.*;

public enum Direction { // We enumerate all directions, so they can be used later with these acronyms.
	
	N, NNE, NE,	ENE, E,	ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW;
	
    public static HashMap<Integer, Direction> directions = new HashMap<>();
    
    // Creates he HashMap of directions
	public static void dirByIndex(){
		HashMap<Integer, Direction> dirs = new HashMap<>();
		dirs.put(0, N);
		dirs.put(1, NNE);
		dirs.put(2, NE);
		dirs.put(3, ENE);
		dirs.put(4, E);
		dirs.put(5, ESE);
		dirs.put(6, SE);
		dirs.put(7, SSE);
		dirs.put(8, S);
		dirs.put(9, SSW);
		dirs.put(10, SW);
		dirs.put(11, WSW);
		dirs.put(12, W);
		dirs.put(13, WNW);
		dirs.put(14, NW);
		dirs.put(15, NNW);
		directions = dirs;
	}
	
}

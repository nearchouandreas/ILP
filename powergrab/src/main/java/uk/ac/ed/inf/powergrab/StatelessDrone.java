package uk.ac.ed.inf.powergrab;

import java.util.*;

public class StatelessDrone extends Drone{
	

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	//Function that calculates the path
	protected List<Position> calculateMoves(Map map) {
		
		List<Position> path = new ArrayList<>();
		//this.stations = stations;
		this.map = map;
        // separate stations into good and bad and store them in two separate lists
        //map.separateStations(map.stations);
		
		int noOfMoves = 0;
		path.add(this.getPosition());
		//Game ends if number of moves is 250 of drone runs out of power
		while(noOfMoves < 250 && this.getPower() >= 1.25) {
			
		    // Search if there is a station in immediate scope (any of the 16 directions). If there is return it, else return null
		    ChargingStation stationInScope = bestStationInScope();
			
			// if there is no station in range, find a random direction to move to
			if (stationInScope == null) {
			    
			    findRandomDirection();

			}
			
			//System.out.println("->drone power before: " + this.getPower());
			this.move(dir);
			path.add(this.getPosition());
			System.out.println(dir);
			//System.out.println("drone power after: " + this.getPower());
			
			noOfMoves++;
			//System.out.println(noOfMoves);
			ChargingStation stationInRange = findClosestStation(map.stations, this.getPosition());
			if (stationInRange != null) {
				System.out.println(stationInRange.getCoins() + "before");
				this.updateCharge(stationInRange);
				System.out.println(stationInRange.getCoins() + "----");
				System.out.println();
					
			}
			
			// Create the current line of the text file.
			detailedMoves += String.format("%s,%s,%s,%s,%s,%f,%f\n", path.get(noOfMoves-1).latitude, path.get(noOfMoves-1).longitude, dir, path.get(noOfMoves).latitude, path.get(noOfMoves).longitude, this.getCoins(), this.getPower());
//        		coinsHistory.add(this.getCoins());
//        		powerHistory.add(this.getPower());
//        		directionHistory.add(dir);
			
		}
		System.out.println("moves: " + noOfMoves);
		System.out.println("coins: " + this.getCoins());
		System.out.println("power: " + this.getPower());
		return path;
		
	}

}

package uk.ac.ed.inf.powergrab;

import java.util.*;

public class StatelessDrone extends Drone{
	

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	//Function that calculates he path
	public List<Position> calculateMoves( List<ChargingStation> stations) {
		
		List<Position> path = new ArrayList<>();
		
		this.stations = stations;
        // separate stations into good and bad and store them in two separate lists
        separateStations(stations);
		
		int noOfMoves = 0;
		path.add(this.getPosition());
		//Game ends if number of moves is 250 of drone runs out of power
		while(noOfMoves < 250 && this.getPower() >= 1.25) {
			
			
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
			ChargingStation stationInRange = findClosestStation(stations, this.getPosition());
			if (stationInRange != null) {
				System.out.println(stationInRange.getCoins() + "before");
				this.updateCharge(stationInRange);
				System.out.println(stationInRange.getCoins() + "----");
				System.out.println();
				
				
			}
			
		}
		System.out.println("moves: " + noOfMoves);
		System.out.println("coins: " + this.getCoins());
		System.out.println("power: " + this.getPower());
		return path;
		
	}

}

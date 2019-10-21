package uk.ac.ed.inf.powergrab;

import java.util.*;

public class StatelessDrone extends Drone{
	

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	public ChargingStation findClosestStation(List<ChargingStation> stations, Position position) {
		
		ChargingStation result = null;
		double minDistance = 1;
		for (ChargingStation station: stations) {
			double currentDistance = station.distance(position);
			if (currentDistance <= 0.00025) { 
				if (currentDistance < minDistance) {
					minDistance = currentDistance;
					result = station;
				}
			}
		}
		return result;
	}
	
//	public ChargingStation findBestStation(List<ChargingStation> stations, Position position) {
//		
//		ChargingStation result = null;
//		double maxPower = 0;
//		for (ChargingStation station: stations) {
//			double currentPower = station.getPower();
//			if (station.inRange(position)) { 
//				if (currentPower > maxPower) {
//					maxPower = currentPower;
//					result = station;
//				}
//			}
//		}
//		return result;
//	}
	
	public List<Position> calculateMoves( List<ChargingStation> stations) {
		
		List<ChargingStation> badStations = new ArrayList<>();
		List<ChargingStation> goodStations = new ArrayList<>();
		List<Position> path = new ArrayList<>();
		for(ChargingStation station : stations) {
			if (station.isSafe()) {
				goodStations.add(station);
			}
			else{
				badStations.add(station);
			}
		}
		
		int noOfMoves = 0;
		while(noOfMoves < 250 && this.getPower() > 0) {
			path.add(this.getPosition());
			
			Direction dir = Direction.N;// = Direction.dirByIndex().get(0);
			Position nextPos = this.getPosition().nextPosition(dir);// = this.getPosition().nextPosition(dir);
			
			for (int i = 0; i < 16; i++) {
				dir = Direction.dirByIndex().get(i);
				
				nextPos = this.getPosition().nextPosition(dir);
				if (goodStations.contains(findClosestStation(stations, nextPos)) && nextPos.inPlayArea()) {
					
					break;				
				}
			}
			
			if (findClosestStation(goodStations, nextPos) == null) {
				int dirIndex = getRnd().nextInt(16);
				dir = Direction.dirByIndex().get(dirIndex);
				nextPos = this.getPosition().nextPosition(dir);
				while (findClosestStation(badStations, nextPos) != null || !nextPos.inPlayArea()) {
					//System.out.print("hello");
					//System.out.print("Hello1");
					dirIndex = getRnd().nextInt(16);
					dir = Direction.dirByIndex().get(dirIndex);
					nextPos = this.getPosition().nextPosition(dir);
				}
//				while (!nextPos.inPlayArea()) {
//					dirIndex = getRnd().nextInt(16);
//					dir = Direction.dirByIndex().get(dirIndex);
//					nextPos = this.getPosition().nextPosition(dir);
//				}
			}
			
			//System.out.println("->drone power before: " + this.getPower());
			this.move(dir);
			
			//System.out.println(dir);
			//System.out.println("drone power after: " + this.getPower());
			
			noOfMoves++;
			//System.out.println(noOfMoves);
			if (findClosestStation(stations, this.getPosition()) != null) {
				ChargingStation stationInRange = findClosestStation(stations, this.getPosition());
				//System.out.println(stationInRange.getPower());
				this.updateCharge(stationInRange);
				//System.out.println(this.getPower());
				//System.out.println();
				goodStations.remove(stationInRange);//findClosestStation(goodStations, nextPos));
				
				
			}
//			if (inRangeOfStations(goodStations, this.getPosition()) != null) {
//				ChargingStation stationInRange = inRangeOfStations(goodStations, this.getPosition());
//				this.updateCharge(stationInRange.getCoins(), stationInRange.getPower());
//			}
			
		}
//		System.out.println("moves: " + noOfMoves);
		//System.out.println("coins: " + this.getCoins());
		//System.out.println("power: " + this.getPower());
		return path;
		
	}

//	private List<Position> extractBadStations(HashMap<Position, ChargingStation> stations) {
//		
//		List<Position> badStations = new ArrayList<>();
//		for (ChargingStation station: )
//		return null;
//		
//	}

}

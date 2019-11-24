package uk.ac.ed.inf.powergrab;

import java.util.*;

public class StatefulDrone extends Drone{
	// Create graph? -> How? Maybe less efficiently if this is going to help later
	// Need to traverse the graph using a heuristic of some sort

	public StatefulDrone(Position initPosition, int seed) {
		
		super(initPosition, seed);
		
	}
	
	public ChargingStation closestGoodStation(List<ChargingStation> stations, Position position) {
		
		double minDist = Double.MAX_VALUE;
		ChargingStation closestGoodStation = null;
		
		for (ChargingStation station: stations) {
			
			double dist = station.distance(position);
			if ((dist < minDist)) {
				minDist = dist;
				closestGoodStation = station;
			}	
		}
		 return closestGoodStation;
	}

	List<Position> calculateMoves(List<ChargingStation> stations) {
		
		// HashMap<ChargingStation, ChargingStation> closestStation = new HashMap<ChargingStation, ChargingStation>();
		
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
		
		
		//boolean foundGoodStation = false;
		Direction dirToAvoid = null;
		ChargingStation goalStation = null;
		int noOfMoves = 0;
		path.add(this.getPosition());
		while (noOfMoves < 250 && this.getPower() > 1.25) {
			
			Position position = this.getPosition();
			//path.add(position);
			
			//!!!!!!
			Direction dir = Direction.N;
			Position nextPos1 = this.getPosition().nextPosition(dir);// = this.getPosition().nextPosition(dir);
			
			//List<ChargingStation> closeStations;
			double bestCoins = Double.MIN_VALUE;
			ChargingStation bestStation  = null;
			
			for (int i = 0; i < 16; i++) {
				Direction currentDir1 = Direction.dirByIndex().get(i);
				Position posToMove1 = this.getPosition().nextPosition(currentDir1);
				
				ChargingStation station = findClosestStation(stations, posToMove1);
				if (station != null && posToMove1.inPlayArea()) {
					if (station.getCoins() > bestCoins) {
						dir = currentDir1;
						bestCoins = station.getCoins();
						nextPos1 = posToMove1;
						bestStation = station;
					}
				}
			//!!!
			}
			position = nextPos1;
			//path.add(position);
			//Position newPos = position.nextPosition(dir);
			
			if (bestStation == null) {
				goalStation = closestGoodStation(goodStations, position);
				if (goalStation != null) {
					
					System.out.println("Station closest: " + goalStation.getCoins());
					double minDistance = Double.MAX_VALUE;
					double currentDistance = goalStation.distance(position);
					//while(findClosestStation(stations, newPos) != closestGoodStation) {
							
						
					
					//double currentDistance = closestGoodStation.distance(position);
					int j = 0;
					for (int i = 0; i < 16; i++) {
						Direction currentDir = Direction.dirByIndex().get(i);
						Position posToMove = this.getPosition().nextPosition(currentDir);
						
						if (posToMove.inPlayArea() &&  !badStations.contains(findClosestStation(stations, posToMove))) {
							if (goalStation.distance(posToMove) < minDistance) {
	//							if (currentDir != dirToAvoid) { // if the direction we are looking at is nod the one we came already, then we can count it towards the result.
	//								j = i;
	//								dir = currentDir;
	//								minDistance = goalStation.distance(posToMove);
	//								position = posToMove;
	//							}
	//							else {
	//								dirToAvoid = null; //Reset the direction we need to avoid for the next step.
	//							}
								if (!path.contains(posToMove)) { 
									// if the direction we are looking at is nod the one we came already, then we can count it towards the result.
									j = i;
									dir = currentDir;
									minDistance = goalStation.distance(posToMove);
									position = posToMove;
								}
								
							}
						}
					}
					
					//path.add(position);
					// If the drone has moved further away from the goal station, then we mark the direction we came from
					if (minDistance > currentDistance) {
						int index = (j+8)%16;
						dirToAvoid = Direction.dirByIndex().get(index);
					}
					//}
				}
			
				else {
					
					
					int dirIndex = getRnd().nextInt(16);
					dir = Direction.dirByIndex().get(dirIndex);
					Position nextPos = this.getPosition().nextPosition(dir);
					while (badStations.contains(findClosestStation(stations, nextPos))|| !nextPos.inPlayArea()) {
						dirIndex = getRnd().nextInt(16);
						dir = Direction.dirByIndex().get(dirIndex);
						nextPos = this.getPosition().nextPosition(dir);
					}
					System.out.println("Wandering Around!!!");
					position = nextPos;
				}
			}
			
			
			path.add(position);
			//System.out.println(dir);
			this.move(dir);
			noOfMoves++;
			
			//System.out.println(dir);
			
			
			
			System.out.println(noOfMoves);
			ChargingStation stationInRange = findClosestStation(stations, this.getPosition());
			if (stationInRange != null) {
				System.out.println("Station charging: " + stationInRange.getCoins());
				this.updateCharge(stationInRange);
				System.out.println(stationInRange.getCoins() + "----");
				System.out.println("drone power after: " + this.getPower());
				System.out.println(stationInRange.distance(position));
				System.out.println();
				if (true) {//stationInRange == goalStation) {
					goodStations.remove(stationInRange);
				}
				//System.out.println("goodStations: " + goodStations.size());
				
			}
			
		}
		System.out.println("Final power: " + this.getPower());
		System.out.println("Final coins: " + this.getCoins());
		return path;
	}
	
	

}

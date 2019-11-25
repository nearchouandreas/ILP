package uk.ac.ed.inf.powergrab;

import java.util.*;

public class StatefulDrone extends Drone{
// NOTE: I changed how direction works. I have set dir as a parameter in the Drone class and it is updated there. 
// Should we change the strategy?
// Should we change the Lists? Maybe sets?    
    
	public StatefulDrone(Position initPosition, int seed) {
		super(initPosition, seed);
	}
	
	// Function that finds the next closest good station that is going to be used as a goal station
	public ChargingStation nextGoodStation(List<ChargingStation> stations, Position position) {
		
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
	
	// Finds the best direction to move towards the goal station(finds which direction brings you closer safely)
	public void bestDirectionToGoal(List<Position> path, ChargingStation goalStation) {
	    
	    double minDistance = Double.MAX_VALUE;
        Direction direction = Direction.N;    
        
        // for every possible direction
        for (int i = 0; i < 16; i++) {
            Direction dirToMove = Direction.dirByIndex().get(i);
            Position posToMove = this.getPosition().nextPosition(dirToMove);
            
            // if the considered position is in the play area and it is not in range of a bad station
            if (posToMove.inPlayArea() &&  !badStations.contains(findClosestStation(stations, posToMove))) {
                
                // Store the minimum distance to the goal over all directions
                if (goalStation.distance(posToMove) < minDistance) {
                    
                    // if the direction we are looking at is not the one we came already, then we can count it towards the result.
                    if (!path.contains(posToMove)) {
                        direction = dirToMove;
                        minDistance = goalStation.distance(posToMove);
                    }   
                }
            }
        }
        dir =  direction;
	}
	

	// calculates the path that the drone follows
	List<Position> calculateMoves(List<ChargingStation> stations) {
		
		
		//List<ChargingStation> badStations = new ArrayList<>();
		//List<ChargingStation> goodStations = new ArrayList<>();
		List<Position> path = new ArrayList<>();
		
		this.stations = stations;
		// separate stations into good and bad and store them in two separate lists
		separateStations(stations);
		
		ChargingStation goalStation = null;
		int noOfMoves = 0;
		path.add(this.getPosition());
		
		// The game stops only when number of moves are 250, or when power is 0. 
		while (noOfMoves < 250 && this.getPower() >= 1.25) {
			
		    // position is the current position of the drone
			Position position = this.getPosition();
			
			// Search if there is a station in immediate scope (any of the 16 directions). If there is return it, else return null
			ChargingStation stationInScope = bestStationInScope();
			
			// if no station is found in any of the 16 directions
			if (stationInScope == null) {
			    
			    // if there are good stations left, 
                if (!goodStations.isEmpty()) {
                    
                    // find the good station that is closest to the current position
                    goalStation = nextGoodStation(goodStations, position);
				
				
					System.out.println("Station closest: " + goalStation.getCoins());
					//find the direction that brings you closer and safe to that goal station
					bestDirectionToGoal(path, goalStation);
				}
				// else just wander safely around in a random direction
				else {
					findRandomDirection();
					System.out.println("Wandering Around!!!");
				}
			}
			
			
			
			//System.out.println(dir);
			
			// Move towards the best direction, add the next position to the path and increment number of moves
			this.move(dir);
			path.add(this.getPosition());
			noOfMoves++;
			
			//System.out.println(dir);
			System.out.println(noOfMoves);
			
			//Check if the drone happens to be in the range of any station
			ChargingStation stationInRange = findClosestStation(stations, this.getPosition());
			
			// if the drone is indeed in the range of a station
			if (stationInRange != null) {
				System.out.println("Station charging: " + stationInRange.getCoins());
				//charge from that station, whatever it's symbol is
				this.updateCharge(stationInRange);
				System.out.println(stationInRange.getCoins() + "----");
				System.out.println("drone power after: " + this.getPower());
				System.out.println(stationInRange.distance(position));
				System.out.println();
				// if the drone charges from a good station then remove it from the list of good stations
				goodStations.remove(stationInRange);
				
			}
			
		}
		System.out.println("Final power: " + this.getPower());
		System.out.println("Final coins: " + this.getCoins());
		return path;
	}
	
	

}

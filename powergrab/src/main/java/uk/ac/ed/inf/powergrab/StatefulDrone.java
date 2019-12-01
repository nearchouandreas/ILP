package uk.ac.ed.inf.powergrab;

import java.util.*;

public class StatefulDrone extends Drone{
// NOTE: I changed how direction works. I have set dir as a parameter in the Drone class and it is updated there. 
// NOTE2: i added total coins of good stations to the drone.
// Should we change the strategy?
// Should we change the Lists? Maybe sets? 
// I don't move towards the closest direction. Just move towards one direction that gets u inside the range
// NOTE3: I added the avoidAsGoal list to change the goal if it gets stuck. !!!
// NOTE4: I commented out the part where it can't go in a 0 station, so it considers 0 stations as directions.
// NOTE5: Remove the part that neglects the random movement.
    
    private List <ChargingStation> avoidAsGoal;
    
	public StatefulDrone(Position initPosition, int seed) {
		super(initPosition, seed);
		this.avoidAsGoal = new ArrayList<>();
	}
	
	// Function that finds the next closest good station that is going to be used as a goal station
	private ChargingStation nextGoodStation(List<ChargingStation> stations, Position position) {
		
		double minDist = Double.MAX_VALUE;
		ChargingStation closestGoodStation = null;
		
		for (ChargingStation station: stations) {
			
			double dist = station.distance(position);
			if ((dist < minDist)) {
			    
			    if (!this.avoidAsGoal.contains(station)) {
			        minDist = dist;
	                closestGoodStation = station;
			    }
				
			}	
		}
		return closestGoodStation;
	}
	
	// Finds the best direction to move towards the goal station(finds which direction brings you closer safely)
	private void bestDirectionToGoal(List<Position> path, ChargingStation goalStation) {
	    
	    double minDistance = Double.MAX_VALUE;
        Direction direction = null;    
      
        // for every possible direction
        for (int i = 0; i < 16; i++) {
            Direction dirToMove = Direction.directions.get(i);//.dirByIndex().get(i);
            Position posToMove = this.getPosition().nextPosition(dirToMove);
            
            // if the considered position is in the play area and it is not in range of a bad station
            if (posToMove.inPlayArea() &&  !map.badStations.contains(findClosestStation(map.stations, posToMove))) {
                
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
        // if the drone can only move away from the drone then move in a random direction
        if (minDistance > goalStation.distance(this.getPosition())) {
            this.avoidAsGoal.add(goalStation);
        }
        // If none of the 16 directions is suitable to move, then move to a safe random one
        // This allows the drone to go to directions that will take it back to positions it visited before.
        if (direction == null) {
            findRandomDirection();
        }
        else {
            dir =  direction;
        }
	}
	
	

	// calculates the path that the drone follows
	protected List<Position> calculateMoves(Map map) {
		
		List<Position> path = new ArrayList<>();
		
		//this.stations = stations;
		this.map = map;
		// separate stations into good and bad and store them in two separate lists
		//map.separateStations(map.stations);
		
		ChargingStation goalStation = null;
		int noOfMoves = 0;
		
		// Add the start position to the path
		path.add(this.getPosition());
		
		// The game stops only when number of moves are 250, or when power is 0. 
		while (noOfMoves < 250 && this.getPower() >= 1.25) {
			
		    // position is the current position of the drone
			Position position = this.getPosition();
			
			// Search if there is a station in immediate scope (any of the 16 directions). If there is return it, else return null
			ChargingStation stationInScope  = bestStationInScope();
			
			// if no station is found in any of the 16 directions
			if (stationInScope == null) {
			    
			    // find the good station that is closest to the current position
                goalStation = nextGoodStation(map.goodStations, position);
                
			    // if you can find a good station to use as a goal station, 
                if (goalStation != null) {
                    
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
			
			
			// Move towards the chosen direction, add the next position to the path and increment number of moves
			this.move(dir);
			path.add(this.getPosition());
			noOfMoves++;
			
			//System.out.println(dir);
			System.out.println(noOfMoves);
			
			
			ChargingStation stationInRange = null;
			// if the drone has moved towards a station in any of the 16 directions charge from that station
			if (stationInScope != null) {
	            stationInRange = stationInScope;
			}
			else {
			    //Check if the drone happens to be in the range of any station
			    stationInRange = findClosestStation(map.stations, this.getPosition());
			}
			
			// if the drone is indeed in the range of a station
			if (stationInRange != null) {
				System.out.println("Station charging: " + stationInRange.getCoins());
			
				//charge from that station, whatever its symbol is
				this.updateCharge(stationInRange);
				if (stationInRange == goalStation) {//goodStations.contains(stationInRange)) {
				    avoidAsGoal.clear();
				}
				System.out.println(stationInRange.getCoins() + "----");
				System.out.println("drone coins after: " + this.getCoins());
				System.out.println(stationInRange.distance(position));
				System.out.println();
				// if the drone charges from a good station then remove it from the list of good stations
				map.goodStations.remove(stationInRange);
				
			}
			
			// There might be times that the drone charges from stations it found in it's scope and thus the avoidAsGoal list might not be cleared;
			// thus if it ever reaches the size of the goodStations list, clear it.
			if (map.goodStations.size() <= avoidAsGoal.size())  {
			    avoidAsGoal.clear();
			}
			
			// Create the current line of the text file
			detailedMoves += String.format("%s,%s,%s,%s,%s,%f,%f\n", path.get(noOfMoves-1).latitude, path.get(noOfMoves-1).longitude, dir, path.get(noOfMoves).latitude, path.get(noOfMoves).longitude, this.getCoins(), this.getPower());
//			coinsHistory.add(this.getCoins());
//	        powerHistory.add(this.getPower());
//	        directionHistory.add(dir);
			
		}
		System.out.println("Final power: " + this.getPower());
		System.out.println("Final coins: " + this.getCoins());
		System.out.println("Total coins: " + map.sumOfGood);
		return path;
	}
	
	

}

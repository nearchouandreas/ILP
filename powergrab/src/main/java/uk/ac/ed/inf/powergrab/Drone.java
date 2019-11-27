package uk.ac.ed.inf.powergrab;

import java.util.*;

public abstract class Drone {
	
	private double power;
	private double coins;
	private Position position;
	private Random rnd;
	
	public List<ChargingStation> stations = new ArrayList<>();
    public List<ChargingStation> badStations = new ArrayList<>();
    public List<ChargingStation> goodStations = new ArrayList<>();
    public Direction dir;
	
	public Drone(Position initPosition, int seed) { // double initPower ,double initCoins,
		
		this.power = 250.0;
		this.coins = 0.0;
		this.position = initPosition;
		rnd = new Random(seed);
		
	}
	
	public void move(Direction direction) {
		 
		Position newPos = this.position.nextPosition(direction);
		this.position = newPos;
		this.power -= 1.25;
		
		
	}
	
	
	// Returns the closest station in range, if there is one, or null otherwise.
	public ChargingStation findClosestStation(List<ChargingStation> stations, Position position) {
		
		ChargingStation result = null;
		double minDistance = 1;
		for (ChargingStation station: stations) {
			double currentDistance = station.distance(position); // distance from position to the station being checked
			if (currentDistance <= 0.00025) { 
				if (currentDistance < minDistance) { // if the current distance is less than the minimum one then store it
					minDistance = currentDistance;
					result = station;
				}
			}
		}
		return result;
	}
	
	// Updates the charge of a station and the drone, when the drone is within range of that station
	public void updateCharge(ChargingStation station ) { //double powerUpdate, double coinsUpdate) {
		
		double updateCoins = 0;
		double updatePower = 0;
		
		// if station is dangerous
		if (!station.isSafe()) {
		    
		    // if subtracting the station's coins from the drone gives a negative result, the drone's coins become 0 
			if((this.coins + station.getCoins()) < 0) {
				this.coins = 0;
				updateCoins = this.coins + station.getCoins();
			}
			// else, the drone's coins are the result and the station's coins are 0
			else {
				updateCoins = 0;
				this.coins = this.coins + station.getCoins();
			}
			// if subtracting the station's power from the drone gives a negative result, the drone's power becomes 0 
			if((this.power + station.getPower()) < 0) {
				this.power = 0;
				updatePower = this.power + station.getPower();
			}
			// else, the drone's power is the result and the station's power is 0
			else {
				updatePower = 0;
				this.power = this.power + station.getPower();
			}
					
		}
		// if station is not dangerous, then just do the addition.
		else {
			this.coins = this.coins + station.getCoins();
			updateCoins = 0;
			updatePower = 0;
			this.power = this.power + station.getPower();
		}
		
		station.updateValues(updateCoins, updatePower);
	}
	
	// Separate good and bad stations
    public void separateStations(List<ChargingStation> stations) {
        
        for(ChargingStation station : stations) {
            if (station.isSafe()) {
                goodStations.add(station);
            }
            else{
                badStations.add(station);
            }
        }
        
    }
    
    // Generates a random direction
    public void findRandomDirection() {
        
        int dirIndex = getRnd().nextInt(16);
        Direction direction = Direction.dirByIndex().get(dirIndex);
        Position nextPos = this.getPosition().nextPosition(direction);
        while (badStations.contains(findClosestStation(stations, nextPos))|| !nextPos.inPlayArea()) {
            dirIndex = getRnd().nextInt(16);
            direction = Direction.dirByIndex().get(dirIndex);
            nextPos = this.getPosition().nextPosition(direction);
        }
        dir =  direction;
    }
	
    //Finds the best station that is in the immediate scope of the drone(can be accessed in one move)
    public ChargingStation bestStationInScope() {

        //initialise variables
        double bestCoins = Double.MIN_VALUE;
        ChargingStation bestStation  = null;
        Direction direction = Direction.N;
        // For every direction possible, doing a clockwise rotation
        for (int i = 0; i < 16; i++) {
            
            // Find the possible direction and next Position
            Direction dirToMove = Direction.dirByIndex().get(i);
            Position posToMove = this.getPosition().nextPosition(dirToMove);
            
            // Check if there is a station in range of the position considered
            ChargingStation stationInRange = findClosestStation(stations, posToMove);
            
            // If there is a station in range and the next position is in play area,
            if (stationInRange != null && posToMove.inPlayArea()) {
                //and if the station's coins are better than any station's coins we have seen so far in range then store it
                if (stationInRange.getCoins() > bestCoins) {
                    direction = dirToMove;
                    bestCoins = stationInRange.getCoins();
                    //nextPos = posToMove;
                    bestStation = stationInRange;
                }
            }
        }
        dir = direction;
        return bestStation;
    }
    
	// abstract method, producing the path, that is implemented separately in the two children classes
	abstract List<Position> calculateMoves(List<ChargingStation> stations);
	
	public double getPower() {
		return power;
	}

	public double getCoins() {
		return coins;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Random getRnd() {
		return rnd;
	}
	

}

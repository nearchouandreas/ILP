package uk.ac.ed.inf.powergrab;

import java.util.*;

public abstract class Drone {
	
	private float power;
	private float coins;
	private Position position;
	
	protected String detailedMoves = "";
	protected Map map;
	private Random rnd;
    protected Direction dir;
	
    //public double sumOfGood;
    
	public Drone(Position initPosition, int seed) { // double initPower ,double initCoins,
		
		this.power = 250.0f;
		this.coins = 0.0f;
		this.position = initPosition;
		rnd = new Random(seed);
		Direction.dirByIndex();

	}
	
	protected void move(Direction direction) {
		 
		Position newPos = this.position.nextPosition(direction);
		this.position = newPos;
		this.power -= 1.25;
		
	}
	
	
	// Returns the closest station in range, if there is one, or null otherwise.
	protected ChargingStation findClosestStation(List<ChargingStation> stations, Position position) {
		
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
	protected void updateCharge(ChargingStation station ) { //double powerUpdate, double coinsUpdate) {
		
		float updateCoins = 0;
		float updatePower = 0;
		
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
	
	
    
    // Generates a random direction
    protected void findRandomDirection() {
        
        int dirIndex = this.rnd.nextInt(16);
        Direction direction = Direction.directions.get(dirIndex);//.dirByIndex().get(dirIndex);
        Position nextPos = this.getPosition().nextPosition(direction);
        while (map.badStations.contains(findClosestStation(map.stations, nextPos)) || !nextPos.inPlayArea()) {
            dirIndex = this.rnd.nextInt(16);
            direction = Direction.directions.get(dirIndex);//.dirByIndex().get(dirIndex);
            nextPos = this.getPosition().nextPosition(direction);
        }
        System.out.println("Wandering Around!!!");
        dir =  direction;
    }
	
    //Finds the best station that is in the immediate scope of the drone(can be accessed in one move)
    protected ChargingStation bestStationInScope() {

        //initialise variables
        // Store two sets of variables, one set is in case every station in immediate scope is bad
        double bestCoins = Double.MIN_VALUE;
        double bestNegativeCoins = -Double.MAX_VALUE;
        
        ChargingStation bestStation  = null;
        ChargingStation bestNegativeStation = null;
        
        Direction direction = null;
        Direction dirBestNegative = null;
        
        boolean allBad = true;
        
        // For every direction possible, doing a clockwise rotation
        for (int i = 0; i < 16; i++) {
            
            
            // Find the possible direction and next Position
            Direction dirToMove = Direction.directions.get(i);//dirByIndex().get(i);
            Position posToMove = this.getPosition().nextPosition(dirToMove);
            
            
            
            // If there is a station in range and the next position is in play area,
            if (posToMove.inPlayArea()) {
                
             // Check if there is a station in range of the position considered
                ChargingStation stationInRange = findClosestStation(map.stations, posToMove);
                
                boolean isCurrentBad = false;
                
                if (stationInRange != null) {
                    // if station is dangerous
                    if (!stationInRange.isSafe()) {
                        isCurrentBad = true;
                        if (stationInRange.getCoins() > bestNegativeCoins) {
                            dirBestNegative = dirToMove;
                            bestNegativeCoins = stationInRange.getCoins();
                            bestNegativeStation = stationInRange;
                        } 
                    }
                    // else, if the station is safe
                    else {
                        if (stationInRange.getCoins() > bestCoins) {
                            direction = dirToMove;
                            bestCoins = stationInRange.getCoins();
                            bestStation = stationInRange;
                        }
                        
                    }
                }
                allBad = allBad && isCurrentBad;
            }
            
        }
        // if there are 16 bad stations, go towards the best one.
        if (allBad) {
            dir = dirBestNegative;
            return bestNegativeStation;
        }
        else {
            dir = direction;
            return bestStation;
        }
        
    }
    
	// abstract method, producing the path, that is implemented separately in the two children classes
	protected abstract List<Position> calculateMoves(Map map);
	
	public float getPower() {
		return power;
	}

	public float getCoins() {
		return coins;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Random getRnd() {
		return rnd;
	}
	

}

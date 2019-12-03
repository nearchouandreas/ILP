package uk.ac.ed.inf.powergrab;

public class ChargingStation {
	
	private String id;
	private float coins;
	private float power;
	private boolean safe;
	private Position coordinates;
	
	
	// Constructor; initialises the variables
	public ChargingStation (String id, float coins, float power, String markerSymbol, Position coordinates ) {
		
		this.id = id;
		this.coins = coins;
		this.power = power;
		this.safe = markerSymbol.equals("lighthouse");
		this.coordinates = coordinates;
		
	}
	
	// Calculates distance of a point to the charging station
	public double distance(Position position) {
		
		double x1 = position.longitude;
		double x2  = this.coordinates.longitude;
		double y1 = position.latitude;
		double y2  = this.coordinates.latitude;
		
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); // <= 0.0025;
		
	}
	
	// Updates the values of station's power and coins to the ones given
	public void updateValues(float coins, float power) {
		
		this.coins = coins;
		this.power = power;
		
	}
	
	public String getId() {
		return id;
	}

	public float getCoins() {
		return coins;
	}

	public float getPower() {
		return power;
	}

	public boolean isSafe() {
		return safe;
	}

	public Position getPosition() {
		return coordinates;
	}

}

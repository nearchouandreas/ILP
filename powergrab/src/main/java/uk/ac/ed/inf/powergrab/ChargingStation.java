package uk.ac.ed.inf.powergrab;

public class ChargingStation {
	
	private String id;
	private float coins;
	private float power;
	private boolean safe;
	private Position coordinates;
	
	public ChargingStation (String id, float coins, float power, String markerSymbol, Position coordinates ) {
		
		this.id = id;
		this.coins = coins;
		this.power = power;
		this.safe = markerSymbol.equals("lighthouse");
		this.coordinates = coordinates;
		
	}
	
	public double distance(Position position) {
		
		double x1 = position.longitude;
		double x2  = this.coordinates.longitude;
		double y1 = position.latitude;
		double y2  = this.coordinates.latitude;
		
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); // <= 0.0025;
		
	}
	
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

	public Position getStationCoordinates() {
		return coordinates;
	}

}

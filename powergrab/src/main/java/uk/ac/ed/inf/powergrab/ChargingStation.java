package uk.ac.ed.inf.powergrab;
import java.util.*;

public class ChargingStation {
	
	private String id;
	private double coins;
	private double power;
	private boolean safe;
	private Position coordinates;
	
	public ChargingStation (String id, double coins, double power, String markerSymbol, Position coordinates ) {
		
		this.id = id;
		this.coins = coins;
		this.power = power;
		this.safe = markerSymbol.equals("lighthouse");
		this.coordinates = coordinates;
		
	}
	
	public double distance(Position position) {
		
		double x1 = position.longitude;
		double x2  = this.getCoordinates().longitude;
		double y1 = position.latitude;
		double y2  = this.getCoordinates().latitude;
		
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); // <= 0.0025;
		
	}
	
	public void updateValues(double coins, double power) {
		
		this.coins = coins;
		this.power = power;
		
	}
	
	public String getId() {
		return id;
	}

	public double getCoins() {
		return coins;
	}

	public double getPower() {
		return power;
	}

	public boolean isSafe() {
		return safe;
	}

	public Position getCoordinates() {
		return coordinates;
	}

}

package uk.ac.ed.inf.powergrab;

import java.util.*;

public abstract class Drone {
	
	private double power;
	private double coins;
	private Position position;
	private Random rnd;
	
	public Drone(Position initPosition, int seed) { // double initPower ,double initCoins,
		
		this.power = 250.0;
		this.coins = 0.0;
		this.position = initPosition;
		rnd = new Random(seed);
		
	}
	
	public void move(Direction direction) {
		 
		Position newPos = this.position.nextPosition(direction);
		this.position = newPos;
		//this.updateCharge(-1.25, 0);
		this.power -= 1.25;
		
		
	}
	
	public void updateCharge(ChargingStation station ) { //double powerUpdate, double coinsUpdate) {
		
		double updateCoins = 0;
		double updatePower = 0;
		
		if (!station.isSafe()) {
			if((this.coins + station.getCoins()) < 0) {
				this.coins = 0;
				updateCoins = this.coins + station.getCoins();
			}
			else {
				updateCoins = 0;
				this.coins = this.coins + station.getCoins();
			}
			if((this.power + station.getPower()) < 0) {
				this.power = 0;
				updatePower = this.power + station.getPower();
			}
			else {
				updatePower = 0;
				this.power = this.power + station.getPower();
			}
					
		}
		else {
			this.coins = this.coins + station.getCoins();
			updateCoins = 0;
			updatePower = 0;
			this.power = this.power + station.getPower();
		}
		station.updateValues(updateCoins, updatePower);
		
	}
	
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

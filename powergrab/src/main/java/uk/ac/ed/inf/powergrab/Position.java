package uk.ac.ed.inf.powergrab;

public class Position {

	public double latitude;
	public double longitude;
	final double r = 0.0003; // The drone always travels this distance r
	
	// The distance travelled in each direction is calculated above for efficiency.
	final double sinDist67_5 = r*Math.sin(Math.toRadians(67.5));
	final double cosDist67_5 = r*Math.cos(Math.toRadians(67.5));
	final double sinDist45 = r*Math.sin(Math.toRadians(45));
	final double cosDist45 = r*Math.cos(Math.toRadians(45));
	final double sinDist22_5 = r*Math.sin(Math.toRadians(22.5));
	final double cosDist22_5 = r*Math.cos(Math.toRadians(22.5));
	
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position nextPosition(Direction direction) {
		
		Position newPos;
		
		// If the drone moves North, latitude will increase, whilst if it moves South latitude will decrease.
		// If the drone moves East, longitude will increase, whilst if it moves West longitude will decrease.
		// So we combine them to find how latitude or longitude will change in any of the other directions and
		// we use trigonometry to find how much will the latitude and longitude increase or decrease.
		// The angle reflects the angle from the North-South axis.
		switch (direction) {
		
			case N:
				newPos = new Position(this.latitude + r, this.longitude);
				break;
			
			case NNE:
				newPos = new Position(this.latitude + sinDist67_5, this.longitude + cosDist67_5);
				break;
			
			case NE:
				newPos = new Position(this.latitude + sinDist45, this.longitude + cosDist45);
				break;
				
			case ENE:
				newPos = new Position(this.latitude + sinDist22_5, this.longitude + cosDist22_5);
				break;
				
			case E:
				newPos = new Position(this.latitude, this.longitude + r);
				break;
				
			case ESE:
				newPos = new Position(this.latitude - sinDist22_5, this.longitude + cosDist22_5);
				break;
				
			case SE:
				newPos = new Position(this.latitude - sinDist45, this.longitude + cosDist45);
				break;
				
			case SSE:
				newPos = new Position(this.latitude - sinDist67_5, this.longitude + cosDist67_5);
				break;
				
			case S:
				newPos = new Position(this.latitude - r, this.longitude);
				break;
				
			case SSW:
				newPos = new Position(this.latitude - sinDist67_5, this.longitude - cosDist67_5);
				break;
				
			case SW:
				newPos = new Position(this.latitude - sinDist45, this.longitude - cosDist45);
				break;
				
			case WSW:
				newPos = new Position(this.latitude - sinDist22_5, this.longitude - cosDist22_5);
				break;
				
			case W:
				newPos = new Position(this.latitude, this.longitude - r);
				break;
				
			case WNW:
				newPos = new Position(this.latitude + sinDist22_5, this.longitude - cosDist22_5);
				break;
				
			case NW:
				newPos = new Position(this.latitude + sinDist45, this.longitude - cosDist45);
				break;
				
			case NNW:
				newPos = new Position(this.latitude + sinDist67_5, this.longitude - cosDist67_5);
				break;
			
			default:
				newPos = new Position(this.latitude, this.longitude); // If none of these directions are entered, stay at current position.
		
				
		}
		return newPos;	
		
	}
	
	// Return true if drone is inside the permitted play area, false if it's not.
	public boolean inPlayArea() {
		
		return (this.latitude > 55.942617 && this.latitude < 55.946233) && (this.longitude > -3.192473 && this.longitude < -3.184319);
		
	}
	
	public String toString() {
		return "[" + Double.toString(this.longitude) + "," + this.latitude + "]\n";
	}
	
	public boolean equals(Object o) {
		Position position = (Position) o;
		
		double uncertainty = Math.pow(10, -10);
		return (Math.abs(this.latitude - position.latitude) < uncertainty) && (Math.abs(this.longitude - position.longitude) < uncertainty);
		
	}
}

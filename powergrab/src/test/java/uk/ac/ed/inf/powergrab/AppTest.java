package uk.ac.ed.inf.powergrab;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Unit test for PowerGrab App.  [JUnit 4 version]
 */
public class AppTest {

    @Test
	public void testPositionConstructor() {
		assertTrue( new Position(55.944425, -3.188396) != null );
	}

	final Position p0 = new Position(55.944425, -3.188396);

    @Test
	public void testPositionLatitude() {
		assertTrue(p0.latitude == 55.944425);
	}

    @Test
	public void testPositionLongitude() {
		assertTrue(p0.longitude == -3.188396);
	}
	
    @Test
	public void testCentreInPlayArea() {
		assertTrue(p0.inPlayArea());
	}
	
    @Test
	public void testBoundaryOutOfPlayArea() {
		Position pos = new Position(55.946233, -3.192473);
		assertFalse(pos.inPlayArea());
	}
	
    @Test
	public void testPositionOutOfPlayArea() {
		Position pos = new Position(55.942000, -3.192000);
		assertFalse(pos.inPlayArea());
	}
	
    
	boolean approxEq(double d0, double d1) {
		final double epsilon = 1.0E-12d;
		return Math.abs(d0 - d1) < epsilon;
	}
	
	boolean approxEq(Position p0, Position p1) {
		return approxEq(p0.latitude, p1.latitude) && approxEq(p0.longitude, p1.longitude); 
	}
	
    @Test
	public void testNextPositionNotIdentity() {
		Position p1 = p0.nextPosition(Direction.N);
		assertFalse(approxEq(p0, p1));
	}
	
    @Test
	public void testNorthThenSouth() {
		Position p1 = p0.nextPosition(Direction.N);
		Position p2 = p1.nextPosition(Direction.S);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testEastThenWest() {
		Position p1 = p0.nextPosition(Direction.E);
		Position p2 = p1.nextPosition(Direction.W);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testNorthSouthEastWest() {
		Position p1 = p0.nextPosition(Direction.N);
		Position p2 = p1.nextPosition(Direction.S);
		Position p3 = p2.nextPosition(Direction.E);
		Position p4 = p3.nextPosition(Direction.W);
		assertTrue(approxEq(p0, p4));
	}
	
    @Test
	public void testNorthEastThenSouthEastThenSouthWestThenNorthWest() {
		Position p1 = p0.nextPosition(Direction.NE);
		Position p2 = p1.nextPosition(Direction.SE);
		Position p3 = p2.nextPosition(Direction.SW);
		Position p4 = p3.nextPosition(Direction.NW);
		assertTrue(approxEq(p0, p4));
	}
	
    @Test
	public void testNorthThenEastIsNotNorthEast() {
		Position p1 = p0.nextPosition(Direction.N);
		Position p2 = p1.nextPosition(Direction.E);
		Position ne = p0.nextPosition(Direction.NE);
		assertFalse(approxEq(p2, ne));
	}
	
    @Test
	public void testNEisNorthEast() {
		Position p1 = p0.nextPosition(Direction.NE);
		assertTrue(p1.latitude > p0.latitude && p1.longitude > p0.longitude);
	}

    @Test
	public void testNNEisNorthEast() {
		Position p1 = p0.nextPosition(Direction.NNE);
		assertTrue(p1.latitude > p0.latitude && p1.longitude > p0.longitude);
	}

    @Test
	public void testENEisNorthEast() {
		Position p1 = p0.nextPosition(Direction.ENE);
		assertTrue(p1.latitude > p0.latitude && p1.longitude > p0.longitude);
	}
	
    @Test
	public void testSWisSouthWest() {
		Position p1 = p0.nextPosition(Direction.SW);
		assertTrue(p1.latitude < p0.latitude && p1.longitude < p0.longitude);
	}

    @Test
	public void testWSWisSouthWest() {
		Position p1 = p0.nextPosition(Direction.WSW);
		assertTrue(p1.latitude < p0.latitude && p1.longitude < p0.longitude);
	}

    @Test
	public void testSSWisSouthWest() {
		Position p1 = p0.nextPosition(Direction.SSW);
		assertTrue(p1.latitude < p0.latitude && p1.longitude < p0.longitude);
	}

    @Test
	public void testNorthEastThenSouthWest() {
		Position p1 = p0.nextPosition(Direction.NE);
		Position p2 = p1.nextPosition(Direction.SW);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testNorthWestThenSouthEast() {
		Position p1 = p0.nextPosition(Direction.NW);
		Position p2 = p1.nextPosition(Direction.SE);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testSouthEastThenNorthWest() {
		Position p1 = p0.nextPosition(Direction.SE);
		Position p2 = p1.nextPosition(Direction.NW);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testSouthWestThenNorthEast() {
		Position p1 = p0.nextPosition(Direction.SW);
		Position p2 = p1.nextPosition(Direction.NE);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testNorthNorthEastThenSouthSouthWest() {
		Position p1 = p0.nextPosition(Direction.NNE);
		Position p2 = p1.nextPosition(Direction.SSW);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testEastNorthEastThenWestSouthWest() {
		Position p1 = p0.nextPosition(Direction.ENE);
		Position p2 = p1.nextPosition(Direction.WSW);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testNorthNorthWestThenSouthSouthEast() {
		Position p1 = p0.nextPosition(Direction.NNW);
		Position p2 = p1.nextPosition(Direction.SSE);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testEastSouthEastThenWestNorthWest() {
		Position p1 = p0.nextPosition(Direction.ESE);
		Position p2 = p1.nextPosition(Direction.WNW);
		assertTrue(approxEq(p0, p2));
	}
	
    @Test
	public void testSSEthenS() {
		Position p1 = p0.nextPosition(Direction.SSE);
		Position p2 = p1.nextPosition(Direction.S);
		Position stop = new Position(55.94384783614024,-3.1882811949702905);
		assertTrue(approxEq(p2, stop));
	}
	
    @Test
	public void testSSEthenSthenSE() {
		Position p1 = p0.nextPosition(Direction.SSE);
		Position p2 = p1.nextPosition(Direction.S);
		Position p3 = p2.nextPosition(Direction.SE);
		Position stop = new Position(55.94363570410589,-3.1880690629359347);
		assertTrue(approxEq(p3, stop));
	}
	
    @Test
	public void testSSEthenSthenSEthenNE() {
		Position p1 = p0.nextPosition(Direction.SSE);
		Position p2 = p1.nextPosition(Direction.S);
		Position p3 = p2.nextPosition(Direction.SE);
		Position p4 = p3.nextPosition(Direction.NE);
		Position stop = new Position(55.94384783614024,-3.187856930901579);
		assertTrue(approxEq(p4, stop));
	}
	
    @Test
	public void testSSEthenSthenSEthenNEthenWNW() {
		Position p1 = p0.nextPosition(Direction.SSE);
		Position p2 = p1.nextPosition(Direction.S);
		Position p3 = p2.nextPosition(Direction.SE);
		Position p4 = p3.nextPosition(Direction.NE);
		Position p5 = p4.nextPosition(Direction.WNW);
		Position stop = new Position(55.94396264116995,-3.1881340947613324);
		assertTrue(approxEq(p5, stop));
	}
	
}

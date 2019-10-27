package br.usp.iterador.plugin.zoom;

import junit.framework.TestCase;

public class RectangleDataTest extends TestCase {

	public void testIsNotReadyWithZeroPoints() {
		RectangleData data = new RectangleData();
		assertEquals(0, data.getNumberOfPoints());
		assertFalse(data.isDone());
	}

	public void testIsNotReadyWithOnePoints() {
		RectangleData data = new RectangleData();
		data.addPoint(1, 1);
		assertEquals(1, data.getNumberOfPoints());
		assertFalse(data.isDone());
	}

	public void testIsReadyWithTwoPoints() {
		RectangleData data = new RectangleData();
		data.addPoint(1, 1);
		data.addPoint(2, 2);
		assertEquals(2, data.getNumberOfPoints());
		assertTrue(data.isDone());
	}

	public void testGetsFirstPoint() {
		RectangleData data = new RectangleData();
		data.addPoint(1, 2);
		data.addPoint(3, 4);
		assertEquals(1.0, data.getCoordinates()[0][0]);
		assertEquals(2.0, data.getCoordinates()[0][1]);
	}

	public void testGetsSecondPoint() {
		RectangleData data = new RectangleData();
		data.addPoint(1, 2);
		data.addPoint(3, 4);
		assertEquals(3.0, data.getCoordinates()[1][0]);
		assertEquals(4.0, data.getCoordinates()[1][1]);
	}

	public void testReset() {
		RectangleData data = new RectangleData();
		data.addPoint(1, 2);
		data.addPoint(3, 4);
		data.reset();
		assertEquals(0, data.getNumberOfPoints());
		assertFalse(data.isDone());
	}

}

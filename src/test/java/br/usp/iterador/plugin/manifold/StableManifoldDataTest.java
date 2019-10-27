package br.usp.iterador.plugin.manifold;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

public class StableManifoldDataTest extends TestCase {

	public void testAddsAndReturnsInTheCorrectOrder() {
		StableManifoldData data = new StableManifoldData();
		data.getView().addPoint(0, 0);
		data.getView().addPoint(10, 10);
		Rectangle2D rectangle = data.getOrderedRectangle();
		Rectangle2D expected = new Rectangle2D.Double(0, 0, 10, 10);
		assertEquals(expected, rectangle);
	}

	public void testAddsAndReturnsInTheCorrectOrderWithChangedStartingPoint() {
		StableManifoldData data = new StableManifoldData();
		data.getView().addPoint(1, 1);
		data.getView().addPoint(10, 10);
		Rectangle2D rectangle = data.getOrderedRectangle();
		Rectangle2D expected = new Rectangle2D.Double(1, 1, 9, 9);
		assertEquals(expected, rectangle);
	}

	public void testAddsInMixedAndReturnsInTheCorrectOrder() {
		StableManifoldData data = new StableManifoldData();
		data.getView().addPoint(10, 10);
		data.getView().addPoint(0, 0);
		Rectangle2D rectangle = data.getOrderedRectangle();
		Rectangle2D expected = new Rectangle2D.Double(0, 0, 10, 10);
		assertEquals(expected, rectangle);
	}

	public void testAddsInMixedAndReturnsInTheCorrectOrderWithChangedStartingPoint() {
		StableManifoldData data = new StableManifoldData();
		data.getView().addPoint(10, 10);
		data.getView().addPoint(1, 1);
		Rectangle2D rectangle = data.getOrderedRectangle();
		Rectangle2D expected = new Rectangle2D.Double(1, 1, 9, 9);
		assertEquals(expected, rectangle);
	}

}

package br.usp.iterador.plugin.bacia.model;

import br.usp.iterador.AbstractTestCase;

public class CloudTest extends AbstractTestCase {

	public void testContainsPointWithoutReverse() {
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		assertTrue(cloud.contains(0.5, 0.5));
	}

	public void testContainsPointWithReverse() {
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		cloud.setReverse(true);
		assertTrue(cloud.contains(1.5, 1.5));
	}

	public void testDoesNotContainsPointWithoutReverse() {
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		assertFalse(cloud.contains(0.5, 1.5));
	}

	public void testDoesNotContainsPointWithReverse() {
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		cloud.setReverse(true);
		assertFalse(cloud.contains(0.5, 0.5));
	}

}

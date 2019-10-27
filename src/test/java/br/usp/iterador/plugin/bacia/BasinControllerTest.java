package br.usp.iterador.plugin.bacia;

import java.awt.Color;

import br.usp.iterador.AbstractTestCase;
import br.usp.iterador.plugin.bacia.model.Cloud;

public class BasinControllerTest extends AbstractTestCase {

	public void testFindsContainedCloud() {
		Basin basin = new Basin();
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		BasinController controller = new BasinController(null, basin);
		basin.addAttractor(cloud);
		Cloud containedCloud = controller.getContainedCloud(0.5, 0.5);
		assertEquals(cloud, containedCloud);
	}

	public void testDoesntFindContainedCloud() {
		Basin basin = new Basin();
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		BasinController controller = new BasinController(null, basin);
		basin.addAttractor(cloud);
		Cloud containedCloud = controller.getContainedCloud(0.5, 1.01);
		assertNull(containedCloud);
	}

	public void testFindsContainedCloudColor() {
		Basin basin = new Basin();
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		BasinController controller = new BasinController(null, basin);
		basin.addAttractor(cloud);
		Color color = controller.getPointColor(new double[] { 0.5, 0.5 });
		assertEquals(cloud.getColor(), color);
	}

	public void testDoesntFindContainedCloudColor() {
		Basin basin = new Basin();
		Cloud cloud = new Cloud("myCloud",
				createPolygon(0, 0, 0, 1, 1, 1, 1, 0));
		BasinController controller = new BasinController(null, basin);
		basin.addAttractor(cloud);
		Color color = controller.getPointColor(new double[] { 0.5, 1.01 });
		assertEquals(Color.BLACK, color);
	}

}

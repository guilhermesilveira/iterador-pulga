package br.usp.iterador.logic.image;

import junit.framework.TestCase;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Scale;

public class ProportionalPlotTest extends TestCase {

	public void testZeroLength() {
		Application app = new Application();
		app.setXScale(new Scale("x", 0, 1.0));
		app.setYScale(new Scale("y", 0, 1.0));
		app.getImage().setHeight(100);
		app.getImage().setWidth(0);
		ProportionalPlot plot = new ProportionalPlot(app);
		plot.update();
		assertEquals(0, app.getImage().getHeight());
	}

	public void testGeneratesSameProportion() {
		Application app = new Application();
		app.setXScale(new Scale("x", 0, 1.0));
		app.setYScale(new Scale("y", 0, 1.0));
		app.getImage().setHeight(1);
		app.getImage().setWidth(100);
		ProportionalPlot plot = new ProportionalPlot(app);
		plot.update();
		assertEquals(100, app.getImage().getHeight());
	}

}

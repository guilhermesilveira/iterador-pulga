package br.usp.iterador.plugin.bacia.model;

import java.awt.Point;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.model.Scale;

/**
 * Attractor builder.
 * 
 * @author Guilherme Silveira
 */
public class AttractorBuilder {

	/**
	 * Current polygon
	 */
	private MyPolygon polygon = new MyPolygon();

	/**
	 * Clears this builders data
	 */
	public void clear() {
		this.polygon = new MyPolygon();
	}

	/**
	 * Creates an attractor based on this builders data
	 * 
	 * @return
	 */
	public synchronized Cloud create() {
		return new Cloud("Unnamed cloud", polygon);
	}

	/**
	 * Prepares the polygon
	 * 
	 * @param p
	 */
	public void add(Point p, Scale scales[], int maxx, int maxy) {
		GUIHelper helper = new GUIHelper(maxx, maxy);
		this.polygon.addPoint(helper.mudaEscala(p.x, 0, maxx, scales[0]),
				helper.mudaEscala(maxy - p.y, 0, maxy, scales[1]));
	}

	public MyPolygon getCurrentPolygon() {
		return this.polygon;
	}

}

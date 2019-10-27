package br.usp.iterador.plugin.manifold;

import java.awt.geom.Rectangle2D;

import org.apache.log4j.Logger;

import br.usp.iterador.plugin.zoom.RectangleData;

public class StableManifoldData {

	private static final Logger LOG = Logger
			.getLogger(StableManifoldData.class);

	private RectangleData view = new RectangleData();

	public StableManifoldData() {
		LOG.debug("Creating new stable manifold data");
	}

	public Rectangle2D getOrderedRectangle() {
		double[][] c = view.getCoordinates();
		Rectangle2D focus = new Rectangle2D.Double(Math.min(c[0][0], c[1][0]),
				Math.min(c[0][1], c[1][1]), Math.abs(c[0][0] - c[1][0]), Math
						.abs(c[0][1] - c[1][1]));
		return focus;
	}

	public RectangleData getView() {
		return view;
	}

}

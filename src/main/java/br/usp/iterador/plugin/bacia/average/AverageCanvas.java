package br.usp.iterador.plugin.bacia.average;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.GridOptions;
import br.usp.iterador.model.Scale;
import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.bacia.BasinController;

/**
 * Draws the current average data.
 *
 * @author Guilherme Silveira
 */
public class AverageCanvas extends Canvas implements AverageListener {

	private static final long serialVersionUID = 3257845489339805752L;

	private Basin basin;

	private final PointListener pointListener = new PointListener();

	private final List<Double[]> averages;

	private final GridOptions grid;

	private final BasinController controller;

	/**
	 * Creates a new bacia canvas.
	 */
	public AverageCanvas(Application app, List<Double[]> averages,
			BasinController controller) {
		this.grid = app.getGrid();
		this.addMouseListener(this.pointListener);
		this.averages = averages;
		this.controller = controller;
	}

	public void paint(Graphics gra) {

		super.paint(gra);

		Graphics2D g = (Graphics2D) gra;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		GUIHelper helper = new GUIHelper(this.getWidth(), this.getHeight());

		if (this.basin == null) {
			return;
		}

		g.setColor(basin.getSampleAverageColor());

		double p1, p2;
		Scale[] scales = basin.getAveragesInfo().getAverageScales();
		for (Double[] v : averages) {
			if ((v[0] > scales[0].getMax() && v[0] < scales[0].getMin())
					|| (v[1] > scales[1].getMax() && v[1] < scales[1].getMin()))
				continue;
			p1 = helper.mudaEscala(v[0], scales[0], 0, this.getWidth());
			p2 = helper.mudaEscala(v[1], scales[1], 0, this.getHeight());
			g.drawRect((int) p1, this.getHeight() - (int) p2, 0, 0);
		}

		// grid
		g.setColor(grid.getColor());
		helper.linhasHorizontais(g, 5, scales[1], (DecimalFormat) DecimalFormat
				.getInstance(Locale.ENGLISH));
		helper.linhasVerticais(g, 5, scales[0], (DecimalFormat) DecimalFormat
				.getInstance(Locale.ENGLISH));

		// draws old polygons
		AverageCanvasDrawer drawer = new AverageCanvasDrawer(basin
				.getAveragesInfo(), getWidth(), getHeight(), helper, controller);
		drawer.drawOldPolygons(g, basin.getAttractors());

		// should display the current polygon?
		drawer.drawCurrentPolygon(g);

	}

	public void updateAverages(Basin b) {
		this.basin = b;
		// repaint();
	}

	public Point waitForNextPoint() {
		return this.pointListener.waitForNextPoint();
	}

	public void clear() {
	}

	public double[] getTotal() {
		return null;
	}

	public void initData() {
	}

	public void pinta(double[] x, HashMap<String, Integer> valores,
			Application dados) {
	}

}

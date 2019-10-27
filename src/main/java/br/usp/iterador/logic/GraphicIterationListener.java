package br.usp.iterador.logic;

import java.awt.Graphics2D;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.gui.Painter;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;
import br.usp.iterador.util.WorldView;

/**
 * An iteration listener which paints points.
 *
 * @author Guilherme Silveira
 */
public class GraphicIterationListener implements IterationListener {

	private Graphics2D g;

	private GUIHelper helper;

	private final WorldView view;

	public GraphicIterationListener(Application app) {
		this.view = new WorldView(app);
	}

	public boolean onPoint(Application dados, ExecutionIterable iteration) {

		// gets x and y
		double val[] = view.getAxisValues(iteration);
		double x = val[0];
		if (x < dados.getXScale().getMin() || x > dados.getXScale().getMax()) {
			return true;
		}
		double y = val[1];
		if (y < dados.getYScale().getMin() || y > dados.getYScale().getMax()) {
			return true;
		}

		// translate them
		int px = (int) helper.mudaEscala(x, dados.getXScale().getMin(), dados
				.getXScale().getMax(), 0, helper.getWidth());
		int py = (int) helper.mudaEscala(y, dados.getYScale().getMin(), dados
				.getYScale().getMax(), 0, helper.getHeight());

		// paint them
		paintPoint(g, px, helper.getHeight() - py, iteration);
		return true;

	}

	protected void paintPoint(Graphics2D g, int tx, int ty,
			ExecutionIterable iteration) {
		Painter.paintPoint(tx, ty, g, iteration.getColor());
	}

	public void init(Graphics2D g, GUIHelper helper) {
		this.helper = helper;
		this.g = g;
	}

	public void initData() {
	}

}

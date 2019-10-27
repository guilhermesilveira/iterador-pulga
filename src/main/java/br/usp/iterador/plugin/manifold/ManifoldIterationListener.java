package br.usp.iterador.plugin.manifold;

import java.awt.Color;
import java.awt.Graphics2D;

import br.usp.iterador.gui.Painter;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.logic.GraphicIterationListener;
import br.usp.iterador.model.Application;

public class ManifoldIterationListener extends GraphicIterationListener {

	private int result;

	public ManifoldIterationListener(Application app) {
		super(app);
	}

	public void setResult(int result) {
		this.result = result;
	}

	protected void paintPoint(Graphics2D g, int tx, int ty,
			ExecutionIterable iteration) {
		if (iteration.getIteration() > result) {
			Painter.paintPoint(tx, ty, g, Color.RED);
		} else {
			Painter.paintPoint(tx, ty, g, Color.BLACK);
		}
	}

}

package br.usp.iterador.plugin.cor;

import java.awt.Graphics2D;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;

/**
 * A listener which changes colors each N iterations.
 *
 * @author Guilherme Silveira
 */
public class ColorIterationListener implements IterationListener {

	private final ColorDataInfo colors;

	public ColorIterationListener(ColorDataInfo data) {
		this.colors = data;
	}

	public boolean onPoint(Application data, ExecutionIterable iteration) {
		int currentIteration = iteration.getIteration() - data.getTrashPoints();
		// if not supposed to change, skip
		if (currentIteration != 0 && currentIteration % colors.getNIterations() != 0) {
			return true;
		}
		iteration.setColor(colors.getColorFor(currentIteration));
		return true;
	}

	public void init(Graphics2D g, GUIHelper helper) {
	}

	public void initData() {
	}

}

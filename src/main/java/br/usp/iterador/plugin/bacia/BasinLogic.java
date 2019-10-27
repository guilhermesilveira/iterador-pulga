package br.usp.iterador.plugin.bacia;

import org.apache.log4j.Logger;

import br.usp.iterador.internal.logic.IterationExecuter;
import br.usp.iterador.internal.logic.ValueApplier;
import br.usp.iterador.logic.IterationListenerNull;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.average.IterationAverageListener;

public class BasinLogic {

	private static final Logger logger = Logger.getLogger(BasinLogic.class);

	private BasinController controller;

	public BasinLogic(BasinController controller) {
		this.controller = controller;
	}

	/**
	 * Iterates over a specific point
	 * 
	 * @param executer
	 *            executer
	 * @param y
	 *            initial y value
	 * @param x
	 *            initial x value
	 * @return iteration result
	 */
	public IterationResult iterate(Application app, IterationExecuter executer,
			double x, double y) {

		// applies both values
		new ValueApplier(app).applyValue(app.getXScale().getField(), x);
		new ValueApplier(app).applyValue(app.getYScale().getField(), y);

		try {
			controller.resetIteration();
			IterationAverageListener listener = controller.getAverageListener();
			int iterations = executer
					.promoteIteration(new IterationListenerNull());
			return new IterationResult(listener.getAverage(), iterations);
		} catch (Exception e) {
			logger.error("Unable to iterate", e);
			return null;
		}
	}

	/**
	 * Generates a random point, iterates over it and saves its average
	 * 
	 * @param listener
	 * @return
	 */
	public Double[] iterateOverARandomPoint(Application app,
			IterationExecuter executer, double rx, double ry) {

		// iterates over it
		double[] v = iterate(app, executer, rx, ry).getAverages();
		logger.debug(String.format("(%f,%f) - average result: (%f,%f)", rx, ry,
				v[0], v[1]));
		return new Double[] { v[0], v[1] };

	}

}

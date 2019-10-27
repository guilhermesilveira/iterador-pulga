package br.usp.iterador.plugin.manifold;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.util.WorldView;

/**
 * Iteration listener. When it exits the selected area or has been too long
 * there, stop
 * 
 * @author Guilherme Silveira
 */
public class StableManifoldIterationListener implements IterationListener {

	private final Rectangle2D focus;

	private final StableManifoldValues values;

	private final WorldView view;

	private final int maxValue, minValue;

	public StableManifoldIterationListener(Rectangle2D focus, int maxValue,
			int minValue, Application app, StableManifoldValues values) {
		this.values = values;
		this.focus = focus;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.view = new WorldView(app);
	}

	public boolean onPoint(Application dados, ExecutionIterable iteration) {
		double[] value = view.getAxisValues(iteration);
		if (focus.contains(value[0], value[1])) {
			logger.debug("contains: " + Arrays.toString(value));
			values.increase();
			if (values.getTotal() == this.maxValue) {
				// stop!!! maximum reached
				return false;
			}
		} else {
			if (values.getTotal() != 0) {
				// stop!!! its quitting
				return false;
			}
		}
		return true;
	}

	private static final Logger logger = Logger
			.getLogger(StableManifoldIterationListener.class);

	public void init(Graphics2D g, GUIHelper helper) {
	}

	public void initData() {
		logger.debug("Reset the total");
		values.reset();
	}

}

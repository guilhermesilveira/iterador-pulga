package br.usp.iterador.plugin.bacia;

import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.logic.NewIterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.model.Cloud;

public class BasinIterationListener implements NewIterationListener {

	private Cloud lastContainedCloud;

	private int accumulatedIterations;

	private BasinController controller;

	private Basin basin;

	public BasinIterationListener(BasinController c, Basin basin) {
		this.controller = c;
		this.basin = basin;
	}

	public boolean nextPluginIterationCode(boolean validPoint, double x,
			double y, int currentPoint) {
		if (!validPoint) {
			return true;
		}
		controller.getAverageListener().add(x, y);
		return !shouldEarlyQuit(currentPoint, controller.getAverageListener()
				.getAverage());
	}

	/**
	 * Should it quit earlier?
	 *
	 * @param currentPointNumber
	 *            the current point number
	 * @param currentAverage
	 *            the current average
	 * @return should it stop
	 */
	private boolean shouldEarlyQuit(int currentPointNumber,
			double currentAverage[]) {
		if (currentPointNumber == 0 || basin.getEarlyQuit() <= 0) {
			return false;
		}
		if (currentPointNumber % basin.getEarlyQuit() == 0) {
			// logger.debug("checking value on iteration " + currentPointNumber
			// + "," + Arrays.toString(currentAverage));
			Cloud cloud = controller.getContainedCloud(currentAverage[0],
					currentAverage[1]);
			if (cloud == null) {
				this.lastContainedCloud = null;
				accumulatedIterations = 0;
				return false;
			}
			if (cloud == lastContainedCloud) {
				accumulatedIterations++;
				// stops if this accumulated for more than max iterations
				return accumulatedIterations >= basin.getMaxIterationsInside();
			} else {
				accumulatedIterations = 1;
				this.lastContainedCloud = cloud;
			}
		}
		return false;
	}

	public boolean onPoint(Application data, ExecutionIterable iteration) {
		BasinIteration basinIteration = (BasinIteration) iteration;
		// false = stop
		return nextPluginIterationCode((iteration.getIteration() >= data
				.getTrashPoints()), basinIteration.getXAverage(),
				basinIteration.getYAverage(), iteration.getIteration()
						- data.getTrashPoints() + 1);
	}

	public void init() {
		lastContainedCloud = null;
		accumulatedIterations = 0;
	}

	public String getExtraCode() {
		AverageFunctions functions = basin.getAveragesInfo()
				.getAverageFunctions();
		return "public double getXAverage() { return " + functions.getX()
				+ "; }" + '\n' + "public double getYAverage() { return "
				+ functions.getY() + "; }";
	}

	public Class getExtraInterface() {
		return BasinIteration.class;
	}
}

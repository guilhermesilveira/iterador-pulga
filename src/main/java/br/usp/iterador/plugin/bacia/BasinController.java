package br.usp.iterador.plugin.bacia;

import java.awt.Color;

import br.usp.iterador.plugin.bacia.average.IterationAverageListener;
import br.usp.iterador.plugin.bacia.model.Cloud;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Basin controller.
 * 
 * @author Guilherme Silveira
 */
public class BasinController {

	private IterationAverageListener averageListener = new IterationAverageListener();

	private Basin basin;

	private NewAttractorLogic newAttractorLogic;

	public BasinController(WindowManager windows, Basin basin) {
		newAttractorLogic = new NewAttractorLogic(windows);
		this.basin = basin;
	}

	public IterationAverageListener getAverageListener() {
		return averageListener;
	}

	/**
	 * Returns whether the point if contained in a cloud
	 * 
	 * @return the cloud
	 */
	public Cloud getContainedCloud(double a, double b) {
		for (Cloud cloud : basin.getAttractors()) {
			if (cloud.contains(a, b)) {
				return cloud;
			}
		}
		return null;
	}

	public NewAttractorLogic getNewAttractorLogic() {
		return newAttractorLogic;
	}

	public void resetIteration() {
		averageListener.reset();
	}

	/**
	 * Returns the point color
	 * 
	 * @param p
	 *            the point
	 * @return the color
	 */
	public Color getPointColor(double p[]) {
		Cloud cloud = getContainedCloud(p[0], p[1]);
		return cloud == null ? Color.BLACK : cloud.getColor();
	}

}

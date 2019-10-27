package br.usp.iterador.plugin.bacia;

import java.awt.Color;
import java.util.ArrayList;

import br.usp.iterador.plugin.bacia.model.Cloud;

/**
 * Basin's internal data.
 * 
 * @author Guilherme Silveira
 */
public class Basin {

	private ArrayList<Cloud> attractors = new ArrayList<Cloud>();

	private AveragesInfo averagesInfo = new AveragesInfo();

	private Color sampleAverageColor = Color.YELLOW;

	private int earlyQuit = 0, maxIterationsInside = 1;

	public void addAttractor(Cloud attractor) {
		this.attractors.add(attractor);
	}

	public void clearAttractors() {
		this.attractors.clear();
	}

	/**
	 * @return Returns the attractors.
	 */
	public ArrayList<Cloud> getAttractors() {
		return attractors;
	}

	/**
	 * @return
	 */
	public Color getSampleAverageColor() {
		return this.sampleAverageColor;
	}

	public void setSampleAverageColor(Color sampleAverageColor) {
		this.sampleAverageColor = sampleAverageColor;
	}

	/**
	 * @return Returns the earlyQuit.
	 */
	public int getEarlyQuit() {
		return earlyQuit;
	}

	/**
	 * @param earlyQuit
	 *            The earlyQuit to set.
	 */
	public void setEarlyQuit(int earlyQuit) {
		this.earlyQuit = earlyQuit;
	}

	/**
	 * Returns all average infos
	 * 
	 * @return infos
	 */
	public AveragesInfo getAveragesInfo() {
		return averagesInfo;
	}

	public int getMaxIterationsInside() {
		return maxIterationsInside;
	}

	public void setMaxIterationsInside(int maxIterationsInside) {
		this.maxIterationsInside = Math.min(1, maxIterationsInside);
	}

}

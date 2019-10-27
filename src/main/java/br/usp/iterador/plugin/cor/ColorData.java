package br.usp.iterador.plugin.cor;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Color data for the color plugin
 *
 * @author Guilherme Silveira
 */
public class ColorData implements Serializable, ColorDataInfo {

	/**
	 * serialized id
	 */
	private static final long serialVersionUID = 10000L;

	private int nIterations = 10000;

	private ArrayList<Color> colors = new ArrayList<Color>();

	/**
	 * Constructs with two colors
	 */
	public ColorData() {
		this.colors.add(Color.BLUE);
	}

	public int getNIterations() {
		return nIterations;
	}

	public void setNIterations(int iterations) {
		nIterations = iterations;
	}

	public void removeColorAt(int i) {
		this.colors.remove(i);
	}

	/**
	 * @return Returns the colors.
	 */
	public ArrayList<Color> getColors() {
		return colors;
	}

	/**
	 * Adds a new color
	 *
	 * @param c
	 */
	public void addColor(Color c) {
		this.colors.add(c);
	}

	/**
	 * Returns the specific color for this iteration
	 * @param iteration
	 * @return
	 */
	public Color getColorFor(int iteration) {
		int currentColor = (iteration / this.nIterations) % this.colors.size();
		return colors.get(currentColor);
	}

}

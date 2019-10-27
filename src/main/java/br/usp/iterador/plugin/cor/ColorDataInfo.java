package br.usp.iterador.plugin.cor;

import java.awt.Color;
import java.util.ArrayList;

public interface ColorDataInfo {

	int getNIterations();

	/**
	 * @return Returns the colors.
	 */
	ArrayList<Color> getColors();

	/**
	 * Returns the specific color for this iteration
	 * @param iteration
	 * @return
	 */
	Color getColorFor(int iteration);

}
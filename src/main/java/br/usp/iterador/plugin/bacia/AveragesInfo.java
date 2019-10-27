package br.usp.iterador.plugin.bacia;

import br.usp.iterador.model.Scale;

/**
 * Averages information.
 * 
 * @author Guilherme Silveira
 */
public class AveragesInfo {

	private AverageFunctions averageFunctions = new AverageFunctions();

	private Scale xScale = new Scale("x1", 0, 2);

	private Scale yScale = new Scale("x2", 0, 2);

	public AverageFunctions getAverageFunctions() {
		return averageFunctions;
	}

	public Scale getXScale() {
		return xScale;
	}

	public Scale getYScale() {
		return yScale;
	}

	public Scale[] getAverageScales() {
		return new Scale[] { xScale, yScale };
	}

}
